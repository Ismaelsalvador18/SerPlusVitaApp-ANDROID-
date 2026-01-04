package com.tridevs.serplusvita.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tridevs.serplusvita.data.models.HabitoRequest
import com.tridevs.serplusvita.data.models.HabitoResponse
import com.tridevs.serplusvita.ui.theme.*

@Composable
fun HabitoCard(
    habito: HabitoResponse,
    onCompletar: () -> Unit,
    onEditar: () -> Unit,
    onDeshabilitar: () -> Unit,
    onEliminar: () -> Unit
) {
    val colorBorde = if (habito.completado) Contorno_Completado else Contorno_Base
    val colorFondo = if (habito.completado) Background_Completado else Background_Box
    var menuVisible by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(vertical = 8.dp)
            .border(1.5.dp, colorBorde, RoundedCornerShape(15.dp)),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habito.titulo,
                    color = Principal,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                habito.descripcion?.let {
                    Text(
                        text = it,
                        color = Principal,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxHeight()
            ) {
                Box {
                    IconButton(onClick = { menuVisible = true }) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Opciones",
                            tint = Principal,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = menuVisible,
                        onDismissRequest = { menuVisible = false }
                    ) {
                        DropdownMenuItem(text = { Text("Editar") }, onClick = onEditar)
                        DropdownMenuItem(text = { Text("Deshabilitar") }, onClick = onDeshabilitar)
                        DropdownMenuItem(text = { Text("Eliminar") }, onClick = onEliminar)
                    }
                }

                if (!habito.completado) {
                    Button(
                        onClick = onCompletar,
                        colors = ButtonDefaults.buttonColors(containerColor = Contorno_Completado),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Completar", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Surface(
                        color = Contorno_Completado,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Completado",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigurarHabitoForm(
    habitoExistente: HabitoResponse? = null,
    onGuardar: (HabitoRequest) -> Unit,
    onCancelar: () -> Unit
) {
    var titulo by remember { mutableStateOf(habitoExistente?.titulo ?: "") }
    var descripcion by remember { mutableStateOf(habitoExistente?.descripcion ?: "") }
    var recordatorio by remember { mutableStateOf(habitoExistente?.recordatorio ?: "") }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showTimePicker) {
        TimePickerDialog(
            onDismiss = { showTimePicker = false },
            onConfirm = { newTime ->
                recordatorio = newTime
                showTimePicker = false
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
        Text(
            text = if (habitoExistente != null) "Editar Hábito" else "Crear Hábito",
            color = Principal,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Contorno_Base)
        )

        Spacer(modifier = Modifier.height(100.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = Contorno_Base,
                    shape = RoundedCornerShape(12.dp)
                ),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Background_Box),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                EntradaTextoPersonalizada(
                    valor = titulo,
                    etiqueta = "Título",
                    placeholder = "Ej: Beber agua",
                    onValueChange = { titulo = it }
                )

                EntradaTextoPersonalizada(
                    valor = descripcion,
                    etiqueta = "Descripción",
                    placeholder = "Detalles del hábito...",
                    alturaCaja = 100.dp,
                    singleLine = false,
                    onValueChange = { descripcion = it }
                )

                EntradaClickablePersonalizada(
                    valor = recordatorio,
                    etiqueta = "Recordatorio",
                    placeholder = "Ej: 08:00 AM",
                    onClick = { showTimePicker = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    val request = HabitoRequest(
                        titulo = titulo,
                        descripcion = descripcion,
                        recordatorio = recordatorio,
                        habilitado = habitoExistente?.habilitado ?: true
                    )
                    onGuardar(request)
                },
                modifier = Modifier
                    .width(150.dp)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Contorno_Completado),
                shape = RoundedCornerShape(20.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text("Guardar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val timePickerState = rememberTimePickerState(is24Hour = true)
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar Hora") },
        confirmButton = {
            TextButton(
                onClick = {
                    val formattedTime = String.format("%02d:%02d:00", timePickerState.hour, timePickerState.minute)
                    onConfirm(formattedTime)
                }
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        text = { 
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TimePicker(state = timePickerState)
            } 
        }
    )
}

@Composable
fun EntradaClickablePersonalizada(
    valor: String,
    etiqueta: String,
    placeholder: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = etiqueta,
            color = Principal,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 5.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .height(45.dp)
                .fillMaxWidth()
                .shadow(2.dp, RoundedCornerShape(10.dp))
                .background(Color.White, RoundedCornerShape(10.dp))
                .clickable { onClick() },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = if (valor.isEmpty()) placeholder else valor,
                color = if (valor.isEmpty()) Color.LightGray else Color.Black,
                modifier = Modifier.padding(horizontal = 12.dp),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun EntradaTextoPersonalizada(
    valor: String,
    etiqueta: String,
    placeholder: String,
    alturaCaja: Dp = 45.dp,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = etiqueta,
            color = Principal,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 5.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .height(alturaCaja)
                .fillMaxWidth()
                .shadow(2.dp, RoundedCornerShape(10.dp))
                .background(Color.White, RoundedCornerShape(10.dp)),
            contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
        ) {
            BasicTextField(
                value = valor,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = if (singleLine) 0.dp else 10.dp),
                singleLine = singleLine,
                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp)
            )
            if (valor.isEmpty()) {
                Text(
                    text = placeholder,
                    color = Color.LightGray,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = if (singleLine) 0.dp else 10.dp),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun NuevoHabitoCard(onNuevoHabitoClick: () -> Unit) {
    Card(
        onClick = onNuevoHabitoClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Background_App)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
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
