package com.tridevs.serplusvita.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tridevs.serplusvita.data.models.Usuario
import com.tridevs.serplusvita.ui.theme.Background_App
import com.tridevs.serplusvita.ui.theme.Principal
import com.tridevs.serplusvita.ui.theme.Secundario
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@Composable
fun ProfileInfoCard(
    usuario: Usuario,
    onEditClick: (String) -> Unit
) {
    val datos = listOf(
        "Nombre" to "${usuario.nombre}",
        "Edad" to "${usuario.fechaNacimiento?.let { calcularEdad(it) } ?: "N/A"} años",
        "Altura" to "${usuario.altura ?: "N/A"} cm",
        "Peso" to "${usuario.peso ?: "N/A"} Kg"
    )

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
                            .clickable { onEditClick(label) }
                    )
                }
                HorizontalDivider(color = Secundario, thickness = 1.dp)
            }
        }
    }
}

fun calcularEdad(fechaNacimiento: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val nacimiento = LocalDate.parse(fechaNacimiento, formatter)
    val ahora = LocalDate.now()
    return Period.between(nacimiento, ahora).years
}
