/**
 * NavGraph.kt - Configuración del grafo de navegación
 *
 * Define la estructura de navegación de la aplicación
 * usando Navigation Compose con rutas type-safe.
 *
 * Flujo de navegación:
 * 1. Splash (inicio) → Onboarding (automático después de 2s)
 * 2. Onboarding → Home (al completar o saltar)
 * 3. Home → Detail (al hacer clic en un juego)
 * 4. Detail → Home (botón volver)
 *
 * @author Cristina Fernández
 * @date Enero 2026
 */

package com.crfete.gameDex.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.crfete.gameDex.ui.screens.detail.GameDetailScreen
import com.crfete.gameDex.ui.screens.home.HomeScreen
import com.crfete.gameDex.ui.screens.onboarding.OnboardingScreen
import com.crfete.gameDex.ui.screens.splash.SplashScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash
    ) {
        composable<Routes.Splash> {
            SplashScreen(navController)
        }

        composable<Routes.Onboarding> {
            OnboardingScreen(navController)
        }

        composable<Routes.Home> {
            HomeScreen(navController)
        }

        composable<Routes.Detail> { backStackEntry ->
            val args = backStackEntry.toRoute<Routes.Detail>()
            GameDetailScreen(
                navController = navController,
                gameId = args.gameId
            )
        }
    }
}