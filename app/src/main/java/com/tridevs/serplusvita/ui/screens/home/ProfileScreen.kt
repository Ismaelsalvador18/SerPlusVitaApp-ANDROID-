package com.tridevs.serplusvita.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tridevs.serplusvita.data.models.UsuarioUpdateRequest
import com.tridevs.serplusvita.ui.theme.*
import com.tridevs.serplusvita.utils.SesionManager
import com.tridevs.serplusvita.viewmodels.profile.ProfileViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val sesionManager = SesionManager(context)
    val usuarioId = sesionManager.obtenerSesion()?.id

    val usuario by viewModel.usuario.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var editingField by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(usuarioId) {
        usuarioId?.let {
            viewModel.cargarUsuario(it)
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        sesionManager.limpiar()
                        onLogout()
                        showLogoutDialog = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Cuenta") },
            text = { Text("¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        usuarioId?.let {
                            viewModel.eliminarUsuario(it) {
                                sesionManager.limpiar()
                                onLogout()
                            }
                        }
                        showDeleteDialog = false
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    editingField?.let { field ->
        if (field == "Edad") {
            EditDateDialog(
                initialDate = usuario?.fechaNacimiento,
                onDismiss = { editingField = null },
                onSave = { newDate ->
                    usuarioId?.let {
                        val request = UsuarioUpdateRequest(nombre = null, altura = null, peso = null, fechaNacimiento = newDate)
                        viewModel.actualizarUsuario(it, request)
                    }
                    editingField = null
                }
            )
        } else {
            EditFieldDialog(field, usuario?.let {
                when(field) {
                    "Nombre" -> it.nombre
                    "Altura" -> it.altura.toString()
                    "Peso" -> it.peso.toString()
                    else -> ""
                }
            } ?: "", onDismiss = { editingField = null }) { newValue ->
                usuarioId?.let {
                    val request = when(field) {
                        "Nombre" -> UsuarioUpdateRequest(nombre = newValue, altura = null, peso = null, fechaNacimiento = null)
                        "Altura" -> UsuarioUpdateRequest(nombre = null, altura = newValue.toIntOrNull(), peso = null, fechaNacimiento = null)
                        "Peso" -> UsuarioUpdateRequest(nombre = null, altura = null, peso = newValue.toDoubleOrNull(), fechaNacimiento = null)
                        else -> UsuarioUpdateRequest(nombre=null, altura = null, peso = null, fechaNacimiento = null)
                    }
                    viewModel.actualizarUsuario(it, request)
                }
                editingField = null
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background_App)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // TÍTULO DE LA SECCIÓN
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Mi Perfil",
                    color = Principal,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .height(3.dp)
                        .fillMaxWidth()
                        .background(Contorno_Base)
                )
            }
        }

        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (error != null) {
            Text(text = error!!, color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (usuario != null) {
            val datos = listOf(
                "Nombre" to "${usuario?.nombre}",
                "Edad" to "${usuario?.fechaNacimiento?.let { calcularEdad(it) } ?: "N/A"} años",
                "Altura" to "${usuario?.altura ?: "N/A"} cm",
                "Peso" to "${usuario?.peso ?: "N/A"} Kg"
            )

            // CARD DE INFORMACIÓN DE USUARIO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        shape = RoundedCornerShape(12.dp),
                        color = Background_App
                    )
                    .border(
                        width = 2.dp,
                        shape = RoundedCornerShape(12.dp),
                        color = Secundario
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    datos.forEach { (label, value) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = label,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Principal,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = value,
                                textAlign = TextAlign.End,
                                fontSize = 18.sp,
                                color = Principal,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(Modifier.width(16.dp))
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Editar",
                                tint = Principal,
                                modifier = Modifier
                                    .size(22.dp)
                                    .clickable { editingField = label.substringBefore(":") }
                            )
                        }
                        HorizontalDivider(color = Secundario, thickness = 1.dp)
                    }
                }
            }
        }


        Spacer(Modifier.height(30.dp))

        // BOTONES DE ACCIÓN
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Boton_Precaucion),
                shape = RoundedCornerShape(8.dp),
                onClick = { showLogoutDialog = true }
            ) {
                Text("Cerrar Sesión", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Boton_Precaucion),
                shape = RoundedCornerShape(8.dp),
                onClick = { showDeleteDialog = true }
            ) {
                Text("Eliminar Cuenta", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDateDialog(initialDate: String?, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate?.let {
            LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli()
        } ?: Instant.now().toEpochMilli()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                        onSave(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    }
                }
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun EditFieldDialog(field: String, initialValue: String, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var text by remember { mutableStateOf(initialValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar $field") },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (field == "Altura" || field == "Peso") KeyboardType.Number else KeyboardType.Text
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = { 
                    onSave(text)
                    onDismiss()
                 }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

fun calcularEdad(fechaNacimiento: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val nacimiento = LocalDate.parse(fechaNacimiento, formatter)
    val ahora = LocalDate.now()
    return Period.between(nacimiento, ahora).years
}
