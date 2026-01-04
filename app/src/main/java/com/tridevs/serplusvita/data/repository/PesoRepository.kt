package com.tridevs.serplusvita.data.repository

import com.tridevs.serplusvita.data.api.PesoApi
import com.tridevs.serplusvita.data.models.ApiResponse
import com.tridevs.serplusvita.data.models.PesoRequest
import com.tridevs.serplusvita.data.models.PesoResponse
import retrofit2.Response
import javax.inject.Inject

class PesoRepository @Inject constructor(private val api: PesoApi) {

    suspend fun obtenerHistorialPeso(usuarioId: Long, dias: Int): ApiResponse<List<PesoResponse>>? {
        return safeCall { api.obtenerHistorialPeso(usuarioId, dias) }
    }

    suspend fun registrarPeso(usuarioId: Long, request: PesoRequest): ApiResponse<PesoResponse>? {
        return safeCall { api.registrarPeso(usuarioId, request) }
    }

    private suspend fun <T> safeCall(call: suspend () -> Response<ApiResponse<T>>): ApiResponse<T>? {
        return try {
            val response = call()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
}
