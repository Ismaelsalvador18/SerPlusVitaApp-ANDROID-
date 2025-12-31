package com.tridevs.serplusvita.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tridevs.serplusvita.ui.components.home.CrearHabitoCard
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
    var creandoHabito by remember { mutableStateOf(false) }

    LaunchedEffect(usuarioId) {
        viewModel.listarHabitos(usuarioId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background_App)
            .padding(horizontal = 16.dp)
    ) {
        // TÍTULO DE LA SECCIÓN
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
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
                        .fillMaxWidth()
                        .background(Contorno_Base)
                )
            }
        }

        // Contenido principal de la pantalla de hábitos
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            when {
                creandoHabito -> {
                    CrearHabitoCard(
                        onGuardar = { request ->
                            viewModel.crearHabito(usuarioId, request)
                            creandoHabito = false
                        },
                        onCancelar = { creandoHabito = false }
                    )
                }
                habitos.isEmpty() && !loading -> {
                    NuevoHabitoCard(onNuevoHabitoClick = { creandoHabito = true })
                    Spacer(Modifier.height(60.dp))
                    SinHabitosMessage()
                }
                else -> {
                    habitos.forEach { habito ->
                        HabitoCard(
                            habito = habito,
                            onCompletar = { viewModel.completarHabito(usuarioId, habito.id) }
                        )
                        Spacer(Modifier.height(12.dp))
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

            // Espacio al final para asegurar el scroll
            Spacer(Modifier.height(24.dp))
        }
    }
}
