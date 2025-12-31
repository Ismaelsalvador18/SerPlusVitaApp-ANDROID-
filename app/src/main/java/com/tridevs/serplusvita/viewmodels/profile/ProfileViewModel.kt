package com.tridevs.serplusvita.viewmodels.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tridevs.serplusvita.data.models.Usuario
import com.tridevs.serplusvita.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarUsuario(usuarioId: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val result = usuarioRepository.obtenerUsuario(usuarioId)
                if (result?.data != null) {
                    _usuario.value = result.data
                } else {
                    _error.value = "No se pudieron cargar los datos del usuario."
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.message}"
            }
            _loading.value = false
        }
    }
    
    fun eliminarUsuario(usuarioId: Long, onAccountDeleted: () -> Unit) {
        viewModelScope.launch {
             _loading.value = true
            _error.value = null
            try {
                usuarioRepository.eliminarUsuario(usuarioId)
                onAccountDeleted()
            } catch (e: Exception) {
                 _error.value = "Error al eliminar la cuenta: ${e.message}"
            }
            _loading.value = false
        }
    }
}