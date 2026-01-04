package com.tridevs.serplusvita.ui.screens.home

import android.graphics.Paint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import com.tridevs.serplusvita.data.models.PesoResponse
import com.tridevs.serplusvita.ui.theme.*
import com.tridevs.serplusvita.utils.SesionManager
import com.tridevs.serplusvita.viewmodels.peso.PesoViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
