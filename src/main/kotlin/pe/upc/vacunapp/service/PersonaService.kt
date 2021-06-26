package pe.upc.vacunapp.service

import pe.upc.vacunapp.dao.PersonaDAO
import pe.upc.vacunapp.domain.Persona
import pe.upc.vacunapp.utils.getCurrentDateTime
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class PersonaService(private val personaDAO: PersonaDAO): BasicCrud<Persona,String> {
    override fun findAll(): List<Persona> {
        return this.personaDAO.findAll()
    }

    override fun findById(id: String): Persona? {
        return this.personaDAO.findByIdOrNull(id)
    }

    override fun save(t: Persona): Persona {
        t.d_fec_crea = getCurrentDateTime()
        return if(!this.personaDAO.existsById(t.s_dni))	this.personaDAO.save(t) else throw DuplicateKeyException("${t.s_dni} does exists")
    }

    override fun update(t: Persona): Persona {
        return throw EntityNotFoundException("${t.s_dni} can't be updated")
    }

    override fun deleteById(id: String): Persona {
        return throw EntityNotFoundException("$id can't be deleted")
    }
}