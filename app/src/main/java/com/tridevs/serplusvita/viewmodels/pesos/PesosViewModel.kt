package com.tridevs.serplusvita.viewmodels.pesos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tridevs.serplusvita.data.models.PesoRequest
import com.tridevs.serplusvita.data.models.PesoResponse
import com.tridevs.serplusvita.data.repository.PesosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PesosViewModel(
    private val repository: PesosRepository
) : ViewModel() {

    private val _pesos = MutableStateFlow<List<PesoResponse>>(emptyList())
    val pesos: StateFlow<List<PesoResponse>> = _pesos

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun listarPesos(usuarioId: Long, dias: Int) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.listarPesos(usuarioId, dias)
            if (result != null) {
                _pesos.value = result.data ?: emptyList()
                _error.value = null
            } else {
                _error.value = "Error al listar pesos"
            }
            _loading.value = false
        }
    }

    fun crearPeso(usuarioId: Long, request: PesoRequest, dias: Int) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.crearPeso(usuarioId, request)
            if (result != null) {
                listarPesos(usuarioId, dias)
                _error.value = null
            } else {
                _error.value = "Error al registrar peso"
            }
            _loading.value = false
        }
    }
}