package com.tridevs.serplusvita.ui.screens.home

import android.graphics.Paint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tridevs.serplusvita.data.models.HabitoResponse
import com.tridevs.serplusvita.data.models.PesoResponse
import com.tridevs.serplusvita.ui.theme.*
import com.tridevs.serplusvita.utils.SesionManager
import com.tridevs.serplusvita.viewmodels.habitos.HabitoViewModel
import com.tridevs.serplusvita.viewmodels.peso.PesoViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import kotlin.math.pow

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
fun WeightRegistryView(viewModel: PesoViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    val sesionManager = SesionManager(context)
    val usuarioId = sesionManager.obtenerSesion()?.id

    var weightInput by remember { mutableStateOf("") }
    var selectedPeriod by remember { mutableStateOf(7) } // 7 o 30

    val historialPeso by viewModel.historialPeso.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(usuarioId, selectedPeriod) {
        usuarioId?.let {
            viewModel.obtenerHistorialPeso(it, selectedPeriod)
        }
    }

    val hoySinRegistro = historialPeso.none { it.fecha.startsWith(LocalDate.now().toString()) } && historialPeso.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background_App)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = weightInput,
                        onValueChange = { weightInput = it },
                        modifier = Modifier.weight(1f).background(Color.White, RoundedCornerShape(10.dp)),
                        placeholder = { Text("60.0 kg", color = Secundario, fontSize = 14.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Contorno_Base,
                            unfocusedBorderColor = Secundario
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val peso = weightInput.toDoubleOrNull()
                            if (peso != null && usuarioId != null) {
                                viewModel.registrarPeso(usuarioId, peso, selectedPeriod)
                                weightInput = ""
                            }
                        },
                        enabled = weightInput.isNotBlank()
                    ) {
                        Text("Guardar")
                    }
                }
                
                if (hoySinRegistro && !loading) {
                    Text(
                        text = "Hoy no has registrado un peso. ¡Regístralo para seguir tu progreso!",
                        color = Principal,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { selectedPeriod = 7 },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = if (selectedPeriod == 7) Background_Seleccionado else Color.White),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Secundario)
                    ) { Text("Semana", color = Principal) }
                    Button(
                        onClick = { selectedPeriod = 30 },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = if (selectedPeriod == 30) Background_Seleccionado else Color.White),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Secundario)
                    ) { Text("Mes", color = Principal) }
                }

                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (historialPeso.isNotEmpty()) {
                     GraficoDeBarras(pesos = historialPeso)
                } else {
                    Text("No hay datos de peso para mostrar.", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
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
fun GraficoDeBarras(pesos: List<PesoResponse>) {
    val maxPeso = pesos.maxOfOrNull { it.peso } ?: 0.0
    val minPeso = pesos.minOfOrNull { it.peso } ?: 0.0
    val pesoTextColor = LocalContentColor.current.toArgb()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val barWidth = size.width / (pesos.size * 2)
            val bottomPadding = 60f 

            pesos.forEachIndexed { index, peso ->
                val barHeight = ((peso.peso - minPeso) / (maxPeso - minPeso).coerceAtLeast(1.0) * (size.height - bottomPadding)).toFloat()
                val barLeft = index * 2 * barWidth + barWidth / 2

                // Dibuja la barra
                drawRect(
                    color = Secundario,
                    topLeft = Offset(
                        x = barLeft,
                        y = size.height - barHeight - bottomPadding
                    ),
                    size = Size(barWidth, barHeight)
                )

                // Dibuja el texto del peso encima de la barra
                drawContext.canvas.nativeCanvas.drawText(
                    String.format("%.1f kg", peso.peso),
                    barLeft + barWidth / 2,
                    size.height - barHeight - bottomPadding - 10,
                    Paint().apply {
                        color = pesoTextColor
                        textAlign = Paint.Align.CENTER
                        textSize = 35f // Aumentar tamaño de texto
                    }
                )

                // Dibuja el texto de la fecha debajo de la barra
                val formattedDate = try {
                    val date = LocalDate.parse(peso.fecha, DateTimeFormatter.ISO_DATE_TIME)
                    date.format(DateTimeFormatter.ofPattern("dd/MM"))
                } catch (e: Exception) {
                    "N/A"
                }

                drawContext.canvas.nativeCanvas.drawText(
                    formattedDate,
                    barLeft + barWidth / 2,
                    size.height - bottomPadding + 40, // Ajustar posición
                    Paint().apply {
                        color = pesoTextColor
                        textAlign = Paint.Align.CENTER
                        textSize = 30f // Aumentar tamaño de texto
                    }
                )
            }
        }
    }
}


@Composable
private fun HabitHistoryView(viewModel: HabitoViewModel, onBack: () -> Unit) {
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
private fun HabitRegistryView(viewModel: HabitoViewModel, onBack: () -> Unit) {
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
private fun TarjetaHabitoEditable(
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
