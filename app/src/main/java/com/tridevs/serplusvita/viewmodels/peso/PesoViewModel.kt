package com.tridevs.serplusvita.viewmodels.peso

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tridevs.serplusvita.data.models.PesoRequest
import com.tridevs.serplusvita.data.models.PesoResponse
import com.tridevs.serplusvita.data.repository.PesoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PesoViewModel @Inject constructor(
    private val repository: PesoRepository
) : ViewModel() {

    private val _historialPeso = MutableStateFlow<List<PesoResponse>>(emptyList())
    val historialPeso: StateFlow<List<PesoResponse>> = _historialPeso

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun obtenerHistorialPeso(usuarioId: Long, dias: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val result = repository.obtenerHistorialPeso(usuarioId, dias)
                if (result?.data != null) {
                    _historialPeso.value = result.data
                } else {
                    _error.value = "No se pudo obtener el historial de peso."
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.message}"
            }
            _loading.value = false
        }
    }

    fun registrarPeso(usuarioId: Long, peso: Double, dias: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val result = repository.registrarPeso(usuarioId, PesoRequest(peso))
                if (result?.data != null) {
                    obtenerHistorialPeso(usuarioId, dias)
                } else {
                    _error.value = "No se pudo registrar el peso."
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.message}"
            }
            _loading.value = false
        }
    }
}
