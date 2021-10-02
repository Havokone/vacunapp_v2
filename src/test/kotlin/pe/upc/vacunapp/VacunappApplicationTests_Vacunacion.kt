package pe.upc.vacunapp

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import pe.upc.vacunapp.domain.Campana
import pe.upc.vacunapp.domain.PersonaCampana
import pe.upc.vacunapp.service.CampanaService
import pe.upc.vacunapp.service.PersonaCampanaService
import pe.upc.vacunapp.service.PersonaService
import pe.upc.vacunapp.service.UsuarioService
import pe.upc.vacunapp.utils.Constantes.Companion.I_DAYMILISECS
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class VacunappApplicationTests_Vacunacion {

	@Autowired
	private lateinit var webApplicationcontext: WebApplicationContext

	private val mockMvc: MockMvc by lazy {
		MockMvcBuilders.webAppContextSetup(webApplicationcontext).
		alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print()).build()
	}

	@Autowired
	private lateinit var mapper: ObjectMapper

	@Autowired
	private lateinit var personaCampanaService: PersonaCampanaService

	@Autowired
	private lateinit var campanaService: CampanaService

	@Autowired
	private lateinit var usuarioService: UsuarioService

	@Autowired
	private lateinit var personaService: PersonaService

	private val vacunacionEndPoint = "/api/aplicacion_vacuna"

	@Test
	fun a_findAllVacunacion() {
		val vacunacionesFromService = personaCampanaService.findAll()
		val vacunaciones:List<PersonaCampana> = mockMvc.perform(MockMvcRequestBuilders.get(vacunacionEndPoint))
										.andExpect(status().isOk).bodyTo(mapper)

		assertThat(vacunacionesFromService, Matchers.`is`(Matchers.equalTo(vacunaciones)))
	}

	@Test
	fun b_findByIdVacunacion(){
		val vacunacionesFromService = personaCampanaService.findAll()
		assert(vacunacionesFromService.isNotEmpty()){
			"Should not be empty"
		}
		val vacunacion = vacunacionesFromService.first()

		mockMvc.perform( MockMvcRequestBuilders.get("${vacunacionEndPoint}/${vacunacion.id_persona_campana}") ).
				andExpect(status().isOk).
				andExpect(MockMvcResultMatchers.jsonPath("$.id_persona_campana", Matchers.`is`(vacunacion.id_persona_campana)))

	}

	@Test
	fun c_finByIdEmptyVacunacion(){
		mockMvc.perform( MockMvcRequestBuilders.get( "${vacunacionEndPoint}/${UUID.randomUUID().toString()}" ) ).
				andExpect( status().isNoContent ).
				andExpect( MockMvcResultMatchers.jsonPath("$").doesNotExist() )
	}

	@Test
	fun d1_saveSuccessfullyVacunacion(){
		val usuario = usuarioService.findAll().first()
		val persona = personaService.findAll().first()
		val campana = campanaService.findAll().first()

		val vacunacion = PersonaCampana(i_sesion = 0,
										id_campana = campana.id_campana,
										s_email = usuario.s_email,
										s_dni = persona.s_dni,
										d_fecha_hora = Date( campana.d_fec_inicio.time + I_DAYMILISECS.toLong()*(2).toLong() ))

		val vacunacionFromApi:PersonaCampana = mockMvc.perform( MockMvcRequestBuilders.post(vacunacionEndPoint).
														content( mapper.writeValueAsBytes(vacunacion) ).
														contentType(MediaType.APPLICATION_JSON_UTF8) ).
														andExpect(status().isCreated).
														bodyTo(mapper)
		assert(personaCampanaService.findAll().contains(vacunacionFromApi))
	}

	@Test
	fun d2_saveCheckRulesVacunacion(){
		val usuario = usuarioService.findAll().first()
		val persona = personaService.findAll().first()
		val campana = campanaService.findAll().first()

		mockMvc.perform( MockMvcRequestBuilders.post(vacunacionEndPoint).
				content( mapper.writeValueAsBytes( PersonaCampana(id_persona_campana = UUID.randomUUID().toString()+UUID.randomUUID().toString()+UUID.randomUUID().toString(),i_sesion = 0,id_campana = campana.id_campana,s_email = usuario.s_email,s_dni = persona.s_dni,d_fecha_hora = Date()) ) ).
				contentType(MediaType.APPLICATION_JSON_UTF8) ).
				andExpect(status().isBadRequest).
				andExpect(MockMvcResultMatchers.jsonPath("$.id_persona_campana").exists())

	}

	@Test
	fun d2_saveCheckKeysVacunacion(){

		mockMvc.perform( MockMvcRequestBuilders.post(vacunacionEndPoint).
				content( mapper.writeValueAsBytes( PersonaCampana(i_sesion = 0,id_campana = 1,s_email = UUID.randomUUID().toString(),s_dni = UUID.randomUUID().toString().substring(0,8),d_fecha_hora = Date()) ) ).
				contentType(MediaType.APPLICATION_JSON_UTF8) ).
				andExpect(status().isConflict).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))

	}

	@Test
	fun d3_saveCheckFechaInicioCampanaVacunacion(){
		val usuario = usuarioService.findAll().first()
		val persona = personaService.findAll().first()
		val campana = campanaService.findAll().first()

		val vacunacion = PersonaCampana(i_sesion = 0,
										id_campana = campana.id_campana,
										s_email = usuario.s_email,
										s_dni = persona.s_dni,
										d_fecha_hora = Date( campana.d_fec_inicio.time - I_DAYMILISECS.toLong()*(10).toLong() ) )

		mockMvc.perform( MockMvcRequestBuilders.post(vacunacionEndPoint).
				content( mapper.writeValueAsBytes( vacunacion ) ).
				contentType(MediaType.APPLICATION_JSON_UTF8) ).
				andExpect(status().isConflict).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))

	}

	@Test
	fun d4_saveCheckFechaSiguienteVacunacion(){
		val usuario = usuarioService.findAll().first()
		val persona = personaService.findAll().first()
		val campana = campanaService.findAll().first()

		val vacunacion = PersonaCampana(i_sesion = 0,id_campana = campana.id_campana,s_email = usuario.s_email,s_dni = persona.s_dni,d_fecha_hora = Date())

		mockMvc.perform( MockMvcRequestBuilders.post(vacunacionEndPoint).
				content( mapper.writeValueAsBytes( vacunacion ) ).
				contentType(MediaType.APPLICATION_JSON_UTF8) ).
				andExpect(status().isConflict).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))

	}

	@Test
	fun d5_saveCheckDisponibilidadVacunacion(){
		val usuario = usuarioService.findAll().first()
		val persona = personaService.findAll().last()
		val campana = campanaService.findAll().first()

		val vacunacion = PersonaCampana(i_sesion = 0,id_campana = campana.id_campana,s_email = usuario.s_email,s_dni = persona.s_dni,d_fecha_hora = Date())

		mockMvc.perform( MockMvcRequestBuilders.post(vacunacionEndPoint).
				content( mapper.writeValueAsBytes( vacunacion ) ).
				contentType(MediaType.APPLICATION_JSON_UTF8) ).
				andExpect(status().isConflict).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))

	}


	@Test
	fun i_deleteByIdVacunacion(){
		val vacunacionesFromService = personaCampanaService.findAll()
		assert(vacunacionesFromService.isNotEmpty()){
			"Should not be empty"
		}
		val vacunacion = vacunacionesFromService.last()

		val vacunacionFromApi:PersonaCampana = mockMvc.perform( MockMvcRequestBuilders.delete("$vacunacionEndPoint/${vacunacion.id_persona_campana}") ).
											andExpect(status().isOk).
											bodyTo(mapper)
		assert(!personaCampanaService.findAll().contains(vacunacionFromApi))
	}

	@Test
	fun j_deleteByIdEntityNotFoundVacunacion(){

		mockMvc.perform( MockMvcRequestBuilders.delete("$vacunacionEndPoint/${UUID.randomUUID().toString()}") ).
				andExpect(status().isConflict).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))

	}

}
