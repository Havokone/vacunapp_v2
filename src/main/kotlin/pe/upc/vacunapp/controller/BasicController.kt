package pe.upc.vacunapp.controller

import pe.upc.vacunapp.service.BasicCrud
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

abstract class BasicController<T,ID>(private val basicCrud: BasicCrud<T, ID>) {

    //cada que el controlador reciba una petición http de tipo get va a invocar esta funcion
    @ApiOperation("Get all entities")
    @GetMapping
    fun findAll() = basicCrud.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id:ID):ResponseEntity<T> {
        val entity = basicCrud.findById(id)
        return ResponseEntity.status( if( entity!=null ) HttpStatus.OK else HttpStatus.NO_CONTENT ).
                                body(entity)
    }

    @PostMapping
    fun save(@Valid @RequestBody body: T) = ResponseEntity.status(HttpStatus.CREATED).body(this.basicCrud.save(body))

    @PutMapping
    fun update(@Valid @RequestBody body: T) = this.basicCrud.update(body)

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id:ID) = this.basicCrud.deleteById(id)
}