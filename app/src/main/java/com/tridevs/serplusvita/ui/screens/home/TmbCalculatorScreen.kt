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

@Composable
fun TmbCalculatorScreen(onBack: () -> Unit) {
    var altura by remember { mutableStateOf(160f) }
    var peso by remember { mutableStateOf(60f) }
    var edad by remember { mutableStateOf(25f) }
    var generoSeleccionado by remember { mutableStateOf("Mujer") }
    var tmbResult by remember { mutableStateOf<Float?>(null) }

    if (tmbResult != null) {
        AlertDialog(
            onDismissRequest = { tmbResult = null },
            title = { Text("Resultado TMB", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Tu Tasa Metabólica Basal es de %.0f calorías diarias.".format(tmbResult!!))
                    Spacer(Modifier.height(8.dp))
                    Text("Estas son las calorías que tu cuerpo necesita en reposo absoluto.")
                }
            },
            confirmButton = {
                Button(onClick = { tmbResult = null }) {
                    Text("Entendido")
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Background_App).padding(20.dp).verticalScroll(rememberScrollState())
    ) {
        // TÍTULO
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Tasa Metabólica Basal", color = Principal, fontSize = 26.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Principal, thickness = 1.dp)
        }
        Spacer(modifier = Modifier.height(20.dp))

        // GÉNERO
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            TmbGeneroItem(label = "Hombre", simbolo = "♂", esSeleccionado = generoSeleccionado == "Hombre", modifier = Modifier.weight(1f)) { generoSeleccionado = "Hombre" }
            TmbGeneroItem(label = "Mujer", simbolo = "♀", esSeleccionado = generoSeleccionado == "Mujer", modifier = Modifier.weight(1f)) { generoSeleccionado = "Mujer" }
        }
        Spacer(modifier = Modifier.height(25.dp))

        // SLIDERS
        TmbSliderCard(label = "ALTURA:", valor = altura, rango = 100f..220f, unidad = "cm") { altura = it }
        Spacer(modifier = Modifier.height(16.dp))
        TmbSliderCard(label = "PESO:", valor = peso, rango = 30f..150f, unidad = "Kg") { peso = it }
        Spacer(modifier = Modifier.height(16.dp))
        TmbSliderCard(label = "EDAD:", valor = edad, rango = 15f..90f, unidad = "años") { edad = it }
        Spacer(modifier = Modifier.height(30.dp))

        // BOTÓN CALCULAR
        Button(
            onClick = {
                tmbResult = if (generoSeleccionado == "Hombre") {
                    88.362f + (13.397f * peso) + (4.799f * altura) - (5.677f * edad)
                } else { // Mujer
                    447.593f + (9.247f * peso) + (3.098f * altura) - (4.330f * edad)
                }
            },
            modifier = Modifier.fillMaxWidth().height(55.dp).padding(horizontal = 20.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Background_Seleccionado),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text("Calcular TMB", fontSize = 20.sp, color = Principal, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.weight(1f))
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
fun TmbSliderCard(label: String, valor: Float, rango: ClosedFloatingPointRange<Float>, unidad: String, onValueChange: (Float) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().border(2.dp, Contorno_Base, RoundedCornerShape(12.dp)).background(Background_Box, RoundedCornerShape(12.dp)).padding(16.dp)) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = label, color = Principal, fontWeight = FontWeight.Bold)
                Text(text = "${valor.toInt()} $unidad", color = Principal, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = valor,
                onValueChange = onValueChange,
                valueRange = rango,
                colors = SliderDefaults.colors(thumbColor = Background_Seleccionado, activeTrackColor = Background_Seleccionado, inactiveTrackColor = Secundario)
            )
        }
    }
}

@Composable
fun TmbGeneroItem(label: String, simbolo: String, esSeleccionado: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.shadow(if (esSeleccionado) 8.dp else 0.dp, RoundedCornerShape(12.dp)).border(2.dp, if (esSeleccionado) Color.Transparent else Contorno_Base, RoundedCornerShape(12.dp)),
        color = if (esSeleccionado) Background_Seleccionado else Color.White,
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = simbolo, fontSize = 60.sp, color = Principal)
            Text(text = label, color = Principal, fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Normal)
        }
    }
}
