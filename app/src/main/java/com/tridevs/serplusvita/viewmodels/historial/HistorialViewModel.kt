package com.tridevs.serplusvita.viewmodels.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tridevs.serplusvita.data.models.HistorialHabitoResponse
import com.tridevs.serplusvita.data.repository.HistorialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistorialViewModel @Inject constructor(
    private val repository: HistorialRepository
) : ViewModel() {

    private val _historial = MutableStateFlow<List<HistorialHabitoResponse>>(emptyList())
    val historial: StateFlow<List<HistorialHabitoResponse>> = _historial

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarHistorial(usuarioId: Long) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.obtenerHistorial(usuarioId)
            if (result != null) {
                _historial.value = result.data ?: emptyList()
                _error.value = null
            } else {
                _error.value = "Error al cargar historial"
            }
            _loading.value = false
        }
    }

    fun completarHabito(usuarioId: Long, habitoId: Long) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.completarHabito(usuarioId, habitoId)
            if (result != null) {
                val actualizado = result.data
                if (actualizado != null) {
                    // 🔹 Usamos habitoId para identificar qué hábito se completó
                    _historial.value = _historial.value.map {
                        if (it.titulo == actualizado.habitoId.toString() && it.fecha == actualizado.fecha) {
                            it.copy(completado = actualizado.completado)
                        } else it
                    }
                }
                _error.value = null
            } else {
                _error.value = "Error al completar hábito"
            }
            _loading.value = false
        }
    }
}