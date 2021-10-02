package pe.upc.vacunapp.controller

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pe.upc.vacunapp.domain.*
import pe.upc.vacunapp.service.*

@CrossOrigin
@RestController
@RequestMapping("/api/vacuna")
class VacunaController(vacunaService: VacunaService):BasicController<Vacuna,Int>(vacunaService)

@CrossOrigin
@RestController
@RequestMapping("/api/persona")
class PersonaController(personaService: PersonaService):BasicController<Persona,String>(personaService)

@CrossOrigin
@RestController
@RequestMapping("/api/local_vacunacion")
class LocalVacunacionController(localVacunacionService: LocalVacunacionService):BasicController<LocalVacunacion,Int>(localVacunacionService)

@CrossOrigin
@RestController
@RequestMapping("/api/campana")
class CampanaController(campanaService: CampanaService):BasicController<Campana,Int>(campanaService)
/*
@CrossOrigin
@RestController
@RequestMapping("/api/campana_notificacion")
class CampanaNotificacionController(campanaNotificacionService: CampanaNotificacionService):BasicController<CampanaNotificacion,Int>(campanaNotificacionService)
*/
@CrossOrigin
@RestController
@RequestMapping("/api/aplicacion_vacuna")//persona_campana
class PersonaCampanaController(personaCampanaService: PersonaCampanaService):BasicController<PersonaCampana,String>(personaCampanaService)

@CrossOrigin
@RestController
@RequestMapping("/api/usuario")//usuario
class UsuarioController(usuarioService: UsuarioService):UserController<Usuario,String>(usuarioService)

@CrossOrigin
@RestController
@RequestMapping("/api/campana_disponible")//campana_disponible
class CampanaDisponibleController(campanaDisponibleService: CampanaDisponibleService):FindController<Campana,String>(campanaDisponibleService)

@CrossOrigin
@RestController
@RequestMapping("/api/proxima_aplicacion")//proxima_aplicacion
class ConsultaVacunacionController(consultaVacunacionService: ConsultaVacunacionService):FindController<VacunacionPorRecibir,String>(consultaVacunacionService)

@CrossOrigin
@RestController
@RequestMapping("/api/padron_vacunacion")//padron_vacunacion
class PadronVacunacionController(padronVacunacionService: PadronVacunacionService):FindController<PadronVacunacion,String>(padronVacunacionService)
