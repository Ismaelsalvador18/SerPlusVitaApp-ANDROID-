package com.tridevs.serplusvita.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

//Usuario.kt
data class Sesion(
    val id: Long,
    val token: String
)

data class Usuario(
    val id: Long,
    val correo: String,
    val nombre: String?,
    val altura: Int?,
    val peso: Double?,
    val fechaNacimiento: String?,
    val invitado : Boolean?
)

data class MedidasUsuario(
    val nombre: String?,
    val altura: Int?,
    val peso: Double?,
    @SerialName("fecha_nacimiento")
    val fechaNacimiento: String?
)

data class UsuarioUpdateRequest(
    val nombre: String? = null,
    val altura: Int? = null,
    val peso: Double? = null,
    @SerialName("fecha_nacimiento")
    val fechaNacimiento: String? = null
)

data class UsuarioRegistro(
    @SerializedName("correo") val correo: String?,          // null solo si invitado
    @SerializedName("contrasena") val contrasena: String?,  // null solo si invitado
    @SerializedName("nombre") val nombre: String,
    @SerializedName("altura") val altura: Int,
    @SerializedName("peso") val peso: Double,
    @SerializedName("fecha_nacimiento") val fechaNacimiento: String,
    @SerializedName("invitado") val invitado: Boolean
)

data class LoginRequest(
    val correo: String,
    val contrasena: String
)
