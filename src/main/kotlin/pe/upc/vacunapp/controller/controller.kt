package pe.upc.vacunapp.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pe.upc.vacunapp.domain.*
import pe.upc.vacunapp.service.*

@RestController
@RequestMapping("/api/persona")
class PersonaController(personaService: PersonaService):BasicController<Persona,String>(personaService)

@RestController
@RequestMapping("/api/vacuna")
class VacunaController(vacunaService: VacunaService):BasicController<Vacuna,Int>(vacunaService)

@RestController
@RequestMapping("/api/local_vacunacion")
class LocalVacunacionController(localVacunacionService: LocalVacunacionService):BasicController<LocalVacunacion,Int>(localVacunacionService)

@RestController
@RequestMapping("/api/campana")
class CampanaController(campanaService: CampanaService):BasicController<Campana,Int>(campanaService)

@RestController
@RequestMapping("/api/campana_notificacion")
class CampanaNotificacionController(campanaNotificacionService: CampanaNotificacionService):BasicController<CampanaNotificacion,Int>(campanaNotificacionService)

@RestController
@RequestMapping("/api/aplicacion_vacuna")//persona_campana
class PersonaCampanaController(personaCampanaService: PersonaCampanaService):BasicController<PersonaCampana,String>(personaCampanaService)

@RestController
@RequestMapping("/api/comunication")//
class ComunicationController(comunicationService: ComunicationService):BasicController<Message,String>(comunicationService)
