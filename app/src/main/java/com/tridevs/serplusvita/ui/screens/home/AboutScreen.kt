package com.tridevs.serplusvita.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tridevs.serplusvita.ui.components.LogoSerPlusVita
import com.tridevs.serplusvita.ui.theme.Background_App
import com.tridevs.serplusvita.ui.theme.Principal
import com.tridevs.serplusvita.ui.theme.Secundario

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background_App)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // --- TÍTULO SECCIÓN ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = "Acerca de",
                color = Principal,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(
                color = Principal,
                thickness = 2.dp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- CONTENIDO CENTRAL ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Somos",
                fontSize = 20.sp,
                color = Principal
            )

            LogoSerPlusVita()

            Text(
                text = "Apasionados por la tecnología, el bienestar y la mejora personal. " +
                        "La app es un proyecto independiente con el fin de ayudar a personas " +
                        "a mantener hábitos saludables de forma simple, visual y motivadora.",
                fontSize = 18.sp,
                color = Principal,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Text(
                text = "¿Qué hace la app?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Principal,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Es una herramienta ligera y amigable para el día a día, " +
                        "siguiendo tus hábitos con recordatorios suaves y " +
                        "visualizaciones de progreso. Todo está diseñado para que " +
                        "empieces tu cambio de forma sostenible y positiva.",
                fontSize = 18.sp,
                color = Principal,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(0.8f),
                color = Secundario.copy(alpha = 0.5f),
                thickness = 1.dp
            )

            // --- CONTACTO ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Contáctanos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secundario
                )
                Text(
                    text = "CondoriTVirtor@gmail.com",
                    fontSize = 16.sp,
                    color = Secundario,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
