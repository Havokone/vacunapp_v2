package pe.upc.vacunapp.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pe.upc.vacunapp.dao.*
import pe.upc.vacunapp.domain.PadronVacunacion
import pe.upc.vacunapp.utils.Constantes.Companion.S_EMPTY

@Service
class PadronVacunacionService(private val personaDAO: PersonaDAO,
                              private val campanaDAO: CampanaDAO,
                              private val vacunaDAO: VacunaDAO,
                              private val usuarioDAO: UsuarioDAO,
                              private val personaCampanaDAO: PersonaCampanaDAO):FindByDNI<PadronVacunacion,String> {
    override fun findAll(dni: String): List<PadronVacunacion> {

        var vacunasAplicadas = mutableListOf<PadronVacunacion>()

        if (dni != null && personaDAO.findAll().any { persona -> persona.s_dni.equals(dni) }) {

            val vacunaciones = personaCampanaDAO.findAll().filter { personaCampana -> personaCampana.s_dni.equals(dni) }
            for (vacunacion in vacunaciones) {
                val campana = campanaDAO.findByIdOrNull(vacunacion.id_campana)
                val vacuna = vacunaDAO.findByIdOrNull(campana?.id_vacuna)
                val usuario = usuarioDAO.findByIdOrNull(vacunacion.s_email)
                val persona = personaDAO.findByIdOrNull(vacunacion.s_dni)
                var nombreMedico:String = S_EMPTY
                if(usuario != null){
                    nombreMedico = usuario!!.nombreCompleto()
                }
                val registroVacunacion = PadronVacunacion(s_dni_persona = persona!!.s_dni,
                                                        s_nombre_persona = persona!!.nombreCompleto(),
                                                        s_nombre_vacuna = vacuna!!.s_nombre,
                                                        s_fabricante = vacuna!!.s_fabricante,
                                                        s_campana = campana!!.s_nombre,
                                                        s_medico = nombreMedico,
                                                        d_fecha_aplicacion=vacunacion.d_fecha_hora,
                                                        qt_nro_dosis = vacunacion.i_sesion,
                                                        b_completo = vacunaciones.size==vacuna.qt_dosis)
                vacunasAplicadas.add(registroVacunacion)
            }

        }
        return vacunasAplicadas
    }
}