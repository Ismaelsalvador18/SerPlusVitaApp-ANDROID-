package com.tridevs.serplusvita.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val DarkColorScheme = darkColorScheme(
    primary = Contorno_Base.copy(alpha = 0.8f),    // Naranja más suave
    onPrimary = Color.White,                       // Texto blanco sobre naranja

    secondary = Por_Completar.copy(alpha = 0.7f),  // Rosado más suave
    onSecondary = Color.White,                     // Texto blanco sobre rosado

    tertiary = Contorno_Completado.copy(alpha = 0.8f), // Verde más suave
    onTertiary = Color.White,                          // Texto blanco sobre verde

    // Fondos oscuros
    background = Color(0xFF121212),               // Negro para fondo app
    onBackground = Color(0xFFE0E0E0),             // Texto gris claro sobre negro

    surface = Color(0xFF1E1E1E),                  // Gris oscuro para cajas
    onSurface = Color(0xFFE0E0E0),                // Texto claro sobre cajas

    surfaceVariant = Color(0xFF2D2D2D),           // Para items seleccionados oscuro
    onSurfaceVariant = Color(0xFFBDBDBD),         // Texto gris sobre seleccionado

    // Estados
    error = Boton_Precaucion,                     // Mismo rojo
    onError = Color.White,                        // Texto blanco sobre rojo

    // Bordes
    outline = Color(0xFF616161),                  // Gris oscuro para bordes
    outlineVariant = Color(0xFF424242)
)

private val LightColorScheme = lightColorScheme(

    primary = Contorno_Base,
    onPrimary = Principal,

    secondary = Por_Completar,
    onSecondary = Principal,

    tertiary = Contorno_Completado,
    onTertiary = Principal,

    background = Background_App,
    onBackground = Principal,

    surface = Background_Box,
    onSurface = Principal,

    surfaceVariant = Background_Seleccionado,
    onSurfaceVariant = Principal,

    error = Boton_Precaucion,
    onError = Color.White,

    outline = Secundario,
    outlineVariant = Secundario.copy(alpha = 0.5f)
)

@Composable
fun SerPlusVitaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}