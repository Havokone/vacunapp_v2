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
import pe.upc.vacunapp.service.CampanaService
import pe.upc.vacunapp.service.LocalVacunacionService
import pe.upc.vacunapp.service.VacunaService
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class VacunappApplicationTests_Campana {

	@Autowired
	private lateinit var webApplicationcontext: WebApplicationContext

	private val mockMvc: MockMvc by lazy {
		MockMvcBuilders.webAppContextSetup(webApplicationcontext).
		alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print()).build()
	}

	@Autowired
	private lateinit var mapper: ObjectMapper

	@Autowired
	private lateinit var campanaService: CampanaService

	@Autowired
	private lateinit var localVacunacionService: LocalVacunacionService

	@Autowired
	private lateinit var vacunaService: VacunaService

	private val campanaEndPoint = "/api/campana"

	@Test
	fun a_findAllCampana() {
		val campanasFromService = campanaService.findAll()
		val campanas:List<Campana> = mockMvc.perform(MockMvcRequestBuilders.get(campanaEndPoint))
										.andExpect(status().isOk).bodyTo(mapper)

		assertThat(campanasFromService, Matchers.`is`(Matchers.equalTo(campanas)))
	}

	@Test
	fun b_findByIdCampana(){
		val campanasFromService = campanaService.findAll()
		assert(campanasFromService.isNotEmpty()){
			"Should not be empty"
		}
		val campana = campanasFromService.first()

		mockMvc.perform( MockMvcRequestBuilders.get("${campanaEndPoint}/${campana.id_campana}") ).
				andExpect(status().isOk).
				andExpect(MockMvcResultMatchers.jsonPath("$.id_campana", Matchers.`is`(campana.id_campana)))

	}

	@Test
	fun c_finByIdEmptyCampana(){
		mockMvc.perform( MockMvcRequestBuilders.get( "${campanaEndPoint}/0" ) ).
		andExpect( status().isNoContent ).
		andExpect( MockMvcResultMatchers.jsonPath("$").doesNotExist() )
	}

	@Test
	fun d_saveSuccessfullyCampana(){
		val vacuna = vacunaService.findAll().first()
		val local = localVacunacionService.findAll().first()
		val campana = Campana(s_nombre = "Campana vacunacion test",d_fec_inicio = Date(),id_vacuna = vacuna.id_vacuna,id_local = local.id_local,qt_dosis_disponible = 100)

		val campanaFromApi:Campana = mockMvc.perform( MockMvcRequestBuilders.post(campanaEndPoint).
										content( mapper.writeValueAsBytes(campana) ).
										contentType(MediaType.APPLICATION_JSON_UTF8) ).
										andExpect(status().isCreated).
										bodyTo(mapper)
		assert(campanaService.findAll().contains(campanaFromApi))
	}

	@Test
	fun d2_saveCheckRulesCampana(){
		val vacuna = vacunaService.findAll().first()
		val local = localVacunacionService.findAll().first()

		mockMvc.perform( MockMvcRequestBuilders.post(campanaEndPoint).
				content( mapper.writeValueAsBytes( Campana(s_nombre = "Campana vacunacion test",d_fec_inicio = Date(),id_vacuna = vacuna.id_vacuna,id_local = local.id_local,qt_dosis_disponible = 0) ) ).
				contentType(MediaType.APPLICATION_JSON_UTF8) ).
				andExpect(status().isBadRequest).
				andExpect(MockMvcResultMatchers.jsonPath("$.qt_dosis_disponible").exists())

	}

	@Test
	fun d2_saveCheckKeysCampana(){

		mockMvc.perform( MockMvcRequestBuilders.post(campanaEndPoint).
				content( mapper.writeValueAsBytes( Campana(s_nombre = "Campana vacunacion test",d_fec_inicio = Date(),id_vacuna = 2,id_local = 2,qt_dosis_disponible = 40) ) ).
				contentType(MediaType.APPLICATION_JSON_UTF8) ).
				andExpect(status().isConflict).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))

	}

	@Test
	fun e_saveDuplicateEntityCampana(){
		val campanasFromService = campanaService.findAll()
		assert(campanasFromService.isNotEmpty()){
			"Should not be empty"
		}
		val campana = campanasFromService.first()

		mockMvc.perform( MockMvcRequestBuilders.post(campanaEndPoint).
				body(data = campana, mapper = mapper)).
				andExpect(status().isConflict).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("DuplicateKeyException")))

	}

	@Test
	fun f_updateSuccessfullyCampana(){
		val campanasFromService = campanaService.findAll()
		assert(campanasFromService.isNotEmpty()){
			"Should not be empty"
		}
		val campana = campanasFromService.first().copy(s_nombre = "Campana vacunacion mod")

		val campanaFromApi:Campana = mockMvc.perform( MockMvcRequestBuilders.put(campanaEndPoint).
											body(data = campana, mapper = mapper) ).
											andExpect(status().isOk).
											bodyTo(mapper)
		assertThat(campanaService.findById(campana.id_campana),Matchers.`is`(campanaFromApi))
	}

	@Test
	fun h_updateEntityNotFoundCampana(){
		val campana = Campana(id_campana = 0,s_nombre = "Campana vacunacion test",d_fec_inicio = Date(),id_vacuna = 2,id_local = 2,qt_dosis_disponible = 20)

		mockMvc.perform( MockMvcRequestBuilders.put(campanaEndPoint).
				body(data = campana, mapper = mapper) ).
				andExpect(status().isConflict).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))

	}

	@Test
	fun i_deleteByIdCampana(){
		val campanasFromService = campanaService.findAll()
		assert(campanasFromService.isNotEmpty()){
			"Should not be empty"
		}
		val campana = campanasFromService.last()

		val campanaFromApi:Campana = mockMvc.perform( MockMvcRequestBuilders.delete("$campanaEndPoint/${campana.id_campana}") ).
											andExpect(status().isOk).
											bodyTo(mapper)
		assert(!campanaService.findAll().contains(campanaFromApi))
	}

	@Test
	fun j_deleteByIdEntityNotFoundCampana(){

		mockMvc.perform( MockMvcRequestBuilders.delete("$campanaEndPoint/0") ).
				andExpect(status().isConflict).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))

	}

}
