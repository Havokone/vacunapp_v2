package pe.upc.vacunapp.service

import pe.upc.vacunapp.dao.LocalVacunacionDAO
import pe.upc.vacunapp.domain.LocalVacunacion
import pe.upc.vacunapp.domain.Vacuna
import pe.upc.vacunapp.utils.getCurrentDateTime
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class LocalVacunacionService(private val localVacunacionDAO: LocalVacunacionDAO): BasicCrud<LocalVacunacion, Int> {
    override fun findAll(): List<LocalVacunacion> {
        return this.localVacunacionDAO.findAll()
    }

    override fun findById(id: Int): LocalVacunacion? {
        return this.localVacunacionDAO.findByIdOrNull(id)
    }

    override fun save(t: LocalVacunacion): LocalVacunacion {
        t.d_fec_crea = getCurrentDateTime()
        return if(!this.localVacunacionDAO.existsById(t.id_local))	this.localVacunacionDAO.save(t) else throw DuplicateKeyException("${t.id_local} does exists")
    }

    override fun update(t: LocalVacunacion): LocalVacunacion {
        t.d_fec_mod = getCurrentDateTime()
        return if(this.localVacunacionDAO.existsById(t.id_local))	this.localVacunacionDAO.save(t) else throw EntityNotFoundException("${t.id_local} does not exists")
    }

    override fun deleteById(id: Int): LocalVacunacion {
        return this.findById(id)?.apply {
            this@LocalVacunacionService.localVacunacionDAO.deleteById(this.id_local)
        } ?: throw EntityNotFoundException("$id does not exists")
    }
}