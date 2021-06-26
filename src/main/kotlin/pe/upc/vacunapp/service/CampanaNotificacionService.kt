package pe.upc.vacunapp.service

import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pe.upc.vacunapp.dao.CampanaDAO
import pe.upc.vacunapp.dao.CampanaNotificacionDAO
import pe.upc.vacunapp.domain.CampanaNotificacion
import pe.upc.vacunapp.utils.getCurrentDateTime
import javax.persistence.EntityNotFoundException

@Service
class CampanaNotificacionService(private val campanaNotificacionDAO: CampanaNotificacionDAO, private val campanaDAO: CampanaDAO): BasicCrud<CampanaNotificacion,Int>  {

    override fun findAll(): List<CampanaNotificacion> {
        return this.campanaNotificacionDAO.findAll()
    }

    override fun findById(id: Int): CampanaNotificacion? {
        return this.campanaNotificacionDAO.findByIdOrNull(id)
    }

    override fun save(t: CampanaNotificacion): CampanaNotificacion {
        t.d_fec_crea = getCurrentDateTime()

        var existsCampana = this.campanaDAO.existsById(t.id_campana)

        if(!this.campanaNotificacionDAO.existsById(t.id_campana_notif) && existsCampana){
            return this.campanaNotificacionDAO.save(t)
        }
        else if( !existsCampana ){
            return throw EntityNotFoundException("Entity Campana ${t.id_campana} does not exists")
        }
        else{
            return throw DuplicateKeyException("${t.id_campana_notif} does exists")
        }
    }

    override fun update(t: CampanaNotificacion): CampanaNotificacion {
        t.d_fec_mod = getCurrentDateTime()

        var existsCampana = this.campanaDAO.existsById(t.id_campana)

        if(this.campanaNotificacionDAO.existsById(t.id_campana_notif) && existsCampana){
            return this.campanaNotificacionDAO.save(t)
        }
        else if( !existsCampana ){
            return throw EntityNotFoundException("Entity Campana ${t.id_campana} does not exists")
        }
        else{
            throw EntityNotFoundException("${t.id_campana_notif} does not exists")
        }
    }

    override fun deleteById(id: Int): CampanaNotificacion {
        return this.findById(id)?.apply {
            this@CampanaNotificacionService.campanaNotificacionDAO.deleteById(this.id_campana_notif)
        } ?: throw EntityNotFoundException("$id does not exists")
    }
}