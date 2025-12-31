package com.tridevs.serplusvita.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tridevs.serplusvita.ui.theme.*

@Composable
fun ProfileScreen(
    // Parámetros de ID y ViewModel temporalmente desactivados
    // usuarioId: Long,
    onLogout: () -> Unit
    // viewModel: ProfileViewModel = hiltViewModel()
) {

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

        // ✅  DATOS ESTÁTICOS TEMPORALES
        val datos = listOf(
            "Nombre:" to "Juan Perez",
            "Edad:" to "19 años",
            "Altura:" to "169 cm",
            "Peso:" to "60.0 Kg"
        )

        // CARD DE INFORMACIÓN DE USUARIO
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
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    HorizontalDivider(color = Secundario, thickness = 1.dp)
                }
            }
        }

        Spacer(Modifier.height(30.dp))

        // BOTONES DE ACCIÓN
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Button(
                modifier = Modifier.weight(1f).padding(horizontal = 6.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Boton_Precaucion),
                shape = RoundedCornerShape(8.dp),
                onClick = onLogout
            ) {
                Text("Cerrar Sesión", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Button(
                modifier = Modifier.weight(1f).padding(horizontal = 6.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Boton_Precaucion),
                shape = RoundedCornerShape(8.dp),
                onClick = { /* Acción deshabilitada temporalmente */ }
            ) {
                Text("Eliminar Cuenta", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
