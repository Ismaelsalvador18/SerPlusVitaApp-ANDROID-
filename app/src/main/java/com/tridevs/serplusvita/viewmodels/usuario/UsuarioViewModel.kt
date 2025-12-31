package com.tridevs.serplusvita.viewmodels.usuario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tridevs.serplusvita.data.models.Usuario
import com.tridevs.serplusvita.data.models.UsuarioUpdateRequest
import com.tridevs.serplusvita.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarUsuario(id: Long) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.obtenerUsuario(id)
            if (result != null) {
                _usuario.value = result.data
                _error.value = null
            } else {
                _error.value = "Error al obtener usuario"
            }
            _loading.value = false
        }
    }

    fun actualizarUsuario(id: Long, request: UsuarioUpdateRequest) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.actualizarUsuario(id, request)
            if (result != null) {
                _usuario.value = result.data
                _error.value = null
            } else {
                _error.value = "Error al actualizar usuario"
            }
            _loading.value = false
        }
    }

    fun convertirInvitado(id: Long) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.convertirInvitado(id)
            if (result != null) {
                _usuario.value = result.data
                _error.value = null
            } else {
                _error.value = "Error al convertir invitado"
            }
            _loading.value = false
        }
    }

    fun eliminarUsuario(id: Long) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.eliminarUsuario(id)
            if (result != null) {
                _usuario.value = null
                _error.value = null
            } else {
                _error.value = "Error al eliminar usuario"
            }
            _loading.value = false
        }
    }
}