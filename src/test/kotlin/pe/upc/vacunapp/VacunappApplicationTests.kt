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
import pe.upc.vacunapp.domain.Vacuna
import pe.upc.vacunapp.service.VacunaService
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class VacunappApplicationTests {

	@Autowired
	private lateinit var webApplicationcontext: WebApplicationContext

	private val mockMvc: MockMvc by lazy {
		MockMvcBuilders.webAppContextSetup(webApplicationcontext).
		alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print()).build()
	}

	@Autowired
	private lateinit var mapper: ObjectMapper

	@Autowired
	private lateinit var vacunaService: VacunaService

	private val vacunaEndPoint = "/api/vacuna"

	@Test
	fun a_findAllVacuna() {
		val vacunasFromService = vacunaService.findAll()
		val vacunas:List<Vacuna> = mockMvc.perform(MockMvcRequestBuilders.get(vacunaEndPoint))
										.andExpect(status().isOk).bodyTo(mapper)

		assertThat(vacunasFromService, Matchers.`is`(Matchers.equalTo(vacunas)))
	}

	@Test
	fun b_findById(){
		val vacunasFromService = vacunaService.findAll()
		assert(vacunasFromService.isNotEmpty()){
			"Should not be empty"
		}
		val vacuna = vacunasFromService.first()

		mockMvc.perform( MockMvcRequestBuilders.get("${vacunaEndPoint}/${vacuna.id_vacuna}") ).
		andExpect(status().isOk).
		andExpect(MockMvcResultMatchers.jsonPath("$.id_vacuna", Matchers.`is`(vacuna.id_vacuna)))

	}

	@Test
	fun c_finByIdEmpty(){
		mockMvc.perform( MockMvcRequestBuilders.get( "${vacunaEndPoint}/0" ) ).
		andExpect( status().isNoContent ).
		andExpect( MockMvcResultMatchers.jsonPath("$").doesNotExist() )
	}

	@Test
	fun d_saveSuccessfully(){

		val vacuna = Vacuna(s_nombre = "P42B",s_fabricante = "Pfizer",qt_dosis = 2,qt_dias = 25)

		val vacunaFromApi:Vacuna = mockMvc.perform( MockMvcRequestBuilders.post(vacunaEndPoint).
										content( mapper.writeValueAsBytes(vacuna) ).
										contentType(MediaType.APPLICATION_JSON_UTF8) ).
										andExpect(status().isCreated).
										bodyTo(mapper)
		assert(vacunaService.findAll().contains(vacunaFromApi))
	}

	@Test
	fun d2_saveCheckRules(){

		mockMvc.perform( MockMvcRequestBuilders.post(vacunaEndPoint).
				content( mapper.writeValueAsBytes(Vacuna(s_nombre = "P42C",s_fabricante = "",qt_dosis = 0,qt_dias = 30)) ).
				contentType(MediaType.APPLICATION_JSON_UTF8) ).
				andExpect(status().isBadRequest).
				andExpect(MockMvcResultMatchers.jsonPath("$.s_fabricante").exists()).
				andExpect(MockMvcResultMatchers.jsonPath("$.qt_dosis").exists())

	}

	@Test
	fun e_saveDuplicateEntity(){
		val vacunasFromService = vacunaService.findAll()
		assert(vacunasFromService.isNotEmpty()){
			"Should not be empty"
		}
		val vacuna = vacunasFromService.first()

		mockMvc.perform( MockMvcRequestBuilders.post(vacunaEndPoint).
				body(data = vacuna, mapper = mapper)).
				andExpect(status().isConflict).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("DuplicateKeyException")))

	}

	@Test
	fun f_updateSuccessfully(){
		val vacunasFromService = vacunaService.findAll()
		assert(vacunasFromService.isNotEmpty()){
			"Should not be empty"
		}
		val vacuna = vacunasFromService.first().copy(qt_dias = 40)

		val vacunaFromApi:Vacuna = mockMvc.perform( MockMvcRequestBuilders.put(vacunaEndPoint).
											body(data = vacuna, mapper = mapper) ).
											andExpect(status().isOk).
											bodyTo(mapper)
		assertThat(vacunaService.findById(vacuna.id_vacuna),Matchers.`is`(vacunaFromApi))
	}

	@Test
	fun h_updateEntityNotFound(){
		val vacuna = Vacuna(id_vacuna = 1,s_nombre = "P42C",s_fabricante = "Sinopharm",qt_dosis = 1,qt_dias = 30)

		mockMvc.perform( MockMvcRequestBuilders.put(vacunaEndPoint).
				body(data = vacuna, mapper = mapper) ).
				andExpect(status().isConflict).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))

	}

	@Test
	fun i_deleteById(){
		val vacunasFromService = vacunaService.findAll()
		assert(vacunasFromService.isNotEmpty()){
			"Should not be empty"
		}
		val vacuna = vacunasFromService.last()

		val vacunaFromApi:Vacuna = mockMvc.perform( MockMvcRequestBuilders.delete("$vacunaEndPoint/${vacuna.id_vacuna}") ).
											andExpect(status().isOk).
											bodyTo(mapper)
		assert(!vacunaService.findAll().contains(vacunaFromApi))
	}

	@Test
	fun j_deleteByIdEntityNotFound(){

		mockMvc.perform( MockMvcRequestBuilders.delete("$vacunaEndPoint/0") ).
				andExpect(status().isConflict).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))

	}

}
