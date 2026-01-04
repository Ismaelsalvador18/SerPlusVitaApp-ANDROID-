package com.tridevs.serplusvita.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tridevs.serplusvita.ui.theme.*

@Composable
fun MetricRegistryView(onNavigate: (String) -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background_App)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // TÍTULO
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
            Text("Métricas Fisiológicas", color = Principal, fontSize = 26.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Principal, thickness = 1.dp, modifier = Modifier.width(320.dp).align(Alignment.CenterHorizontally))
        }
        Spacer(modifier = Modifier.height(24.dp))

        // CONTENEDOR DE OPCIONES
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ItemMetricaFisiologica(
                titulo = "Índice de masa corporal (IMC)",
                descripcion = "El IMC es una medida que relaciona tu peso con tu altura para estimar si tienes un peso saludable. Ayuda a identificar riesgos para la salud.",
                onIrClick = { onNavigate("imc_calculator") } 
            )
            ItemMetricaFisiologica(
                titulo = "Tasa Metabólica Basal (TMB)",
                descripcion = "La TMB calcula las calorías mínimas que tu cuerpo necesita en reposo para funcionar. Es clave para ajustar tu dieta si quieres ganar o perder peso.",
                onIrClick = { onNavigate("tmb_calculator") }
            )
            ItemMetricaFisiologica(
                titulo = "Nivel de Actividad Física",
                descripcion = "Esta métrica ayuda a calcular cuántas calorías quemas al día según tu estilo de vida (sedentario, activo, etc.), complementando tu TMB.",
                onIrClick = { /* TODO */ }
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        // BOTÓN DE VOLVER
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().height(60.dp).padding(top = 20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Background_Box),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Principal)
        ) {
            Text("Volver", color = Principal, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ItemMetricaFisiologica(titulo: String, descripcion: String, onIrClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Contorno_Base, RoundedCornerShape(12.dp))
                .background(Background_Box, RoundedCornerShape(12.dp))
                .clickable { expanded = !expanded }
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = titulo, color = Principal, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                Button(
                    onClick = onIrClick, 
                    modifier = Modifier.height(36.dp).width(65.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Background_Seleccionado),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = "IR", fontSize = 14.sp, color = Principal, fontWeight = FontWeight.Bold)
                }
            }
        }

        AnimatedVisibility(visible = expanded) {
            Text(descripcion, color = Principal.copy(alpha = 0.8f), fontSize = 14.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        }
    }
}
