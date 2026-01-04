package com.tridevs.serplusvita.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tridevs.serplusvita.ui.theme.*
import com.tridevs.serplusvita.viewmodels.habitos.HabitoViewModel
import com.tridevs.serplusvita.viewmodels.peso.PesoViewModel

@Composable
fun RecordsScreen(
    habitoViewModel: HabitoViewModel = hiltViewModel(),
    pesoViewModel: PesoViewModel = hiltViewModel()
) {
    var currentView by remember { mutableStateOf("main") }

    when (currentView) {
        "main" -> MainRecordsView { destination -> currentView = destination }
        "habit_registry" -> HabitRegistryView(habitoViewModel) { currentView = "main" }
        "habit_history" -> HabitHistoryView(habitoViewModel) { currentView = "main" }
        "weight_registry" -> WeightRegistryView(pesoViewModel) { currentView = "main" }
        "metric_registry" -> MetricRegistryView(onNavigate = { dest -> currentView = dest }, onBack = { currentView = "main" })
        "imc_calculator" -> ImcCalculatorScreen { currentView = "metric_registry" }
        "tmb_calculator" -> TmbCalculatorScreen { currentView = "metric_registry" }
    }
}

@Composable
private fun MainRecordsView(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background_App)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth()) {
            Text(text = "Registros", color = Principal, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = Principal, thickness = 2.dp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        FilaRegistroClickable(texto = "Registro de Hábitos") { onNavigate("habit_registry") }
        FilaRegistroClickable(texto = "Historial de Hábitos") { onNavigate("habit_history") }
        FilaRegistroClickable(texto = "Registro de Peso") { onNavigate("weight_registry") }
        FilaRegistroClickable(texto = "Métricas fisiológicas") { onNavigate("metric_registry") }
    }
}

@Composable
fun FilaRegistroClickable(texto: String, onClick: () -> Unit) {
    Column(modifier = Modifier.clickable(onClick = onClick)) {
        BaseFilaRegistro(texto)
    }
}

@Composable
fun FilaRegistroSimple(texto: String) {
    BaseFilaRegistro(texto)
}

@Composable
private fun BaseFilaRegistro(texto: String) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = texto,
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal,
                color = Principal
            )
        }
        HorizontalDivider(
            color = Secundario.copy(alpha = 0.6f),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
