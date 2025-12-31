package com.tridevs.serplusvita.ui.theme

import androidx.compose.ui.graphics.Color

val Background_App = Color(0xFFFFFFFF)
val Background_Box = Color(0xFFFEF7DD)
val Background_Seleccionado = Color(0xFFF2E1A1)
val Contorno_Base = Color(0xFFF5B547)
val Background_Completado = Color(0xFFC6F2B3)
val Contorno_Completado = Color(0xFF7FD959)
val Por_Completar = Color(0xFFF75989)
val Boton_Precaucion = Color(0xFFCC3D20)
val Principal = Color(0xFF000000)
val Secundario = Color(0xFFC5BEBE)

fun getHabitColor(isCompleted: Boolean, isSelected: Boolean): Color {
    return when {
        isCompleted -> Background_Completado
        isSelected -> Background_Seleccionado
        else -> Background_Box
    }
}

fun getHabitBorderColor(isCompleted: Boolean): Color {
    return if (isCompleted) Contorno_Completado else Contorno_Base
}

fun getTextColorForBackground(backgroundColor: Color): Color {
    // Si el fondo es claro, usa negro; si es oscuro, usa blanco
    val luminance = 0.299 * backgroundColor.red +
            0.587 * backgroundColor.green +
            0.114 * backgroundColor.blue

    return if (luminance > 0.5) Principal else Color.White
}