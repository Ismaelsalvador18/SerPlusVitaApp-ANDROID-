package com.tridevs.serplusvita.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tridevs.serplusvita.data.models.UsuarioUpdateRequest
import com.tridevs.serplusvita.ui.theme.*
import com.tridevs.serplusvita.utils.SesionManager
import com.tridevs.serplusvita.viewmodels.profile.ProfileViewModel

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
             ProfileInfoCard(
                usuario = usuario!!,
                onEditClick = { editingField = it.substringBefore(":") }
            )
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
