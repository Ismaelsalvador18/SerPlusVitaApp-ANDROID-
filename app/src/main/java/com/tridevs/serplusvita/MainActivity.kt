package com.tridevs.serplusvita

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.tridevs.serplusvita.ui.navigation.AppNavHost
import com.tridevs.serplusvita.ui.theme.SerPlusVitaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SerPlusVitaTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}