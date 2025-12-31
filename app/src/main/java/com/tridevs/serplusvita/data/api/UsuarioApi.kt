package com.tridevs.serplusvita.data.api

import com.tridevs.serplusvita.data.models.ApiResponse
import com.tridevs.serplusvita.data.models.Usuario
import com.tridevs.serplusvita.data.models.UsuarioUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UsuarioApi {
    @GET("usuarios/{id}")
    suspend fun obtenerUsuarioPorId(
        @Path("id") usuarioId: Long
    ): Response<ApiResponse<Usuario>>

    @PATCH("usuarios/{id}")
    suspend fun actualizarUsuario(
        @Path("id") usuarioId: Long,
        @Body request: UsuarioUpdateRequest
    ): Response<ApiResponse<Usuario>>

    @PATCH("usuarios/{id}/convertir")
    suspend fun convertirInvitado(
        @Path("id") usuarioId: Long
    ): Response<ApiResponse<Usuario>>

    @DELETE("usuarios/{id}")
    suspend fun eliminarUsuario(
        @Path("id") usuarioId: Long
    ): Response<ApiResponse<Unit>>

}