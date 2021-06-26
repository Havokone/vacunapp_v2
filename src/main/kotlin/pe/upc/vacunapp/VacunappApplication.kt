package pe.upc.vacunapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VacunappApplication

fun main(args: Array<String>) {
	runApplication<VacunappApplication>(*args)
}
