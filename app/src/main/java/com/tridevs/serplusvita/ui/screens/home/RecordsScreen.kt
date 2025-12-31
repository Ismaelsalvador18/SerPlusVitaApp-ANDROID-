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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
fun RecordsScreen() {
    var currentView by remember { mutableStateOf("main") }

    when (currentView) {
        "main" -> MainRecordsView { destination -> currentView = destination }
        "habit_registry" -> HabitRegistryView { currentView = "main" }
        "habit_history" -> HabitHistoryView { currentView = "main" }
        "weight_registry" -> WeightRegistryView { currentView = "main" }
        "metric_registry" -> MetricRegistryView(onNavigate = { dest -> currentView = dest }, onBack = { currentView = "main" })
        "imc_calculator" -> ImcCalculatorScreen { currentView = "metric_registry" }
        "tmb_calculator" -> TmbCalculatorScreen { currentView = "metric_registry" } // ✅ Nueva vista
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
            TmbGeneroItem(label = "Hombre", simbolo = "\u2642", esSeleccionado = generoSeleccionado == "Hombre", modifier = Modifier.weight(1f)) { generoSeleccionado = "Hombre" }
            TmbGeneroItem(label = "Mujer", simbolo = "\u2640", esSeleccionado = generoSeleccionado == "Mujer", modifier = Modifier.weight(1f)) { generoSeleccionado = "Mujer" }
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

// --- Vistas y Composables Anteriores (sin cambios significativos) ---

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
            GeneroItem(label = "Hombre", simbolo = "\u2642", esSeleccionado = generoSeleccionado == "Hombre", modifier = Modifier.weight(1f)) { generoSeleccionado = "Hombre" }
            GeneroItem(label = "Mujer", simbolo = "\u2640", esSeleccionado = generoSeleccionado == "Mujer", modifier = Modifier.weight(1f)) { generoSeleccionado = "Mujer" }
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
                onIrClick = { onNavigate("tmb_calculator") } // ✅ Conectado
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


@Composable
fun WeightRegistryView(onBack: () -> Unit) {
    var weight by remember { mutableStateOf("") }
    var selectedPeriod by remember { mutableStateOf("Semana") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background_App)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // TÍTULO
        Text(
            text = "Registro de Peso",
            color = Principal,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Principal, thickness = 1.dp)

        Spacer(modifier = Modifier.height(24.dp))

        // TARJETA DE REGISTRO
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Contorno_Base, RoundedCornerShape(15.dp)),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = Background_Box)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // INPUT PESO
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Peso:", color = Principal, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { weight = it },
                        modifier = Modifier.width(110.dp).background(Color.White, RoundedCornerShape(10.dp)),
                        placeholder = { Text("60.0 kg", color = Secundario, fontSize = 14.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Contorno_Base,
                            unfocusedBorderColor = Secundario
                        )
                    )
                }

                // SELECTOR SEMANA / MES
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { selectedPeriod = "Semana" },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = if (selectedPeriod == "Semana") Background_Seleccionado else Color.White),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Secundario)
                    ) { Text("Semana", color = Principal) }
                    Button(
                        onClick = { selectedPeriod = "Mes" },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = if (selectedPeriod == "Mes") Background_Seleccionado else Color.White),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Secundario)
                    ) { Text("Mes", color = Principal) }
                }

                // GRÁFICA
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            val alturas = listOf(0.6f, 0.4f, 0.75f, 0.5f, 0.9f, 0.35f, 0.6f)
                            alturas.forEach { ratio ->
                                Box(modifier = Modifier.width(18.dp).fillMaxHeight(ratio).background(Secundario, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            listOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom").forEach { dia ->
                                Text(text = dia, color = Principal, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        // BOTÓN DE VOLVER
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Background_Box),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Principal)
        ) {
            Text("Volver", color = Principal, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
private fun HabitHistoryView(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background_App)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // TÍTULO
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

        val historial = listOf(
            Pair("Ejercicio", true),
            Pair("Sueño", true),
            Pair("Meditación", true),
            Pair("Lectura", false),
            Pair("Ejercicio", true),
            Pair("Sueño", true),
            Pair("Meditación", false),
            Pair("Lectura", true),
            Pair("Ejercicio", true),
            Pair("Meditación", false)
        )

        historial.forEach { item ->
            FilaHistorialHabito(titulo = item.first, esCompletado = item.second)
        }
    }
}

@Composable
private fun FilaHistorialHabito(titulo: String, esCompletado: Boolean) {
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

@Composable
private fun HabitRegistryView(onBack: () -> Unit) {
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
        val habitos = listOf("Caminata", "Agua", "Ejercicio", "Sueño", "Alimentación")
        habitos.forEach { habito ->
            TarjetaHabitoEditable(titulo = habito)
            Spacer(modifier = Modifier.height(12.dp))
        }

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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lectura",
                    color = Principal,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.KeyboardReturn,
                    contentDescription = null,
                    tint = Principal,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun TarjetaHabitoEditable(titulo: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .border(1.dp, Contorno_Base, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = Background_Box),
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
                text = titulo,
                color = Principal,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { /* Editar */ }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Principal,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = { /* Borrar */ }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Borrar",
                        tint = Principal,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
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
