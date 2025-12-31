package com.tridevs.serplusvita.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tridevs.serplusvita.ui.theme.Principal

@Composable
fun VistaHome(onLogout: () -> Unit) {
    Column {
        Text("Bienvenido a Ser+ Vita")
        Button(onClick = { onLogout() }) {
            Text("Cerrar sesión")
        }
    }
}