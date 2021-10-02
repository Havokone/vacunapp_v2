package pe.upc.vacunapp.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pe.upc.vacunapp.dao.CampanaDAO
import pe.upc.vacunapp.dao.PersonaCampanaDAO
import pe.upc.vacunapp.dao.PersonaDAO
import pe.upc.vacunapp.dao.VacunaDAO
import pe.upc.vacunapp.domain.VacunacionPorRecibir
import pe.upc.vacunapp.utils.Constantes.Companion.I_DAYMILISECS

import java.util.*

@Service
class ConsultaVacunacionService(private val personaDAO: PersonaDAO,
                                private val campanaDAO: CampanaDAO,
                                private val vacunaDAO: VacunaDAO,
                                private val personaCampanaDAO: PersonaCampanaDAO):FindByDNI<VacunacionPorRecibir,String> {
    override fun findAll(dni: String): List<VacunacionPorRecibir> {

        var proximasVacunas= mutableListOf<VacunacionPorRecibir>()

        if ( dni != null && personaDAO.findAll().any { persona -> persona.s_dni.equals(dni) } ){

            val vacunaciones = personaCampanaDAO.findAll()

            if( vacunaciones.any { personaCampana -> personaCampana.s_dni.equals(dni) } ){
                val qtVacunaciones = vacunaciones.count { personaCampana -> personaCampana.s_dni.equals(dni) }
                val ultimaVacunacion = vacunaciones.last { personaCampana -> personaCampana.s_dni.equals(dni) }
                val ultimaCampana = campanaDAO.findByIdOrNull(ultimaVacunacion.id_campana)
                val vacunaAplicada = vacunaDAO.findByIdOrNull( ultimaCampana?.id_vacuna )

                var sgteFecha = ultimaVacunacion.d_fecha_hora

                var totalIteraciones = vacunaAplicada!!.qt_dosis!!-qtVacunaciones
                var nro_aplicacion:Int = qtVacunaciones+1
                var qtDias =vacunaAplicada!!.qt_dias

                for(i in 1..totalIteraciones ){

                    sgteFecha = Date( sgteFecha.time + I_DAYMILISECS.toLong().times(qtDias.toLong()) )

                    var sgteAplicacion = VacunacionPorRecibir(s_nombre = vacunaAplicada.s_nombre,
                                                              s_fabricante = vacunaAplicada.s_fabricante,
                                                              d_fecha_proxima = sgteFecha,
                                                              qt_nro_dosis = nro_aplicacion)
                    proximasVacunas.add(sgteAplicacion)
                    nro_aplicacion++
                }
            }
        }
        return proximasVacunas
    }
}