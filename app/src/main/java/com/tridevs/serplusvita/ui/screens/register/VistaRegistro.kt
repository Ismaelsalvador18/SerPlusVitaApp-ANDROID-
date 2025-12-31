package com.tridevs.serplusvita.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tridevs.serplusvita.ui.components.LogoSerPlusVita
import com.tridevs.serplusvita.ui.components.common.CustomTextField
import com.tridevs.serplusvita.ui.components.common.PrimaryButton
import com.tridevs.serplusvita.ui.theme.*
import com.tridevs.serplusvita.viewmodels.login.AuthViewModel
import com.tridevs.serplusvita.data.models.UsuarioRegistro
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import com.tridevs.serplusvita.utils.validarFecha
import androidx.compose.ui.text.input.TextFieldValue
import java.time.LocalDate

fun validarFecha(fecha: String): Boolean {
    return try {
        LocalDate.parse(fecha) // lanza excepción si la fecha no existe
        true
    } catch (e: Exception) {
        false
    }
}

@Composable
fun VistaRegistro(
    onRegisterSuccess: () -> Unit,
    onGuestClick: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val loading by authViewModel.loading.collectAsState(initial = false)
    val error by authViewModel.error.collectAsState(initial = null)
    val sesionState = authViewModel.sesion.collectAsState(initial = null)
    val sesion = sesionState.value

    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarPassword by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf(TextFieldValue("")) }
    var invitado by remember { mutableStateOf(false) }

    var localError by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background_App)
            .verticalScroll(scrollState)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoSerPlusVita(tamano = 56)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Crear cuenta", style = MaterialTheme.typography.titleLarge, color = Principal)

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Background_Seleccionado, shape = RoundedCornerShape(10.dp))
                .padding(24.dp)
        ) {
            Column {
                if (!invitado) {
                    CustomTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        label = "Correo electrónico",
                        keyboardType = KeyboardType.Email,
                        errorMessage = if (correo.isNotBlank() && !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches())
                            "Formato de correo inválido" else null
                    )

                    CustomTextField(
                        value = contrasena,
                        onValueChange = { contrasena = it },
                        label = "Contraseña",
                        isPassword = true,
                        keyboardType = KeyboardType.Password,
                        errorMessage = if (contrasena.isNotBlank() && contrasena.length < 8)
                            "Debe tener al menos 8 caracteres" else null
                    )

                    CustomTextField(
                        value = confirmarPassword,
                        onValueChange = { confirmarPassword = it },
                        label = "Confirmar contraseña",
                        isPassword = true,
                        keyboardType = KeyboardType.Password,
                        errorMessage = if (confirmarPassword.isNotBlank() && confirmarPassword != contrasena)
                            "Las contraseñas no coinciden" else null
                    )
                }

                CustomTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = "Nombre completo",
                    keyboardType = KeyboardType.Text,
                    errorMessage = if (nombre.isNotBlank() && nombre.length < 4)
                        "Debe tener al menos 4 caracteres" else null
                )

                CustomTextField(
                    value = altura,
                    onValueChange = { altura = it },
                    label = "Altura (cm)",
                    keyboardType = KeyboardType.Number,
                    errorMessage = when {
                        altura.isBlank() -> null
                        altura.toIntOrNull() == null -> "Altura inválida"
                        altura.toInt() < 120 || altura.toInt() > 250 -> "Debe estar entre 120 y 250 cm"
                        else -> null // 🔹 no mostrar mensaje si es válido
                    }
                )

                CustomTextField(
                    value = peso,
                    onValueChange = { peso = it },
                    label = "Peso (kg)",
                    keyboardType = KeyboardType.Number,
                    errorMessage = when {
                        peso.isBlank() -> null
                        peso.toDoubleOrNull() == null -> "Peso inválido"
                        peso.toDouble() < 30 || peso.toDouble() > 300 -> "Debe estar entre 30 y 300 kg"
                        else -> null // 🔹 no mostrar mensaje si es válido
                    }
                )

                CustomTextField(
                    value = fechaNacimiento.text,   // 🔹 pasamos solo el String
                    onValueChange = { input ->
                        var formatted = input.filter { it.isDigit() }

                        if (formatted.length > 4) {
                            formatted = formatted.substring(0,4) + "-" + formatted.substring(4)
                        }
                        if (formatted.length > 7) {
                            formatted = formatted.substring(0,7) + "-" + formatted.substring(7)
                        }
                        if (formatted.length > 10) {
                            formatted = formatted.substring(0,10)
                        }

                        // 🔹 actualizamos el TextFieldValue con cursor al final
                        fechaNacimiento = TextFieldValue(
                            text = formatted,
                            selection = TextRange(formatted.length)
                        )
                    },
                    label = "Fecha nacimiento (AAAA-MM-DD)",
                    keyboardType = KeyboardType.Number,
                    errorMessage = if (fechaNacimiento.text.isNotBlank() && !validarFecha(fechaNacimiento.text))
                        "Fecha inválida" else null
                )



                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = invitado, onCheckedChange = { invitado = it })
                    Text("Entrar como invitado", color = Principal)
                }

                if (invitado) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "⚠️ Como invitado no podrás recuperar tu cuenta si pierdes el dispositivo.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Boton_Precaucion
                    )
                }

                PrimaryButton(text = if (invitado) "Entrar como invitado" else "Registrarme") {
                    localError = null
                    try {
                        if (!invitado) {
                            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                                throw Exception("Correo inválido")
                            }
                            if (contrasena.length < 8) {
                                throw Exception("La contraseña debe tener al menos 8 caracteres")
                            }
                            if (contrasena != confirmarPassword) {
                                throw Exception("Las contraseñas no coinciden")
                            }
                        }
                        if (nombre.length < 4) throw Exception("El nombre debe tener al menos 4 caracteres")

                        val alturaVal = altura.toIntOrNull() ?: throw Exception("Altura inválida")
                        if (alturaVal < 120 || alturaVal > 250) throw Exception("Altura debe estar entre 120 y 250 cm")

                        val pesoVal = peso.toDoubleOrNull() ?: throw Exception("Peso inválido")
                        if (pesoVal < 30 || pesoVal > 300) throw Exception("Peso debe estar entre 30 y 300 kg")

                        if (!validarFecha(fechaNacimiento.text)) throw Exception("Fecha inválida")

                        val request = UsuarioRegistro(
                            correo = if (invitado) null else correo,
                            contrasena = if (invitado) null else contrasena,
                            nombre = nombre,
                            altura = alturaVal,
                            peso = pesoVal,
                            fechaNacimiento = fechaNacimiento.text, // 🔹 aquí va el String
                            invitado = invitado
                        )
                        authViewModel.registro(request)
                    } catch (e: Exception) {
                        localError = e.message
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) Text("Cargando...", color = Principal)
        error?.let { Text(it, color = Boton_Precaucion) }
        localError?.let { Text(it, color = Boton_Precaucion) }

        sesion?.let { s ->
            if (s.id > 0 && !s.token.isNullOrBlank()) {
                onRegisterSuccess()
            }
        }
    }
}