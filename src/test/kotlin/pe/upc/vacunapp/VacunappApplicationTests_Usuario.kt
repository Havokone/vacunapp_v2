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
import pe.upc.vacunapp.domain.Usuario
import pe.upc.vacunapp.service.UsuarioService
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class VacunappApplicationTests_Usuario {

	@Autowired
	private lateinit var webApplicationcontext: WebApplicationContext

	private val mockMvc: MockMvc by lazy {
		MockMvcBuilders.webAppContextSetup(webApplicationcontext).
		alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print()).build()
	}

	@Autowired
	private lateinit var mapper: ObjectMapper

	@Autowired
	private lateinit var usuarioService: UsuarioService

	private val usuarioEndPoint = "/api/usuario"

	@Test
	fun a_findAllUsuario() {
		val usuariosFromService = usuarioService.findAll()
		val usuarios:List<Usuario> = mockMvc.perform(MockMvcRequestBuilders.get(usuarioEndPoint))
										.andExpect(status().isOk).bodyTo(mapper)

		assertThat(usuariosFromService, Matchers.`is`(Matchers.equalTo(usuarios)))
	}

	@Test
	fun b_findByUserPassUsuario(){
		val usuariosFromService = usuarioService.findAll()
		assert(usuariosFromService.isNotEmpty()){
			"Should not be empty"
		}
		val usuario = usuariosFromService.first()
		
		mockMvc.perform( MockMvcRequestBuilders.get("${usuarioEndPoint}/user=${usuario.s_email}&&password=${usuario.s_password}") ).
				andExpect(status().isOk).
				andExpect(MockMvcResultMatchers.jsonPath("$.s_email", Matchers.`is`(usuario.s_email)))

	}

	@Test
	fun c_findByUserPassEmptyUsuario(){
		mockMvc.perform( MockMvcRequestBuilders.get( "${usuarioEndPoint}/user=${UUID.randomUUID().toString()}&&password=${UUID.randomUUID().toString()}" ) ).
		andExpect( status().isConflict ).
		andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))

	}

	@Test
	fun d_saveSuccessfullyUsuario(){

		val usuario = Usuario(s_email = "admin@adminupc.com.pe",s_nombres = "Admin Upc",s_apellidos = "Admin Upc",s_rol = "Administrador",s_password = "12345",b_habilitado = true)

		val usuarioFromApi:Usuario = mockMvc.perform( MockMvcRequestBuilders.post(usuarioEndPoint).
											content( mapper.writeValueAsBytes(usuario) ).
											contentType(MediaType.APPLICATION_JSON_UTF8) ).
											andExpect(status().isCreated).
											bodyTo(mapper)
		assert(usuarioService.findAll().contains(usuarioFromApi))
	}

	@Test
	fun d2_saveCheckRulesUsuario(){

		mockMvc.perform( MockMvcRequestBuilders.post(usuarioEndPoint).
				content( mapper.writeValueAsBytes( Usuario(s_email = "admin@adminupc.com.peadmin@adminupc.com.peadmin@adminupc.com.peadmin@adminupc.com.pe",s_nombres = "Admin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin UpcAdmin Upc",s_apellidos = "Admin Upc",s_rol = "Administrador",s_password = "12345",b_habilitado = true) ) ).
				contentType(MediaType.APPLICATION_JSON_UTF8) ).
				andExpect(status().isBadRequest).
				andExpect(MockMvcResultMatchers.jsonPath("$.s_email").exists()).
				andExpect(MockMvcResultMatchers.jsonPath("$.s_nombres").exists())

	}

	@Test
	fun e_saveDuplicateEntityUsuario(){
		val usuariosFromService = usuarioService.findAll()
		assert(usuariosFromService.isNotEmpty()){
			"Should not be empty"
		}
		val vacuna = usuariosFromService.first()

		mockMvc.perform( MockMvcRequestBuilders.post(usuarioEndPoint).
				body(data = vacuna, mapper = mapper)).
				andExpect(status().isConflict).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("DuplicateKeyException")))

	}

	@Test
	fun f_updateSuccessfullyUsuario(){
		val usuariosFromService = usuarioService.findAll()
		assert(usuariosFromService.isNotEmpty()){
			"Should not be empty"
		}
		val usuario = usuariosFromService.first().copy(s_rol = "Operador")

		val usuarioFromApi:Usuario = mockMvc.perform( MockMvcRequestBuilders.put(usuarioEndPoint).
											body(data = usuario, mapper = mapper) ).
											andExpect(status().isOk).
											bodyTo(mapper)
		assertThat(usuarioService.findByUserPass(usuario.s_email,usuario.s_password),Matchers.`is`(usuarioFromApi))
	}

	@Test
	fun h_updateEntityNotFoundUsuario(){
		val usuario = Usuario(s_email = UUID.randomUUID().toString().substring(1,10),s_nombres = "Admin Upc",s_apellidos = "Admin Upc",s_rol = "Administrador",s_password = "12345",b_habilitado = true)

		mockMvc.perform( MockMvcRequestBuilders.put(usuarioEndPoint).
				body(data = usuario, mapper = mapper) ).
				andExpect(status().isConflict).
				andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))

	}

}
