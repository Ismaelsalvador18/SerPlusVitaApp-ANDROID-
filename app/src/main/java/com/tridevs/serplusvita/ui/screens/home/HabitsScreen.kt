package com.tridevs.serplusvita.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tridevs.serplusvita.data.models.HabitoResponse
import com.tridevs.serplusvita.ui.components.home.ConfigurarHabitoForm
import com.tridevs.serplusvita.ui.components.home.HabitoCard
import com.tridevs.serplusvita.ui.components.home.NuevoHabitoCard
import com.tridevs.serplusvita.ui.components.home.SinHabitosMessage
import com.tridevs.serplusvita.ui.theme.Background_App
import com.tridevs.serplusvita.ui.theme.Contorno_Base
import com.tridevs.serplusvita.ui.theme.Principal
import com.tridevs.serplusvita.viewmodels.habitos.HabitoViewModel

@Composable
fun HabitsScreen(
    usuarioId: Long,
    viewModel: HabitoViewModel = hiltViewModel()
) {
    val habitos by viewModel.habitos.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    var modoEdicion by remember { mutableStateOf<HabitoResponse?>(null) }
    var creandoHabito by remember { mutableStateOf(false) }

    LaunchedEffect(usuarioId) {
        viewModel.listarHabitos(usuarioId)
    }

    if (creandoHabito || modoEdicion != null) {
        ConfigurarHabitoForm(
            habitoExistente = modoEdicion,
            onGuardar = {
                if (modoEdicion != null) {
                    viewModel.editarHabito(usuarioId, modoEdicion!!.id, it)
                } else {
                    viewModel.crearHabito(usuarioId, it)
                }
                creandoHabito = false
                modoEdicion = null
            },
            onCancelar = {
                creandoHabito = false
                modoEdicion = null
            }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background_App)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Hábitos",
                        color = Principal,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .height(3.dp)
                            .width(120.dp) // Ancho de la línea
                            .background(Contorno_Base)
                    )
                }
                if (habitos.isNotEmpty()) {
                    Button(
                        onClick = { creandoHabito = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Contorno_Base)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Nuevo Hábito", tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Nuevo", color = Color.White)
                    }
                }
            }

            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                when {
                    habitos.isEmpty() && !loading -> {
                        NuevoHabitoCard(onNuevoHabitoClick = { creandoHabito = true })
                        Spacer(Modifier.height(60.dp))
                        SinHabitosMessage()
                    }
                    else -> {
                        habitos.forEach { habito ->
                            if (habito.habilitado) {
                                HabitoCard(
                                    habito = habito,
                                    onCompletar = { viewModel.completarHabito(usuarioId, habito.id) },
                                    onEditar = { modoEdicion = habito },
                                    onDeshabilitar = { viewModel.deshabilitarHabito(usuarioId, habito) },
                                    onEliminar = { viewModel.eliminarHabito(usuarioId, habito.id) }
                                )
                                Spacer(Modifier.height(12.dp))
                            }
                        }
                    }
                }

                if (loading) {
                    Spacer(Modifier.height(12.dp))
                    Text("Cargando...", color = Principal)
                }
                error?.let {
                    Spacer(Modifier.height(12.dp))
                    Text("Error: $it", color = Principal)
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}