package pe.upc.vacunapp.controller

import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pe.upc.vacunapp.service.BasicCrud
import pe.upc.vacunapp.service.UserCrud
import javax.validation.Valid

abstract class UserController<T,ID>(private val userCrud: UserCrud<T, ID>) {

    //cada que el controlador reciba una petición http de tipo get va a invocar esta funcion
    @CrossOrigin
    @ApiOperation("Get all entities")
    @GetMapping
    fun findAll() = userCrud.findAll()

    @CrossOrigin
    @GetMapping("/user={user}&&password={password}")
    fun findById(@PathVariable user:ID,@PathVariable password:ID): ResponseEntity<T> {
        val entity = userCrud.findByUserPass(user,password)
        return ResponseEntity.status( if( entity!=null ) HttpStatus.OK else HttpStatus.NO_CONTENT ).
        body(entity)
    }

    @CrossOrigin
    @PostMapping
    fun save(@Valid @RequestBody body: T) = ResponseEntity.status(HttpStatus.CREATED).body(this.userCrud.save(body))

    @CrossOrigin
    @PutMapping
    fun update(@Valid @RequestBody body: T) = this.userCrud.update(body)

}