package pe.upc.vacunapp.service

import com.upc.vacunapp.utils.formatPrint
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pe.upc.vacunapp.dao.CampanaDAO
import pe.upc.vacunapp.dao.PersonaCampanaDAO
import pe.upc.vacunapp.dao.PersonaDAO
import pe.upc.vacunapp.dao.VacunaDAO
import pe.upc.vacunapp.domain.PersonaCampana
import pe.upc.vacunapp.utils.Constantes
import pe.upc.vacunapp.utils.Constantes.Companion.I_DAYMILISECS
import pe.upc.vacunapp.utils.getCurrentDateTime
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class PersonaCampanaService(private val personaCampanaDAO: PersonaCampanaDAO,
                            private val personaDAO: PersonaDAO,
                            private val campanaDAO: CampanaDAO,
                            private val vacunaDAO: VacunaDAO): BasicCrud<PersonaCampana,String> {
    override fun findAll(): List<PersonaCampana> {
        return this.personaCampanaDAO.findAll()
    }

    override fun findById(id: String): PersonaCampana? {
        return this.personaCampanaDAO.findByIdOrNull(id)
    }

    override fun save(t: PersonaCampana): PersonaCampana {
        t.d_fec_crea = getCurrentDateTime()

        var existsPersona = this.personaDAO.existsById(t.s_dni)
        var existsCampana = this.campanaDAO.existsById(t.id_campana)
        t.d_fecha_hora = Date( t.d_fecha_hora.time + I_DAYMILISECS.toLong() )

        if( existsPersona && existsCampana ){
            var thisCampana = campanaDAO.findByIdOrNull(t!!.id_campana)
            //verificamos si esta campaña tiene dosis disponibles

            if(thisCampana!!.d_fec_inicio.before(t.d_fecha_hora) || thisCampana!!.d_fec_inicio.equals(t.d_fecha_hora)){
                if( personaCampanaDAO.findAll().count { personaCampana -> personaCampana.id_campana.equals(t.id_campana) } < thisCampana!!.qt_dosis_disponible ){

                    /*Creamos iteracion de registro y validamos si esta dentro de los días de aplicacion*/
                    //primera dosis
                    var notExistsVacunacionPreviaThisVacuna = true
                    for(vacunacion in personaCampanaDAO.findAll()){
                        val campanaAplicacion=campanaDAO.findByIdOrNull(vacunacion.id_campana)
                        if ( vacunacion.s_dni.equals(t.s_dni) && campanaAplicacion!!.id_vacuna.equals(thisCampana!!.id_vacuna) ){
                            notExistsVacunacionPreviaThisVacuna = false
                        }
                    }
                    /*if( personaCampanaDAO.findAll().any { personaCampana ->
                            personaCampana.s_dni.equals(t.s_dni) &&
                                    !campanaDAO.findByIdOrNull(personaCampana!!.id_campana)!!.id_vacuna
                                        .equals(thisCampana!!.id_vacuna)} ){*/
                    if( notExistsVacunacionPreviaThisVacuna ){
                        t.i_sesion = 1
                        t.d_fecha_hora.hours = 0
                        t.id_persona_campana = StringBuilder("").append((thisCampana!!.id_vacuna).toString()).append("-").append(t.i_sesion.toString()).append("-").append(t.s_dni).toString()
                        return this.personaCampanaDAO.save(t)
                    }
                    //ya tiene dosis previas
                    else{
                        val ultimaVacunacion = personaCampanaDAO.findAll().last { personaCampana ->
                            personaCampana.s_dni.equals(t.s_dni) &&
                                    campanaDAO.findByIdOrNull(personaCampana!!.id_campana)!!.id_vacuna
                                        .equals(thisCampana!!.id_vacuna) }

                        var vacunaAplicada = vacunaDAO.findByIdOrNull(thisCampana!!.id_vacuna)

                        var proxLimFecha = Date( ultimaVacunacion.d_fecha_hora.time + Constantes.I_DAYMILISECS.toLong().times(vacunaAplicada!!.qt_dias.toLong()) )

                        if( t.d_fecha_hora.after(proxLimFecha) || t.d_fecha_hora.equals(proxLimFecha) ){
                            t.i_sesion = ultimaVacunacion.i_sesion+1
                            t.d_fecha_hora.hours = 0
                            t.id_persona_campana = StringBuilder("").append((thisCampana!!.id_vacuna).toString()).append("-").append(t.i_sesion.toString()).append("-").append(t.s_dni).toString()
                            return this.personaCampanaDAO.save(t)
                        }
                        else{
                            return throw EntityNotFoundException("No se puede aplicar la vacuna ${vacunaAplicada.s_nombre} antes de la fecha ${proxLimFecha.formatPrint()}")
                        }

                    }
                } else{
                    return throw EntityNotFoundException("No se puede agregar más vacunas a esta campaña, limite alcanzado (${thisCampana!!.qt_dosis_disponible} aplicaciones).")
                }
            } else{
                return throw EntityNotFoundException("No se puede registrar la vacunación si la fecha de la campaña (${thisCampana.d_fec_inicio.formatPrint()}) es posterior a la fecha de vacunación(${t.d_fecha_hora.formatPrint()}).")
            }

        }
        else if( !existsPersona ){
            return throw EntityNotFoundException("Entity Persona ${t.s_dni} does not exists")
        }
        else {
            return throw EntityNotFoundException("Entity Campana ${t.id_campana} does not exists")
        }

    }

    override fun update(t: PersonaCampana): PersonaCampana {
        throw EntityNotFoundException("${t.id_persona_campana} can't be updated")
    }

    override fun deleteById(id: String): PersonaCampana {

        //Verificamos no existencia de otras aplicaciones posteriores de esta vacuna

        var vacunacionToDelete = personaCampanaDAO.findByIdOrNull(id)
        if(vacunacionToDelete!=null){
            var vacunaciones = findAll().filter { personaCampana ->
                    personaCampana.s_dni.equals(vacunacionToDelete.s_dni) &&
                            campanaDAO.findByIdOrNull(personaCampana!!.id_campana)!!.id_vacuna
                                .equals(campanaDAO.findByIdOrNull(vacunacionToDelete!!.id_campana)!!.id_vacuna) &&
                            !personaCampana.i_sesion.equals(vacunacionToDelete.i_sesion)}

            if( vacunaciones.any{ personaCampana -> personaCampana.i_sesion>vacunacionToDelete.i_sesion } ){
                throw EntityNotFoundException("$id no puede ser eliminado debido a que hay vacunas posteriores a esta")
            }
        }

        return this.findById(id)?.apply {
            this@PersonaCampanaService.personaCampanaDAO.deleteById(this.id_persona_campana)
        } ?: throw EntityNotFoundException("$id does not exists")
    }
}