package com.tridevs.serplusvita.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tridevs.serplusvita.ui.theme.*
import kotlin.math.pow

@Composable
fun ImcCalculatorScreen(onBack: () -> Unit) {
    var peso by remember { mutableStateOf(60f) }
    var altura by remember { mutableStateOf(160f) }
    var generoSeleccionado by remember { mutableStateOf("Mujer") }
    var imcResult by remember { mutableStateOf<Pair<Float, String>?>(null) }

    if (imcResult != null) {
        AlertDialog(
            onDismissRequest = { imcResult = null },
            title = { Text("Resultado del IMC", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Tu IMC es: %.2f".format(imcResult!!.first))
                    Spacer(Modifier.height(8.dp))
                    Text(imcResult!!.second)
                }
            },
            confirmButton = {
                Button(onClick = { imcResult = null }) {
                    Text("Entendido")
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
            Text("Métricas Fisiológicas", color = Principal, fontSize = 26.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Principal, thickness = 1.dp)
        }
        Spacer(modifier = Modifier.height(20.dp))

        // SELECCIÓN DE GÉNERO
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            GeneroItem(label = "Hombre", simbolo = "♂", esSeleccionado = generoSeleccionado == "Hombre", modifier = Modifier.weight(1f)) { generoSeleccionado = "Hombre" }
            GeneroItem(label = "Mujer", simbolo = "♀", esSeleccionado = generoSeleccionado == "Mujer", modifier = Modifier.weight(1f)) { generoSeleccionado = "Mujer" }
        }
        Spacer(modifier = Modifier.height(25.dp))

        // SLIDERS
        SliderMetrica(label = "PESO:", valor = peso, rango = 30f..150f, unidad = "Kg") { peso = it }
        Spacer(modifier = Modifier.height(16.dp))
        SliderMetrica(label = "ALTURA:", valor = altura, rango = 100f..220f, unidad = "cm") { altura = it }
        Spacer(modifier = Modifier.height(30.dp))

        // BOTÓN CALCULAR
        Button(
            onClick = {
                val alturaEnMetros = altura / 100f
                val imc = peso / alturaEnMetros.pow(2)
                val imcDescription = when {
                    imc < 18.5 -> "Bajo peso. Es recomendable consultar a un profesional."
                    imc < 24.9 -> "Peso normal. ¡Buen trabajo!"
                    imc < 29.9 -> "Sobrepeso. Considera ajustar tu dieta y ejercicio."
                    else -> "Obesidad. Es importante buscar asesoramiento médico."
                }
                imcResult = Pair(imc, imcDescription)
            },
            modifier = Modifier.fillMaxWidth().height(55.dp).padding(horizontal = 20.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Background_Seleccionado),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text("Calcular IMC", fontSize = 20.sp, color = Principal, fontWeight = FontWeight.Bold)
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
fun GeneroItem(label: String, simbolo: String, esSeleccionado: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val fondo = if (esSeleccionado) Background_Seleccionado else Color.White
    val borde = if (esSeleccionado) Color.Transparent else Contorno_Base

    Surface(
        modifier = modifier.shadow(if (esSeleccionado) 8.dp else 0.dp, RoundedCornerShape(12.dp)).border(2.dp, borde, RoundedCornerShape(12.dp)),
        color = fondo,
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = simbolo, fontSize = 60.sp, color = Principal)
            Text(label, color = Principal, fontSize = 16.sp, fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Normal)
        }
    }
}

@Composable
fun SliderMetrica(label: String, valor: Float, rango: ClosedFloatingPointRange<Float>, unidad: String, onValueChange: (Float) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().border(2.dp, Contorno_Base, RoundedCornerShape(15.dp)).background(Background_Box, RoundedCornerShape(15.dp)).padding(16.dp)) {
        Column {
            Text(text = label, color = Principal, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Slider(
                    value = valor,
                    onValueChange = onValueChange,
                    valueRange = rango,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(thumbColor = Background_Seleccionado, activeTrackColor = Background_Seleccionado, inactiveTrackColor = Secundario)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("${valor.toInt()} $unidad", color = Principal, fontWeight = FontWeight.Bold, modifier = Modifier.width(60.dp), textAlign = TextAlign.End)
            }
        }
    }
}
