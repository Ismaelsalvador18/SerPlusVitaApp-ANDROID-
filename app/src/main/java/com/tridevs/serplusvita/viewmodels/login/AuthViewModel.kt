package com.tridevs.serplusvita.viewmodels.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tridevs.serplusvita.data.models.LoginRequest
import com.tridevs.serplusvita.data.models.Sesion
import com.tridevs.serplusvita.data.models.UsuarioRegistro
import com.tridevs.serplusvita.data.repository.AuthRepository
import com.tridevs.serplusvita.utils.SesionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val sesionManager: SesionManager
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _sesion = MutableStateFlow<Sesion?>(sesionManager.obtenerSesion())
    val sesion: StateFlow<Sesion?> = _sesion

    fun login(correo: String, contrasena: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.login(LoginRequest(correo, contrasena))
            result?.data?.let { sesion ->
                if (sesion.id > 0 && !sesion.token.isNullOrBlank()) {
                    // 🔹 Guardar sesión en memoria y SharedPreferences
                    _sesion.value = sesion
                    sesionManager.guardar(sesion)

                    // 🔹 El interceptor ya usará sesionManager.bearer() en cada request
                    _error.value = null
                } else {
                    _error.value = "Sesión inválida"
                }
            } ?: run {
                _error.value = "Error al iniciar sesión"
            }
            _loading.value = false
        }
    }

    fun registro(request: UsuarioRegistro) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.registro(request)
            result?.data?.let { sesion ->
                if (sesion.id > 0 && !sesion.token.isNullOrBlank()) {
                    _sesion.value = sesion
                    sesionManager.guardar(sesion)
                    _error.value = null
                } else {
                    _error.value = "Sesión inválida"
                }
            } ?: run {
                _error.value = "Error al registrar usuario"
            }
            _loading.value = false
        }
    }

    fun logout() {
        sesionManager.limpiar()
        _sesion.value = null
    }
}