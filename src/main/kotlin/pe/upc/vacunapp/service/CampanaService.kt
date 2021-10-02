package pe.upc.vacunapp.service

import pe.upc.vacunapp.dao.CampanaDAO
import pe.upc.vacunapp.dao.LocalVacunacionDAO
import pe.upc.vacunapp.dao.VacunaDAO
import pe.upc.vacunapp.domain.Campana
import pe.upc.vacunapp.utils.getCurrentDateTime
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException


@Service
class CampanaService(private val campanaDAO: CampanaDAO,
                     private val localVacunacionDAO: LocalVacunacionDAO,
                     private val vacunaDao: VacunaDAO): BasicCrud<Campana,Int> {

    override fun findAll(): List<Campana> {
        return this.campanaDAO.findAll()
    }

    override fun findById(id: Int): Campana? {
        return this.campanaDAO.findByIdOrNull(id)
    }

    override fun save(t: Campana): Campana {
        t.d_fec_crea = getCurrentDateTime()
        var existsLocal = this.localVacunacionDAO.existsById(t.id_local)
        var existsVacuna = this.vacunaDao.existsById(t.id_vacuna)
        if(!this.campanaDAO.existsById(t.id_campana) && existsLocal && existsVacuna){
            return this.campanaDAO.save(t)
        }
        else if( !existsLocal ){
            return throw EntityNotFoundException("Entity Local ${t.id_local} does not exists")
        }
        else if( !existsVacuna ){
            return throw EntityNotFoundException("Entity Vacuna ${t.id_vacuna} does not exists")
        }
        else{
            return throw DuplicateKeyException("${t.id_campana} does exists")
        }
    }

    override fun update(t: Campana): Campana {
        t.d_fec_mod = getCurrentDateTime()
        var existsLocal = this.localVacunacionDAO.existsById(t.id_local)
        var existsVacuna = this.vacunaDao.existsById(t.id_vacuna)

        if(this.campanaDAO.existsById(t.id_campana) && existsLocal && existsVacuna){
            return this.campanaDAO.save(t)
        }
        else if( !existsLocal ){
            return throw EntityNotFoundException("Entity Local ${t.id_local} does not exists")
        }
        else if( !existsVacuna ){
            return throw EntityNotFoundException("Entity Vacuna ${t.id_vacuna} does not exists")
        }
        else{
            throw EntityNotFoundException("${t.id_campana} does not exists")
        }
    }

    override fun deleteById(id: Int): Campana {
        return this.findById(id)?.apply {
            this@CampanaService.campanaDAO.deleteById(this.id_campana)
        } ?: throw EntityNotFoundException("$id does not exists")
    }
}