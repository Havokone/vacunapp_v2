package pe.upc.vacunapp.service

import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pe.upc.vacunapp.dao.CampanaDAO
import pe.upc.vacunapp.dao.PersonaCampanaDAO
import pe.upc.vacunapp.dao.PersonaDAO
import pe.upc.vacunapp.domain.PersonaCampana
import pe.upc.vacunapp.utils.getCurrentDateTime
import javax.persistence.EntityNotFoundException

@Service
class PersonaCampanaService(private val personaCampanaDAO: PersonaCampanaDAO, private val personaDAO: PersonaDAO, private val campanaDAO: CampanaDAO): BasicCrud<PersonaCampana,String> {
    override fun findAll(): List<PersonaCampana> {
        return this.personaCampanaDAO.findAll()
    }

    override fun findById(id: String): PersonaCampana? {
        return this.personaCampanaDAO.findByIdOrNull(id)
    }

    override fun save(t: PersonaCampana): PersonaCampana {
        t.d_fec_crea = getCurrentDateTime()
        t.id_persona_campana = StringBuilder("").append(t.id_campana.toString()).append("-").append(t.i_sesion.toString()).append("-").append(t.s_dni).toString()

        var existsPersona = this.personaDAO.existsById(t.s_dni)
        var existsCampana = this.campanaDAO.existsById(t.id_campana)

        if(!this.personaCampanaDAO.existsById(t.id_persona_campana) && existsPersona && existsCampana){
            return this.personaCampanaDAO.save(t)
        }
        else if( !existsPersona ){
            return throw EntityNotFoundException("Entity Persona ${t.s_dni} does not exists")
        }
        else if( !existsCampana ){
            return throw EntityNotFoundException("Entity Campana ${t.id_campana} does not exists")
        }
        else{
            return throw DuplicateKeyException("${t.id_persona_campana} does exists")
        }

    }

    override fun update(t: PersonaCampana): PersonaCampana {
        throw EntityNotFoundException("${t.id_persona_campana} can't be updated")
    }

    override fun deleteById(id: String): PersonaCampana {
        return this.findById(id)?.apply {
            this@PersonaCampanaService.personaCampanaDAO.deleteById(this.id_persona_campana)
        } ?: throw EntityNotFoundException("$id does not exists")
    }
}