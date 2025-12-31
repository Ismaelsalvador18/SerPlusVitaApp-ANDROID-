package com.tridevs.serplusvita.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tridevs.serplusvita.ui.components.LogoSerPlusVita
import com.tridevs.serplusvita.ui.components.common.CustomTextField
import com.tridevs.serplusvita.ui.components.common.PrimaryButton
import com.tridevs.serplusvita.ui.theme.*
import com.tridevs.serplusvita.viewmodels.login.AuthViewModel

@Composable
fun VistaBienvenida(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val sesionState = authViewModel.sesion.collectAsState(initial = null)
    val sesion = sesionState.value

    val loading by authViewModel.loading.collectAsState(initial = false)
    val error by authViewModel.error.collectAsState(initial = null)

    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    // ✅ Usar sesion directamente, sin smart cast
    LaunchedEffect(sesion) {
        sesion?.let {
            if (it.id > 0 && !it.token.isNullOrBlank()) {
                onNavigateToHome()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background_App)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoSerPlusVita(tamano = 64)

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Background_Seleccionado, shape = RoundedCornerShape(10.dp))
                .padding(24.dp)
        ) {
            Text(
                text = "Cuida tu salud con hábitos diarios a tu preferencia, registra y mira tus progresos.",
                style = MaterialTheme.typography.bodyMedium,
                color = Principal
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Background_Seleccionado, shape = RoundedCornerShape(10.dp))
                .padding(24.dp)
        ) {
            Column {
                CustomTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = "Correo electrónico",
                    keyboardType = KeyboardType.Email
                )
                Spacer(modifier = Modifier.height(12.dp))
                CustomTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = "Contraseña",
                    isPassword = true,
                    keyboardType = KeyboardType.Password
                )
                Spacer(modifier = Modifier.height(16.dp))
                PrimaryButton(text = "Iniciar sesión") {
                    authViewModel.login(correo, contrasena)
                }
                Spacer(modifier = Modifier.height(8.dp))
                PrimaryButton(text = "Registrarme") {
                    onNavigateToRegister()
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) Text("Cargando...", color = Principal)
        error?.let { Text(it, color = Boton_Precaucion) }
    }
}