package com.tridevs.serplusvita.data.api

import com.tridevs.serplusvita.data.models.ApiResponse
import com.tridevs.serplusvita.data.models.LoginRequest
import com.tridevs.serplusvita.data.models.Sesion
import com.tridevs.serplusvita.data.models.UsuarioRegistro
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<Sesion>>

    @POST("auth/registro")
    suspend fun registro(@Body request: UsuarioRegistro): Response<ApiResponse<Sesion>>
}