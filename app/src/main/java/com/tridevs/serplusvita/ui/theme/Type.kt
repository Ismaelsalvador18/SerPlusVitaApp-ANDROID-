package com.tridevs.serplusvita.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.tridevs.serplusvita.R

val MerriweatherSans = FontFamily(
    Font(R.font.merriweather_sans_regular, FontWeight.Normal),
    Font(R.font.merriweather_sans_bold, FontWeight.Bold),
    Font(R.font.merriweather_sans_light, FontWeight.Light)
)

val Inspiration = FontFamily(
    Font(R.font.inspiration_regular, FontWeight.Normal)
)

val SerPlusVitaStyle = TextStyle(
    fontFamily = Inspiration,
    fontWeight = FontWeight.Normal,
    fontSize = 48.sp
)

val Typography = Typography(
    // Para títulos grandes (pantalla principal, nombre app)
    displayLarge = TextStyle(
        fontFamily = MerriweatherSans,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),

    // Para títulos de secciones (HÁBITOS, MÉTRICAS, etc.)
    titleLarge = TextStyle(
        fontFamily = MerriweatherSans,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),

    // Para subtítulos (nombre de hábito, métricas)
    titleMedium = TextStyle(
        fontFamily = MerriweatherSans,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),

    // Para texto normal (descripciones, botones)
    bodyLarge = TextStyle(
        fontFamily = MerriweatherSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),

    // Para texto secundario (explicaciones pequeñas)
    bodyMedium = TextStyle(
        fontFamily = MerriweatherSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),

    // Para texto pequeño (fechas, horas, detalles)
    bodySmall = TextStyle(
        fontFamily = MerriweatherSans,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp
    ),

    // Para botones principales
    labelLarge = TextStyle(
        fontFamily = MerriweatherSans,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),

    // Para botones pequeños o etiquetas
    labelMedium = TextStyle(
        fontFamily = MerriweatherSans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )

)