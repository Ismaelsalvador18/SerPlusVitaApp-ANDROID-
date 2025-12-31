package com.tridevs.serplusvita.utils

import java.time.LocalDate

fun validarFecha(fecha: String): Boolean {
    return try {
        LocalDate.parse(fecha) // lanza excepción si la fecha no existe
        true
    } catch (e: Exception) {
        false
    }
}