package com.tridevs.serplusvita.data.models

import com.google.gson.annotations.SerializedName

/**
 * Modelo para enviar un nuevo registro de peso al backend.
 */
data class PesoRequest(
    val peso: Double
)

/**
 * Modelo que representa la respuesta del backend para un registro de peso.
 * Coincide con lo que la API devuelve: `peso` y `fecha`.
 */
data class PesoResponse(
    val peso: Double,
    val fecha: String
)
