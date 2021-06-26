package pe.upc.vacunapp.service

import pe.upc.vacunapp.dao.VacunaDAO
import pe.upc.vacunapp.domain.Vacuna
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException
import pe.upc.vacunapp.utils.*

@Service
class VacunaService(private val vacunaDAO: VacunaDAO): BasicCrud<Vacuna, Int> {
    override fun findAll(): List<Vacuna> {
        return this.vacunaDAO.findAll()
    }

    override fun findById(id: Int): Vacuna? {
        return this.vacunaDAO.findByIdOrNull(id)
    }

    override fun save(t: Vacuna): Vacuna {
        t.d_fec_crea = getCurrentDateTime()
        return if(!this.vacunaDAO.existsById(t.id_vacuna))	this.vacunaDAO.save(t) else throw DuplicateKeyException("${t.id_vacuna} does exists")
    }

    override fun update(t: Vacuna): Vacuna {
        t.d_fec_mod = getCurrentDateTime()
        return if(this.vacunaDAO.existsById(t.id_vacuna))	this.vacunaDAO.save(t) else throw EntityNotFoundException("${t.id_vacuna} does not exists")
    }

    override fun deleteById(id: Int): Vacuna {
        return this.findById(id)?.apply {
            this@VacunaService.vacunaDAO.deleteById(this.id_vacuna)
        } ?: throw EntityNotFoundException("$id does not exists")
    }
}