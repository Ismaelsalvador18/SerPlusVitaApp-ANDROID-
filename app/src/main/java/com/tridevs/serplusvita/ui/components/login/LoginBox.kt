package com.tridevs.serplusvita.ui.components.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tridevs.serplusvita.ui.components.common.CustomTextField
import com.tridevs.serplusvita.ui.components.common.PrimaryButton
import com.tridevs.serplusvita.ui.theme.Background_Box
import com.tridevs.serplusvita.ui.theme.Contorno_Base
import com.tridevs.serplusvita.ui.theme.Principal

@Composable
fun LoginBox(
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Background_Box, RoundedCornerShape(20.dp))
            .border(3.dp, Contorno_Base, RoundedCornerShape(20.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bienvenido", color = Principal)

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = email,
            onValueChange = { email = it },
            label = "Correo electrónico"
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Ingresar",
            onClick = { onLoginClick(email, password) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        PrimaryButton(
            text = "Crear cuenta",
            onClick = { onRegisterClick() }
        )
    }
}