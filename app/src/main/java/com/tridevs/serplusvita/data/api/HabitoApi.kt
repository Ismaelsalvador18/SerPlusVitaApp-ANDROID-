package com.tridevs.serplusvita.data.api

import com.tridevs.serplusvita.data.models.ApiResponse
import com.tridevs.serplusvita.data.models.HabitoRequest
import com.tridevs.serplusvita.data.models.HabitoResponse
import retrofit2.Response
import retrofit2.http.*

interface HabitoApi {
    @POST("usuarios/{id}/habitos")
    suspend fun crearHabito(
        @Path("id") usuarioId: Long,
        @Body request: HabitoRequest
    ): Response<ApiResponse<HabitoResponse>>

    @GET("usuarios/{id}/habitos")
    suspend fun listarHabitos(
        @Path("id") usuarioId: Long,
        @Query("habilitado") habilitado: Boolean? = true,   // habilitado=true
        @Query("detalles") detalles: Boolean? = true        // detalles=true
    ): Response<ApiResponse<List<HabitoResponse>>>

    @GET("usuarios/{id}/habitos/{habitoId}")
    suspend fun obtenerHabitoPorId(
        @Path("id") usuarioId: Long,
        @Path("habitoId") habitoId: Long
    ): Response<ApiResponse<HabitoResponse>>

    @PATCH("usuarios/{id}/habitos/{habitoId}")
    suspend fun modificarHabito(
        @Path("id") usuarioId: Long,
        @Path("habitoId") habitoId: Long,
        @Body request: HabitoRequest
    ): Response<ApiResponse<HabitoResponse>>

    @DELETE("usuarios/{id}/habitos/{habitoId}")
    suspend fun eliminarHabito(
        @Path("id") usuarioId: Long,
        @Path("habitoId") habitoId: Long
    ): Response<ApiResponse<Unit>>
}