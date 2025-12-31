package com.tridevs.serplusvita.data.models

data class HabitoRequest(
    val titulo: String,
    val descripcion: String,
    val recordatorio: String,
    val habilitado: Boolean
)

data class HabitoResponse(
    val id: Long,
    val titulo: String,
    val descripcion: String?,
    val recordatorio: String?,
    val habilitado: Boolean,
    val completado: Boolean // 🔹 nuevo campo que viene del backend
)

data class Habito(
    val id: Long,
    val titulo: String,
    val descripcion: String?,
    val recordatorio: String?,
    val habilitado: Boolean,
    val completado: Boolean
)