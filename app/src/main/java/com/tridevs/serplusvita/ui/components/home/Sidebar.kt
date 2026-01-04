package com.tridevs.serplusvita.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
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

@Composable
fun Sidebar(
    onLogout: () -> Unit,
    onNavigateToPreferences: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToAbout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(Background_App)
            .padding(16.dp)
    ) {
        Text(
            text = "Menú",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Principal,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        HorizontalDivider(color = Principal.copy(alpha = 0.5f))

        // Opciones del Sidebar
        SidebarItem(icon = Icons.Default.Settings, text = "Preferencias", onClick = onNavigateToPreferences)
        SidebarItem(icon = Icons.AutoMirrored.Filled.HelpOutline, text = "Ayuda", onClick = onNavigateToHelp)
        SidebarItem(icon = Icons.Default.Info, text = "Acerca de", onClick = onNavigateToAbout)

        Spacer(Modifier.weight(1f))

        // Logout al final
        SidebarItem(icon = Icons.AutoMirrored.Filled.ExitToApp, text = "Cerrar Sesión", onClick = onLogout)
    }
}

@Composable
private fun SidebarItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = text, tint = Principal)
        Spacer(Modifier.width(16.dp))
        Text(text = text, color = Principal, fontSize = 18.sp)
    }
}
