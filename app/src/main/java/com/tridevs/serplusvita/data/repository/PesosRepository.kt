package com.tridevs.serplusvita.data.repository

import com.tridevs.serplusvita.data.api.PesosApi
import com.tridevs.serplusvita.data.models.ApiResponse
import com.tridevs.serplusvita.data.models.PesoActualizadoResponse
import com.tridevs.serplusvita.data.models.PesoRequest
import com.tridevs.serplusvita.data.models.PesoResponse
import retrofit2.Response
import javax.inject.Inject

class PesosRepository @Inject constructor(private val api: PesosApi) {

    suspend fun crearPeso(usuarioId: Long, request: PesoRequest): ApiResponse<PesoActualizadoResponse>? {
        return safeCall { api.crearPeso(usuarioId, request) }
    }

    suspend fun listarPesos(usuarioId: Long, dias: Int): ApiResponse<List<PesoResponse>>? {
        return safeCall { api.listarPesos(usuarioId, dias) }
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