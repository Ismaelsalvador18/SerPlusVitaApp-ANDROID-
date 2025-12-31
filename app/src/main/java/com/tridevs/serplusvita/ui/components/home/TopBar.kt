package com.tridevs.serplusvita.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tridevs.serplusvita.ui.components.common.noRippleClickable
import com.tridevs.serplusvita.ui.theme.Background_Seleccionado
import com.tridevs.serplusvita.ui.theme.Principal

@Composable
fun TopBar(
    title: String,
    onMenuClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Background_Seleccionado)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menú",
                tint = Principal,
                modifier = Modifier
                    .size(28.dp)
                    .noRippleClickable(onMenuClick)
            )
            Text(
                text = "Ser+ Vita",
                color = Principal,
                fontSize = 28.sp
            )
        }
    }
}