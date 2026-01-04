package com.tridevs.serplusvita.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tridevs.serplusvita.ui.theme.*
import com.tridevs.serplusvita.utils.SesionManager
import com.tridevs.serplusvita.viewmodels.habitos.HabitoViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

@Composable
fun HabitHistoryView(viewModel: HabitoViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    val sesionManager = SesionManager(context)
    val usuarioId = sesionManager.obtenerSesion()?.id

    val historial by viewModel.historialHabitos.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(usuarioId) {
        usuarioId?.let { viewModel.obtenerHistorial(it) }
    }

    val groupedHistorial = historial.groupBy { it.fecha.substringBefore('T') }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background_App)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = "Historial de Habitos",
                color = Principal,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(
                color = Principal,
                thickness = 2.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            if (groupedHistorial.isEmpty()) {
                Text("No hay historial de hábitos para mostrar.", color = Principal, modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                groupedHistorial.keys.sortedDescending().forEach { fecha ->
                    Text(
                        text = formatDate(fecha),
                        color = Principal,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    groupedHistorial[fecha]?.forEach { habito ->
                        FilaHistorialHabito(titulo = habito.titulo, esCompletado = habito.completado)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(top = 20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Background_Box),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Principal)
        ) {
            Text("Volver", color = Principal, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

fun formatDate(dateString: String): String {
    return try {
        val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
        val date = LocalDate.parse(dateString, inputFormatter)
        val outputFormatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale("es", "ES"))
        date.format(outputFormatter).replaceFirstChar { it.uppercase() }
    } catch (e: DateTimeParseException) {
        try {
            val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE
            val date = LocalDate.parse(dateString, inputFormatter)
            val outputFormatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale("es", "ES"))
            date.format(outputFormatter).replaceFirstChar { it.uppercase() }
        } catch (e2: DateTimeParseException) {
            dateString
        }
    }
}


@Composable
fun FilaHistorialHabito(titulo: String, esCompletado: Boolean) {
    val colorEstado = if (esCompletado) Contorno_Completado else Boton_Precaucion
    val icono = if (esCompletado) Icons.Filled.Check else Icons.Filled.Close

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = titulo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                color = colorEstado,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = icono,
                contentDescription = if (esCompletado) "Completado" else "Incompleto",
                modifier = Modifier.size(28.dp),
                tint = colorEstado
            )
        }

        HorizontalDivider(
            color = Secundario.copy(alpha = 0.5f),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
