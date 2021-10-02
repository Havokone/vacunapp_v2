package pe.upc.vacunapp.controller

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import pe.upc.vacunapp.service.FindByDNI

abstract class FindController<T,ID>(private val FindByDNI: FindByDNI<T,ID>) {

    @CrossOrigin
    @GetMapping("/{dni}")
    fun findAll(@PathVariable dni:ID) = FindByDNI.findAll(dni)

}