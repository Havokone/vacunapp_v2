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
import pe.upc.vacunapp.domain.LocalVacunacion
import pe.upc.vacunapp.service.LocalVacunacionService

@RunWith(SpringRunner::class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class VacunappApplicationTests_Local {

	@Autowired
	private lateinit var webApplicationcontext: WebApplicationContext

	private val mockMvc: MockMvc by lazy {
		MockMvcBuilders.webAppContextSetup(webApplicationcontext).
		alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print()).build()
	}

	@Autowired
	private lateinit var mapper: ObjectMapper

	@Autowired
	private lateinit var localVacunacionService: LocalVacunacionService

	private val localVacunacionEndPoint = "/api/local_vacunacion"

	@Test
	fun a_findAllLocalVacunacion() {
		val localesFromService = localVacunacionService.findAll()
		val locales:List<LocalVacunacion> = mockMvc.perform(MockMvcRequestBuilders.get(localVacunacionEndPoint))
										.andExpect(status().isOk).bodyTo(mapper)

		assertThat(localesFromService, Matchers.`is`(Matchers.equalTo(locales)))
	}

	@Test
	fun b_findByIdLocalVacunacion(){
		val localVacunacionService = localVacunacionService.findAll()
		assert(localVacunacionService.isNotEmpty()){
			"Should not be empty"
		}
		val local = localVacunacionService.first()

		mockMvc.perform( MockMvcRequestBuilders.get("${localVacunacionEndPoint}/${local.id_local}") ).
		andExpect(status().isOk).
		andExpect(MockMvcResultMatchers.jsonPath("$.id_local", Matchers.`is`(local.id_local)))

	}

	@Test
	fun c_finByIdEmptyLocalVacunacion(){
		mockMvc.perform( MockMvcRequestBuilders.get( "${localVacunacionEndPoint}/0" ) ).
		andExpect( status().isNoContent ).
		andExpect( MockMvcResultMatchers.jsonPath("$").doesNotExist() )
	}

	@Test
	fun d_saveSuccessfullyLocalVacunacion(){

		val local = LocalVacunacion(s_nombre = "Colegio XXX",s_direccion = "Av. Siempreviva 123",s_ubigeo_dep = "Ica",s_ubigeo_pro = "Ica",s_ubigeo_dis = "Ica")

		val localFromApi:LocalVacunacion = mockMvc.perform( MockMvcRequestBuilders.post(localVacunacionEndPoint).
										content( mapper.writeValueAsBytes(local) ).
										contentType(MediaType.APPLICATION_JSON_UTF8) ).
										andExpect(status().isCreated).
										bodyTo(mapper)
		assert(localVacunacionService.findAll().contains(localFromApi))
	}

	@Test
	fun d2_saveCheckRulesLocalVacunacion(){

		mockMvc.perform( MockMvcRequestBuilders.post(localVacunacionEndPoint).
				content( mapper.writeValueAsBytes(LocalVacunacion(s_nombre = "",s_direccion = "",s_ubigeo_dep = "Lima",s_ubigeo_pro = "Lima",s_ubigeo_dis = "Lima")) ).
				contentType(MediaType.APPLICATION_JSON_UTF8) ).
				andExpect(status().isBadRequest).
				andExpect(MockMvcResultMatchers.jsonPath("$.s_direccion").exists()).
				andExpect(MockMvcResultMatchers.jsonPath("$.s_nombre").exists())

	}

	@Test
	fun e_saveDuplicateEntityLocalVacunacion(){
		val localesFromService = localVacunacionService.findAll()
		assert(localesFromService.isNotEmpty()){
			"Should not be empty"
		}
		val local = localesFromService.first()

		mockMvc.perform( MockMvcRequestBuilders.post(localVacunacionEndPoint).
				body(data = local, mapper = mapper)).
				andExpect(status().isConflict).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("DuplicateKeyException")))

	}

	@Test
	fun f_updateSuccessfullyLocalVacunacion(){
		val localesFromService = localVacunacionService.findAll()
		assert(localesFromService.isNotEmpty()){
			"Should not be empty"
		}
		val local = localesFromService.first().copy(s_direccion = "Avenida Peru 1282")

		val localFromApi: LocalVacunacion = mockMvc.perform( MockMvcRequestBuilders.put(localVacunacionEndPoint).
											body(data = local, mapper = mapper) ).
											andExpect(status().isOk).
											bodyTo(mapper)
		assertThat(localVacunacionService.findById(local.id_local),Matchers.`is`(localFromApi))
	}

	@Test
	fun h_updateEntityNotFoundLocalVacunacion(){
		val local = LocalVacunacion(id_local = 0,
									s_nombre = "Colegio XXX",
									s_direccion = "Av. Siempreviva 123",
									s_ubigeo_dep = "Ica",
									s_ubigeo_pro = "Ica",
									s_ubigeo_dis = "Ica")

		mockMvc.perform( MockMvcRequestBuilders.put(localVacunacionEndPoint).
												body(data = local, mapper = mapper) ).
												andExpect(status().isConflict).
												andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))

	}

	@Test
	fun i_deleteByIdLocalVacunacion(){
		val localesFromService = localVacunacionService.findAll()
		assert(localesFromService.isNotEmpty()){
			"Should not be empty"
		}
		val local = localesFromService.last()

		val localFromApi: LocalVacunacion = mockMvc.perform( MockMvcRequestBuilders.delete("$localVacunacionEndPoint/${local.id_local}") ).
											andExpect(status().isOk).
											bodyTo(mapper)
		assert(!localVacunacionService.findAll().contains(localFromApi))
	}

	@Test
	fun j_deleteByIdEntityNotFoundLocalVacunacion(){

		mockMvc.perform( MockMvcRequestBuilders.delete("$localVacunacionEndPoint/0") ).
		andExpect(status().isConflict).
		andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))

	}

}
