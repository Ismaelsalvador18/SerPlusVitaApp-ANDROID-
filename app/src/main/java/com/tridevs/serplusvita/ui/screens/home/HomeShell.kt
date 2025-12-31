package com.tridevs.serplusvita.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.tridevs.serplusvita.data.models.Sesion
import com.tridevs.serplusvita.ui.components.common.noRippleClickable
import com.tridevs.serplusvita.ui.components.home.BottomBar
import com.tridevs.serplusvita.ui.components.home.Sidebar
import com.tridevs.serplusvita.ui.components.home.TopBar
import com.tridevs.serplusvita.ui.theme.Background_App

// ✅ Añadimos la pantalla "Acerca de"
enum class Screen { Perfil, Habitos, Registros, Preferencias, Ayuda, AcercaDe }

@Composable
fun HomeShell(
    sesion: Sesion?,
    onLogout: () -> Unit
) {

    var currentScreen by remember { mutableStateOf(Screen.Habitos) }
    var sidebarOpen by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                title = when (currentScreen) {
                    Screen.Habitos -> "Hábitos"
                    Screen.Perfil -> "Perfil"
                    Screen.Registros -> "Registros"
                    Screen.Preferencias -> "Preferencias"
                    Screen.Ayuda -> "Ayuda"
                    Screen.AcercaDe -> "Acerca de"
                },
                onMenuClick = { sidebarOpen = true }
            )
        },
        bottomBar = {
            BottomBar(
                selected = currentScreen,
                onSelect = { currentScreen = it }
            )
        },
        containerColor = Background_App
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Background_App)
        ) {
            when (currentScreen) {
                Screen.Habitos -> {
                    sesion?.id?.let { usuarioId ->
                        HabitsScreen(usuarioId = usuarioId)
                    }
                }
                Screen.Perfil -> {
                    ProfileScreen(onLogout = onLogout)
                }
                Screen.Registros -> RecordsScreen()
                Screen.Preferencias -> PreferencesScreen()
                Screen.Ayuda -> HelpScreen()
                Screen.AcercaDe -> AboutScreen()
            }

            if (sidebarOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.25f))
                        .noRippleClickable { sidebarOpen = false }
                )
            }

            AnimatedVisibility(
                visible = sidebarOpen,
                enter = slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(220)),
                exit = slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(220))
            ) {
                Sidebar(
                    onClose = { sidebarOpen = false },
                    onLogout = {
                        sidebarOpen = false
                        onLogout()
                    },
                    onNavigateToPreferences = { 
                        currentScreen = Screen.Preferencias
                        sidebarOpen = false
                    },
                    onNavigateToHelp = {
                        currentScreen = Screen.Ayuda
                        sidebarOpen = false
                    },
                     onNavigateToAbout = {
                        currentScreen = Screen.AcercaDe
                        sidebarOpen = false
                    }
                )
            }
        }
    }
}