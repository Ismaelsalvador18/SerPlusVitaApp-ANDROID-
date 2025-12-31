package com.tridevs.serplusvita.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tridevs.serplusvita.ui.components.common.noRippleClickable
import com.tridevs.serplusvita.ui.theme.Principal
import com.tridevs.serplusvita.ui.screens.home.Screen

@Composable
fun BottomBar(
    selected: Screen,
    onSelect: (Screen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomItem(
                label = "Perfil",
                icon = Icons.Filled.Person,
                selected = selected == Screen.Perfil,
            ) { onSelect(Screen.Perfil) }

            BottomItem(
                label = "Hábitos",
                icon = Icons.Filled.FormatListNumbered,
                selected = selected == Screen.Habitos,
            ) { onSelect(Screen.Habitos) }

            BottomItem(
                label = "Registros",
                icon = Icons.Filled.EditNote,
                selected = selected == Screen.Registros,
            ) { onSelect(Screen.Registros) }
        }
    }
}

@Composable
private fun BottomItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.noRippleClickable(onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) Principal else Principal.copy(alpha = 0.6f),
            modifier = Modifier.size(if (selected) 30.dp else 28.dp)
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = if (selected) Principal else Principal.copy(alpha = 0.7f)
        )
        if (selected) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .width(36.dp)
                    .background(Principal.copy(alpha = 0.8f))
            )
        }
    }
}