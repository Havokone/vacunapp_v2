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
import pe.upc.vacunapp.domain.Persona
import pe.upc.vacunapp.service.PersonaService
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class VacunappApplicationTests_Persona {

	@Autowired
	private lateinit var webApplicationcontext: WebApplicationContext

	private val mockMvc: MockMvc by lazy {
		MockMvcBuilders.webAppContextSetup(webApplicationcontext).
		alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print()).build()
	}

	@Autowired
	private lateinit var mapper: ObjectMapper

	@Autowired
	private lateinit var personaService: PersonaService

	private val personaEndPoint = "/api/persona"

	@Test
	fun a_findAllPersona() {
		val personasFromService = personaService.findAll()
		val personas:List<Persona> = mockMvc.perform(MockMvcRequestBuilders.get(personaEndPoint))
										.andExpect(status().isOk).bodyTo(mapper)

		assertThat(personasFromService, Matchers.`is`(Matchers.equalTo(personas)))
	}

	@Test
	fun b_findByIdPersona(){
		val personasFromService = personaService.findAll()
		assert(personasFromService.isNotEmpty()){
			"Should not be empty"
		}
		val persona = personasFromService.first()

		mockMvc.perform( MockMvcRequestBuilders.get("${personaEndPoint}/${persona.s_dni}") ).
				andExpect(status().isOk).
				andExpect(MockMvcResultMatchers.jsonPath("$.s_dni", Matchers.`is`(persona.s_dni)))

	}

	@Test
	fun c_finByIdEmptyPersona(){
		mockMvc.perform( MockMvcRequestBuilders.get( "${personaEndPoint}/${UUID.randomUUID().toString()}" ) ).
				andExpect( status().isConflict ).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))
	}

	@Test
	fun d_saveSuccessfullyPersona(){

		val persona = Persona(s_dni = "85857474",s_nombres = "Alejandro",s_apellidos = "Sanz",d_fec_nac = Date(),s_num_celular = "999999999")

		val personaFromApi:Persona = mockMvc.perform( MockMvcRequestBuilders.post(personaEndPoint).
										content( mapper.writeValueAsBytes(persona) ).
										contentType(MediaType.APPLICATION_JSON_UTF8) ).
										andExpect(status().isCreated).
										bodyTo(mapper)
		assert(personaService.findAll().contains(personaFromApi))
	}

	@Test
	fun d2_saveCheckRulesPersona(){

		mockMvc.perform( MockMvcRequestBuilders.post(personaEndPoint).
				content( mapper.writeValueAsBytes( Persona(s_dni = "",s_nombres = "Alejandro",s_apellidos = "Sanz",d_fec_nac = Date(),s_num_celular = "999999999") ) ).
				contentType(MediaType.APPLICATION_JSON_UTF8) ).
				andExpect(status().isBadRequest).
				andExpect(MockMvcResultMatchers.jsonPath("$.s_dni").exists())

	}

	@Test
	fun e_saveDuplicateEntityPersona(){
		val personasFromService = personaService.findAll()
		assert(personasFromService.isNotEmpty()){
			"Should not be empty"
		}
		val persona = personasFromService.first()

		mockMvc.perform( MockMvcRequestBuilders.post(personaEndPoint).
				body(data = persona, mapper = mapper)).
				andExpect(status().isConflict).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("DuplicateKeyException")))

	}

	@Test
	fun f_updateEntityNotFoundPersona(){
		val personasFromService = personaService.findAll()
		assert(personasFromService.isNotEmpty()){
			"Should not be empty"
		}
		val persona = personasFromService.first().copy(s_apellidos = "Sanz Perez")

		mockMvc.perform( MockMvcRequestBuilders.put(personaEndPoint).
				body(data = persona, mapper = mapper) ).
				andExpect(status().isConflict).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))
	}

	@Test
	fun g_deleteByIdEntityNotFoundPersona(){
		val personasFromService = personaService.findAll()
		assert(personasFromService.isNotEmpty()){
			"Should not be empty"
		}
		val persona = personasFromService.first().copy(s_apellidos = "Sanz Perez")

		mockMvc.perform( MockMvcRequestBuilders.delete("$personaEndPoint/${persona.s_dni}") ).
				andExpect(status().isConflict).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))

	}

}
