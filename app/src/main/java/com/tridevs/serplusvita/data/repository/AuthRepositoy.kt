package com.tridevs.serplusvita.data.repository

import com.tridevs.serplusvita.data.api.AuthService
import com.tridevs.serplusvita.data.models.ApiResponse
import com.tridevs.serplusvita.data.models.LoginRequest
import com.tridevs.serplusvita.data.models.Sesion
import com.tridevs.serplusvita.data.models.UsuarioRegistro
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthService
){

    suspend fun login(request: LoginRequest): ApiResponse<Sesion>? {
        return safeCall { api.login(request) }
    }

    suspend fun registro(request: UsuarioRegistro): ApiResponse<Sesion>? {
        return safeCall { api.registro(request) }
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
