package com.tridevs.serplusvita.ui.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tridevs.serplusvita.data.models.HabitoRequest
import com.tridevs.serplusvita.data.models.HabitoResponse
import com.tridevs.serplusvita.ui.theme.*

@Composable
fun CrearHabitoCard(
    onGuardar: (HabitoRequest) -> Unit,
    onCancelar: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var recordatorio by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Background_App)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Título", color = Principal, fontWeight = FontWeight.Bold)
            OutlinedTextField(value = titulo, onValueChange = { titulo = it }, placeholder = { Text("Ingrese título") })

            Text("Descripción", color = Principal, fontWeight = FontWeight.Bold)
            OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, placeholder = { Text("Ingrese descripción") })

            Text("Recordatorio", color = Principal, fontWeight = FontWeight.Bold)
            OutlinedTextField(value = recordatorio, onValueChange = { recordatorio = it }, placeholder = { Text("Ingrese recordatorio") })

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    onGuardar(HabitoRequest(titulo, descripcion, recordatorio, true))
                }) { Text("Guardar") }

                OutlinedButton(onClick = onCancelar) { Text("Cancelar") }
            }
        }
    }
}

@Composable
fun NuevoHabitoCard(onNuevoHabitoClick: () -> Unit) {
    Card(
        onClick = onNuevoHabitoClick,
        modifier = Modifier.fillMaxWidth().height(100.dp).padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Background_App)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Nuevo Hábito", fontSize = 22.sp, color = Principal, modifier = Modifier.weight(1f))
            Icon(Icons.Filled.Edit, contentDescription = "Crear", tint = Principal, modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
fun SinHabitosMessage() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No tienes ningún hábito personalizado.", color = Secundario, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        Text("Puedes crear uno presionando en el botón de arriba.", color = Secundario, fontSize = 16.sp)
    }
}

@Composable
fun HabitoCard(
    habito: HabitoResponse,
    onCompletar: () -> Unit
) {
    val backgroundColor = if (habito.completado) Color(0xFF81C784) else Color(0xFFFFE0B2)

    Card(
        modifier = Modifier.fillMaxWidth().height(200.dp).padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(habito.titulo, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Principal)
            habito.descripcion?.let { Text(it, color = Principal) }
            habito.recordatorio?.let { Text("Recordatorio: $it", color = Principal) }

            if (!habito.completado) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onCompletar) { Text("Completar") }
            } else {
                Icon(Icons.Filled.CheckCircle, contentDescription = "Completado", tint = Contorno_Completado)
            }
        }
    }
}