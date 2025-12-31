package com.tridevs.serplusvita.data.models

import kotlinx.serialization.SerialName

data class PesoRequest(
    val peso: Double
)

data class PesoActualizadoResponse(
    val peso: Double,
    val fecha: String
)

data class PesoResponse(
    val peso: Double,
    val fecha: String
)