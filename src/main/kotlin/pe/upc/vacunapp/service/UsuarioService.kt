package pe.upc.vacunapp.service

import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pe.upc.vacunapp.dao.UsuarioDAO
import pe.upc.vacunapp.domain.Usuario
import pe.upc.vacunapp.utils.getCurrentDateTime
import javax.persistence.EntityNotFoundException

@Service
class UsuarioService(private val usuarioDAO: UsuarioDAO):UserCrud<Usuario,String> {
    override fun findAll(): List<Usuario> {
        return this.usuarioDAO.findAll()
    }

    override fun findByUserPass(id: String, pass: String): Usuario? {
        var usuario = this.usuarioDAO.findByIdOrNull(id)
        if(usuario != null && usuario.s_password.equals(pass)){
            return usuario
        } else if(usuario == null){
            return throw EntityNotFoundException("El usuario no existe")
        } else if( !usuario.s_password.equals(pass) ){
            return throw EntityNotFoundException("Contraseña inválida")
        }
        return null
    }

    override fun save(t: Usuario): Usuario {
        t.d_fec_crea = getCurrentDateTime()
        return if(!this.usuarioDAO.existsById(t.s_email))	this.usuarioDAO.save(t) else throw DuplicateKeyException("${t.s_email} does exists")
    }

    override fun update(t: Usuario): Usuario {
        t.d_fec_mod = getCurrentDateTime()
        return if(this.usuarioDAO.existsById(t.s_email))	this.usuarioDAO.save(t) else throw EntityNotFoundException("${t.s_email} does not exists")
    }

}