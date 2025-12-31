package com.tridevs.serplusvita.data.repository

import com.tridevs.serplusvita.data.api.HistorialHabitosApi
import com.tridevs.serplusvita.data.models.ApiResponse
import com.tridevs.serplusvita.data.models.HistorialHabitoActualizadoResponse
import com.tridevs.serplusvita.data.models.HistorialHabitoResponse
import retrofit2.Response
import javax.inject.Inject

class HistorialRepository @Inject constructor(private val api: HistorialHabitosApi) {

    suspend fun obtenerHistorial(usuarioId: Long): ApiResponse<List<HistorialHabitoResponse>>? {
        return safeCall { api.obtenerHistorialHabitos(usuarioId) }
    }

    suspend fun completarHabito(usuarioId: Long, habitoId: Long): ApiResponse<HistorialHabitoActualizadoResponse>? {
        return try {
            val resp = api.completarHabito(usuarioId, habitoId)
            if (resp.isSuccessful) resp.body() else null
        } catch (_: Exception) { null }
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