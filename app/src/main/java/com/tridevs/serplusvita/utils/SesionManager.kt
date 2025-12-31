package com.tridevs.serplusvita.utils

import android.content.Context
import com.tridevs.serplusvita.data.models.Sesion

class SesionManager(context: Context) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun guardar(sesion: Sesion) {
        prefs.edit()
            .putLong("id", sesion.id)
            .putString("token", sesion.token)
            .apply()
    }

    fun obtenerSesion(): Sesion? {
        val id = prefs.getLong("id", -1L)
        val token = prefs.getString("token", null)
        if (id <= 0 || token.isNullOrBlank()) return null
        return Sesion(id, token)
    }

    fun limpiar() {
        prefs.edit().clear().apply()
    }

    /**
     * Devuelve el token de autenticación puro, sin prefijos.
     */
    fun getToken(): String? = prefs.getString("token", null)
}
