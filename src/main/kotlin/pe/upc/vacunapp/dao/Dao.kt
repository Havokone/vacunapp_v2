package pe.upc.vacunapp.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pe.upc.vacunapp.domain.*

@Repository
interface VacunaDAO: JpaRepository<Vacuna, Int>

@Repository
interface LocalVacunacionDAO: JpaRepository<LocalVacunacion, Int>

@Repository
interface PersonaDAO: JpaRepository<Persona, String>

@Repository
interface CampanaDAO: JpaRepository<Campana, Int>

@Repository
interface CampanaNotificacionDAO: JpaRepository<CampanaNotificacion, Int>

@Repository
interface PersonaCampanaDAO: JpaRepository<PersonaCampana, String>
