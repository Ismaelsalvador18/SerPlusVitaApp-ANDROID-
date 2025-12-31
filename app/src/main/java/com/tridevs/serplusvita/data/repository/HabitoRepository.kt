package com.tridevs.serplusvita.data.repository

import com.tridevs.serplusvita.data.api.HabitoApi
import com.tridevs.serplusvita.data.models.ApiResponse
import com.tridevs.serplusvita.data.models.HabitoRequest
import com.tridevs.serplusvita.data.models.HabitoResponse
import retrofit2.Response
import javax.inject.Inject

class HabitoRepository @Inject constructor(private val api: HabitoApi) {

    suspend fun listarHabitos(usuarioId: Long): ApiResponse<List<HabitoResponse>>? {
        //  ✅  FIX: Eliminamos los filtros para obtener TODOS los hábitos 
        return safeCall { api.listarHabitos(usuarioId, null, null) }
    }

    suspend fun crearHabito(usuarioId: Long, request: HabitoRequest): ApiResponse<HabitoResponse>? {
        return safeCall { api.crearHabito(usuarioId, request) }
    }

    suspend fun obtenerHabitoPorId(usuarioId: Long, habitoId: Long): ApiResponse<HabitoResponse>? {
        return safeCall { api.obtenerHabitoPorId(usuarioId, habitoId) }
    }

    suspend fun modificarHabito(usuarioId: Long, habitoId: Long, request: HabitoRequest): ApiResponse<HabitoResponse>? {
        return safeCall { api.modificarHabito(usuarioId, habitoId, request) }
    }

    suspend fun eliminarHabito(usuarioId: Long, habitoId: Long): ApiResponse<Unit>? {
        return safeCall { api.eliminarHabito(usuarioId, habitoId) }
    }

    private suspend fun <T> safeCall(call: suspend () -> Response<ApiResponse<T>>): ApiResponse<T>? {
        return try {
            val resp = call()
            if (resp.isSuccessful) resp.body() else null
        } catch (_: Exception) { null }
    }
}