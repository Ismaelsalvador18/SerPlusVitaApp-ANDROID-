package com.tridevs.serplusvita.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tridevs.serplusvita.ui.theme.Background_App
import com.tridevs.serplusvita.ui.theme.Principal
import com.tridevs.serplusvita.ui.theme.Secundario

@Composable
fun PreferencesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background_App)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // TÍTULO SUPERIOR
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Preferencias",
                color = Principal, 
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = Principal, thickness = 2.dp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // LISTA DE OPCIONES
        FilaOpcionPreferencia(
            titulo = "Idioma",
            icono = Icons.Default.Language
        ) { 
            // TODO: Navegar a la pantalla de selección de idioma
        }

        FilaOpcionPreferencia(
            titulo = "Notificaciones",
            icono = Icons.Default.Notifications
        ) {
             // TODO: Navegar a los ajustes de notificaciones
        }

        FilaOpcionPreferencia(
            titulo = "Tema",
            icono = Icons.Default.WbSunny
        ) {
            // TODO: Mostrar selector de tema (claro/oscuro/sistema)
        }
    }
}

@Composable
private fun FilaOpcionPreferencia(
    titulo: String,
    icono: ImageVector,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = titulo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Principal
            )

            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = Principal,
                modifier = Modifier.size(26.dp)
            )
        }

        HorizontalDivider(
            color = Secundario.copy(alpha = 0.4f),
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
