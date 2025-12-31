package com.tridevs.serplusvita.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tridevs.serplusvita.ui.screens.home.HomeShell
import com.tridevs.serplusvita.ui.screens.login.VistaBienvenida
import com.tridevs.serplusvita.ui.screens.register.VistaRegistro
import com.tridevs.serplusvita.viewmodels.login.AuthViewModel

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val sesion by authViewModel.sesion.collectAsState()

    // ✅ Decide destino inicial según si hay sesión guardada
    val startDestination = if (sesion != null) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            VistaBienvenida(
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("register") {
            VistaRegistro(
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onGuestClick = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeShell(
                sesion = sesion,
                onLogout = {
                    authViewModel.logout()   // ✅ limpia sesión
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}