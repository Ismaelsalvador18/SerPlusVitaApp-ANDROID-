package com.tridevs.serplusvita.data.api

import com.tridevs.serplusvita.data.models.ApiResponse
import com.tridevs.serplusvita.data.models.PesoActualizadoResponse
import com.tridevs.serplusvita.data.models.PesoRequest
import com.tridevs.serplusvita.data.models.PesoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PesosApi {
    @POST("usuarios/{id}/pesos")
    suspend fun crearPeso(
        @Path("id") usuarioId: Long,
        @Body request: PesoRequest
    ): Response<ApiResponse<PesoActualizadoResponse>>

    @GET("usuarios/{id}/pesos")
    suspend fun listarPesos(
        @Path("id") usuarioId: Long,
        @Query("dias") dias: Int
    ): Response<ApiResponse<List<PesoResponse>>>
}