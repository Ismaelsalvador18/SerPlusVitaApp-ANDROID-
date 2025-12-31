package com.tridevs.serplusvita.data.models

import kotlinx.serialization.SerialName

// Response al listar historial de hábitos (últimos 30 días)
data class HistorialHabitoResponse(
    val titulo: String,
    val fecha: String,
    val completado: Boolean
)

// Response al actualizar un hábito como completado
data class HistorialHabitoActualizadoResponse(
    @SerialName("habito_id")
    val habitoId: Long,
    val fecha: String,
    val completado: Boolean
)