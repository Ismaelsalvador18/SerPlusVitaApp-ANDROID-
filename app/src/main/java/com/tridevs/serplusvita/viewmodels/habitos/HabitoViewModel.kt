package com.tridevs.serplusvita.viewmodels.habitos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tridevs.serplusvita.data.models.HabitoRequest
import com.tridevs.serplusvita.data.models.HabitoResponse
import com.tridevs.serplusvita.data.models.HistorialHabitoResponse
import com.tridevs.serplusvita.data.repository.HabitoRepository
import com.tridevs.serplusvita.data.repository.HistorialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitoViewModel @Inject constructor(
    private val repository: HabitoRepository,
    private val historialRepository: HistorialRepository
) : ViewModel() {

    private val _habitos = MutableStateFlow<List<HabitoResponse>>(emptyList())
    val habitos: StateFlow<List<HabitoResponse>> = _habitos

    private val _historialHabitos = MutableStateFlow<List<HistorialHabitoResponse>>(emptyList())
    val historialHabitos: StateFlow<List<HistorialHabitoResponse>> = _historialHabitos

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun listarHabitos(usuarioId: Long) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.listarHabitos(usuarioId)
            _habitos.value = result?.data ?: emptyList()
            _error.value = if (result == null) "Error al listar hábitos" else null
            _loading.value = false
        }
    }

    fun obtenerHistorial(usuarioId: Long) {
        viewModelScope.launch {
            _loading.value = true
            val result = historialRepository.obtenerHistorial(usuarioId)
            _historialHabitos.value = result?.data ?: emptyList()
            _error.value = if (result == null) "Error al obtener el historial" else null
            _loading.value = false
        }
    }

    fun crearHabito(usuarioId: Long, request: HabitoRequest) {
        viewModelScope.launch {
            _loading.value = true
            val r = repository.crearHabito(usuarioId, request)
            if (r?.data != null) {
                listarHabitos(usuarioId)
                _error.value = null
            } else {
                _error.value = "Error al crear hábito"
            }
            _loading.value = false
        }
    }

    fun editarHabito(usuarioId: Long, habitoId: Long, request: HabitoRequest) {
        viewModelScope.launch {
            _loading.value = true
            val r = repository.modificarHabito(usuarioId, habitoId, request)
            if (r?.data != null) {
                listarHabitos(usuarioId)
                _error.value = null
            } else {
                _error.value = "Error al editar hábito"
            }
            _loading.value = false
        }
    }

    fun deshabilitarHabito(usuarioId: Long, habito: HabitoResponse) {
        viewModelScope.launch {
            _loading.value = true
            val request = HabitoRequest(
                titulo = habito.titulo,
                descripcion = habito.descripcion ?: "",
                recordatorio = habito.recordatorio ?: "",
                habilitado = false
            )
            val r = repository.modificarHabito(usuarioId, habito.id, request)
            if (r?.data != null) {
                listarHabitos(usuarioId)
                _error.value = null
            } else {
                _error.value = "Error al deshabilitar hábito"
            }
            _loading.value = false
        }
    }

    fun habilitarHabito(usuarioId: Long, habito: HabitoResponse) {
        viewModelScope.launch {
            _loading.value = true
            val request = HabitoRequest(
                titulo = habito.titulo,
                descripcion = habito.descripcion ?: "",
                recordatorio = habito.recordatorio ?: "",
                habilitado = true
            )
            val r = repository.modificarHabito(usuarioId, habito.id, request)
            if (r?.data != null) {
                listarHabitos(usuarioId)
                _error.value = null
            } else {
                _error.value = "Error al habilitar hábito"
            }
            _loading.value = false
        }
    }

    fun eliminarHabito(usuarioId: Long, habitoId: Long) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.eliminarHabito(usuarioId, habitoId)
            if (result != null) {
                _habitos.value = _habitos.value.filterNot { it.id == habitoId }
                _error.value = null
            } else {
                _error.value = "Error al eliminar hábito"
            }
            _loading.value = false
        }
    }

    fun completarHabito(usuarioId: Long, habitoId: Long) {
        viewModelScope.launch {
            _loading.value = true
            val r = historialRepository.completarHabito(usuarioId, habitoId)
            if (r != null) {
                listarHabitos(usuarioId)
                _error.value = null
            } else {
                _error.value = "Error al completar hábito"
            }
            _loading.value = false
        }
    }
}