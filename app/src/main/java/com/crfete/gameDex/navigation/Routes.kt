package com.crfete.gameDex.navigation

import kotlinx.serialization.Serializable

/**
 *  Routes.kt - Definición de rutas de navegación
 *
 *   Define todas las pantallas de la aplicación usando
 *   Kotlin Serialization para type-safe navigation.
 *
 *  Rutas disponibles:
 *   - Splash: Pantalla inicial con logo (2 segundos)
 *   - Onboarding: 4 pantallas
 *   - Home: Listado principal de juegos
 *   - Detail: Detalle de un juego específico (recibe gameId)
 *  *
 *   @author Cristina Fernández
 *   @date Enero 2026
 */
sealed class Routes {
    @Serializable
    object Splash

    @Serializable
    object Onboarding

    @Serializable
    object Home

    @Serializable
    data class Detail(val gameId: Int)
}