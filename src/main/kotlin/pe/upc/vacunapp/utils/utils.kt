package pe.upc.vacunapp.utils

import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun getCurrentDateTime(): Date = Calendar.getInstance().time

public class Constantes{
    companion object {
        const val S_EMPTY = ""
        const val I_DAYMILISECS = 1000*24*60*60
    }

}
