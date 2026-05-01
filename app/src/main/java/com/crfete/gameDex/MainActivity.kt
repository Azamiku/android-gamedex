/**
* MainActivity.kt - Punto de entrada de mi aplicación GameDex
*
* Esta Activity inicializa el tema personalizado y configura
* el grafo de navegación principal de la aplicación.
*
* La app utiliza Jetpack Compose para toda la UI y Navigation
* Compose para la navegación entre pantallas.
*
* Flujo de navegación:
* SplashScreen → Onboarding → Home → Detail
*
* @author Cristina Fernández
* @date Enero 2026
* @course Programación multimèdia y dispositivos móviles
*/


package com.crfete.gameDex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.crfete.gameDex.navigation.NavGraph
import com.crfete.gameDex.ui.theme.GameDexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameDexTheme {
                NavGraph()
            }
        }
    }
}