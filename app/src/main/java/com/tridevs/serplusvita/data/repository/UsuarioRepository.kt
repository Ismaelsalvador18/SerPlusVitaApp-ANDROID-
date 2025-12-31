package com.tridevs.serplusvita.repository

import com.tridevs.serplusvita.data.api.UsuarioApi
import com.tridevs.serplusvita.data.models.ApiResponse
import com.tridevs.serplusvita.data.models.Usuario
import com.tridevs.serplusvita.data.models.UsuarioUpdateRequest
import retrofit2.Response
import javax.inject.Inject

class UsuarioRepository @Inject constructor(private val api: UsuarioApi) {

    suspend fun obtenerUsuario(id: Long): ApiResponse<Usuario>? {
        return safeCall { api.obtenerUsuarioPorId(id) }
    }

    suspend fun actualizarUsuario(id: Long, request: UsuarioUpdateRequest): ApiResponse<Usuario>? {
        return safeCall { api.actualizarUsuario(id, request) }
    }

    suspend fun convertirInvitado(id: Long): ApiResponse<Usuario>? {
        return safeCall { api.convertirInvitado(id) }
    }

    suspend fun eliminarUsuario(id: Long): ApiResponse<Unit>? {
        return safeCall { api.eliminarUsuario(id) }
    }

    // 🔒 Manejo seguro de llamadas
    private suspend fun <T> safeCall(call: suspend () -> Response<ApiResponse<T>>): ApiResponse<T>? {
        return try {
            val response = call()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
}