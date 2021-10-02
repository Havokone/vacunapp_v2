package pe.upc.vacunapp.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pe.upc.vacunapp.dao.CampanaDAO
import pe.upc.vacunapp.dao.PersonaCampanaDAO
import pe.upc.vacunapp.dao.PersonaDAO
import pe.upc.vacunapp.domain.Campana
import pe.upc.vacunapp.domain.PersonaCampana

@Service
class CampanaDisponibleService(private val personaDAO: PersonaDAO,
                               private val campanaDAO: CampanaDAO,
                               private val personaCampanaDAO: PersonaCampanaDAO):FindByDNI<Campana,String> {
    override fun findAll(dni: String): List<Campana> {
        var campanasDisponibles:List<Campana> = mutableListOf()

        if ( dni != null && personaDAO.findAll().any { persona -> persona.s_dni.equals(dni) } ){

            val vacunaciones = personaCampanaDAO.findAll()

            if( vacunaciones.any { personaCampana -> personaCampana.s_dni.equals(dni) } ){
                val ultimaVacunacion:PersonaCampana = vacunaciones.last { personaCampana -> personaCampana.s_dni.equals(dni) }
                val ultimaCampana = campanaDAO.findByIdOrNull(ultimaVacunacion.id_campana)

                campanasDisponibles = campanaDAO.findAll().filter { campana -> campana.id_vacuna == ultimaCampana?.id_vacuna }

                return campanasDisponibles
            }
            else{
                return campanaDAO.findAll()
            }
        }
        return campanasDisponibles
    }
}