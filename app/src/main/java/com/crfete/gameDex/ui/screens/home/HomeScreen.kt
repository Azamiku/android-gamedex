/**
 * HomeScreen.kt - Pantalla principal de la app GameDex
 *
 * Muestra una cuadrícula adaptativa de videojuegos con:
 * - Grid de 2 columnas en portrait, 3 en landscape
 * - Cards clickeables con imagen, título, plataforma,rating (puntuación) y desarrolladora
 * - Degradado oscuro para mejorar la  legibilidad del texto
 * - TopBar con el logo/nombre de la app
 * - Navegación al detalle al hacer clic en un juego
 *
 * Cada card muestra:
 * - Imagen del juego (con degradado)
 * - Rating con estrella dorada (arriba)
 * - Título del juego (abajo, blanco)
 * - Plataforma (abajo, púrpura claro)
 *
 * Cumple los requisitos de:
 * - Dos pantallas con paso de información
 * - Versiones portrait y landscape
 *
 * @param navController Controlador de navegación para ir a DetailScreen
 * @author Cristina Fernández
 * @date Enero 2026
 */
package com.crfete.gameDex.ui.screens.home


import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.crfete.gameDex.R
import com.crfete.gameDex.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "GameDex",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        val games = getSampleGames()
        val columns = if (isLandscape) 3 else 2

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(games) { game ->
                GameCard(
                    game = game,
                    onClick = {
                        navController.navigate(Routes.Detail(gameId = game.id))
                    }
                )
            }
        }
    }
}

@Composable
fun GameCard(
    game: Game,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Imagen del juego
            Image(
                painter = painterResource(id = game.imageRes),
                contentDescription = game.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Degradado oscuro más pronunciado en la parte inferior
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,                // Arriba transparente
                                Color.Black.copy(alpha = 0.3f),   // Medio
                                Color.Black.copy(alpha = 0.85f)   // Abajo muy oscuro
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Rating arriba a la izquierda
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = game.rating.toString(),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Título y plataforma abajo
                Column {
                    Text(
                        text = game.title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = game.platform,
                        color = Color(0xFFC4B5FD),  // Púrpura claro que combina con tu logo
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

data class Game(
    val id: Int,
    val title: String,
    val platform: String,
    val rating: Double,
    val imageRes: Int,
    val description: String,
    val genre: String,
    val releaseYear: Int,
    val developer: String
)

fun getSampleGames(): List<Game> {
    return listOf(
        Game(
            id = 1,
            title = "The Legend of Zelda",
            platform = "Nintendo Switch",
            rating = 9.5,
            imageRes = R.drawable.game_zelda,
            description = "Explora un vasto mundo abierto en esta épica aventura. Advertencia: puede causar adicción severa y olvido de responsabilidades.",
            genre = "Aventura, Acción",
            releaseYear = 2017,
            developer = "Nintendo"
        ),
        Game(
            id = 2,
            title = "God of War",
            platform = "PlayStation 4",
            rating = 9.3,
            imageRes = R.drawable.game_gow,
            description = "Kratos y Atreus en un viaje épico por los reinos nórdicos. Atreus es un buen nombre para un perro, ¿no? 🐕 Justo como se llama el mío... ",
            genre = "Acción, Aventura",
            releaseYear = 2018,
            developer = "Santa Monica Studio"
        ),
        Game(
            id = 3,
            title = "Elden Ring",
            platform = "Multi-plataforma",
            rating = 9.4,
            imageRes = R.drawable.game_elder_ring,
            description = "Un RPG de acción brutal de FromSoftware. Prepárate para morir... muchas veces. Y luego unas cuantas más.",
            genre = "RPG, Acción",
            releaseYear = 2022,
            developer = "FromSoftware"
        ),
        Game(
            id = 4,
            title = "Minecraft",
            platform = "Multi-plataforma",
            rating = 8.9,
            imageRes = R.drawable.game_minecraft,
            description = "Construye, explora y sobrevive en un mundo de bloques infinito. Perfecto para perder horas sin darte cuenta.",
            genre = "Sandbox",
            releaseYear = 2011,
            developer = "Mojang Studios"
        ),
        Game(
            id = 5,
            title = "Hollow Knight",
            platform = "Multi-plataforma",
            rating = 9.1,
            imageRes = R.drawable.game_hollow,
            description = "Un metroidvania desafiante ambientado en un reino de insectos. Los bichos nunca fueron tan adorables... ni tan mortales.",
            genre = "Metroidvania",
            releaseYear = 2017,
            developer = "Team Cherry"
        ),
        Game(
            id = 6,
            title = "Stardew Valley",
            platform = "Multi-plataforma",
            rating = 9.0,
            imageRes = R.drawable.game_stardew,
            description = "Gestiona tu granja, socializa con aldeanos y olvídate del mundo real. La terapia más barata que existe.",
            genre = "Simulación",
            releaseYear = 2016,
            developer = "ConcernedApe"
        )
    )
}