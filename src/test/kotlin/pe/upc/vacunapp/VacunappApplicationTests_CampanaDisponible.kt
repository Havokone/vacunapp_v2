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
import pe.upc.vacunapp.service.CampanaDisponibleService
import pe.upc.vacunapp.service.PersonaService

@RunWith(SpringRunner::class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class VacunappApplicationTests_CampanaDisponible {

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

	private val campanaDisponibleEndPoint = "/api/campana_disponible"

	@Test
	fun a_findByDNICampanaDisponible(){
		val personasFromService = personaService.findAll()
		assert(personasFromService.isNotEmpty()){
			"Should not be empty"
		}
		val persona = personasFromService.first()

		mockMvc.perform( MockMvcRequestBuilders.get("${campanaDisponibleEndPoint}/${persona.s_dni}") ).
				andExpect(status().isOk)

	}

}
