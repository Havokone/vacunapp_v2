package com.upc.vacunapp.utils

import java.text.SimpleDateFormat
import java.util.*

fun <E> MutableSet<E>.update(element: E):Boolean{
    return this.remove(element) && this.add(element)
}

fun Date.formatPrint():String{
    return SimpleDateFormat("dd-MM-yyyy").format(this)
}