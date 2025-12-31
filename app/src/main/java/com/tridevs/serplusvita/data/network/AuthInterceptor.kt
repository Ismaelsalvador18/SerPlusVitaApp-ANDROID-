package com.tridevs.serplusvita.data.network

import com.tridevs.serplusvita.utils.SesionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sesionManager: SesionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sesionManager.getToken()
        var request = chain.request()

        // Si tenemos un token, lo añadimos a la cabecera de la petición
        if (token != null) {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        }

        // ✅ FIX: Siempre continuamos con la petición, tenga o no tenga token.
        // El backend se encargará de validar si la ruta requería autenticación.
        return chain.proceed(request)
    }
}
