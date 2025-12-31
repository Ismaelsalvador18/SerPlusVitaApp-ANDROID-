package com.tridevs.serplusvita.data.api

import com.tridevs.serplusvita.data.models.ApiResponse
import com.tridevs.serplusvita.data.models.HistorialHabitoActualizadoResponse
import com.tridevs.serplusvita.data.models.HistorialHabitoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface HistorialHabitosApi {

    @GET("usuarios/{id}/historial-habitos")
    suspend fun obtenerHistorialHabitos(
        @Path("id") usuarioId: Long
    ): Response<ApiResponse<List<HistorialHabitoResponse>>>

    @PATCH("usuarios/{id}/habitos/{habitoId}/completar")
    suspend fun completarHabito(
        @Path("id") usuarioId: Long,
        @Path("habitoId") habitoId: Long
    ): Response<ApiResponse<HistorialHabitoActualizadoResponse>>

}


