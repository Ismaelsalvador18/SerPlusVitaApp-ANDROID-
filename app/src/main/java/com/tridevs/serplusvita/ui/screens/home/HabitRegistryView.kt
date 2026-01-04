package com.tridevs.serplusvita.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tridevs.serplusvita.data.models.HabitoResponse
import com.tridevs.serplusvita.ui.theme.*
import com.tridevs.serplusvita.utils.SesionManager
import com.tridevs.serplusvita.viewmodels.habitos.HabitoViewModel

@Composable
fun HabitRegistryView(viewModel: HabitoViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    val sesionManager = SesionManager(context)
    val usuarioId = sesionManager.obtenerSesion()?.id

    val habitos by viewModel.habitos.collectAsState()
    val loading by viewModel.loading.collectAsState()
    var habitoAEliminar by remember { mutableStateOf<HabitoResponse?>(null) }

    LaunchedEffect(usuarioId) {
        usuarioId?.let { viewModel.listarHabitos(it) }
    }

    if (habitoAEliminar != null) {
        AlertDialog(
            onDismissRequest = { habitoAEliminar = null },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que quieres eliminar el hábito '${habitoAEliminar?.titulo}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        usuarioId?.let { viewModel.eliminarHabito(it, habitoAEliminar!!.id) }
                        habitoAEliminar = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { habitoAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background_App)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // TÍTULO
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Registro de Habitos",
                color = Principal,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Principal, thickness = 1.dp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // LISTA DE TARJETAS
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            habitos.forEach { habito ->
                TarjetaHabitoEditable(
                    habito = habito,
                    onHabilitar = {
                        usuarioId?.let { uid -> viewModel.habilitarHabito(uid, habito) }
                    },
                    onDeshabilitar = {
                        usuarioId?.let { uid -> viewModel.deshabilitarHabito(uid, habito) }
                    },
                    onEliminar = { habitoAEliminar = habito }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // BOTÓN INFERIOR
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .border(1.dp, Principal, RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Background_Box),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Volver",
                    color = Principal,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TarjetaHabitoEditable(
    habito: HabitoResponse,
    onHabilitar: () -> Unit,
    onDeshabilitar: () -> Unit,
    onEliminar: () -> Unit
) {
    val colorFondo = if (habito.habilitado) Background_Box else Secundario.copy(alpha = 0.5f)
    val colorTexto = if (habito.habilitado) Principal else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .border(1.dp, Contorno_Base, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = habito.titulo,
                color = colorTexto,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (habito.habilitado) {
                    IconButton(onClick = onDeshabilitar) {
                        Icon(
                            imageVector = Icons.Default.VisibilityOff,
                            contentDescription = "Deshabilitar",
                            tint = colorTexto,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = onEliminar) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Borrar",
                            tint = colorTexto,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    IconButton(onClick = onHabilitar) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "Habilitar",
                            tint = colorTexto,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
