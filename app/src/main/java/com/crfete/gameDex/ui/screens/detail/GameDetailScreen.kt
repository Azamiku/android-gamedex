/**
 * GameDetailScreen.kt - Pantalla de detalle del juego seleccionado
 *
 * Muestra información completa del videojuego:
 * - Imagen destacada con animación de zoom suave
 * - Título con rating en la misma línea
 * - Plataforma del juego
 * - Cards informativas con iconos: género, año, desarrollador
 * - Descripción completa del juego
 *
 * Soporta dos orientaciones:
 * - Portrait: Layout vertical con imagen arriba (300dp) + info abajo
 * - Landscape: Layout horizontal con imagen izquierda (40%) + info derecha (60%)
 *
 * Animaciones incluidas:
 * - Zoom infinito en la imagen (1.0 a 1.05 en 2 segundos)
 * - La imagen tiene clip para no salirse del contenedor
 *
 * Cumple los requisitos de:
 * - Dos pantallas con paso de información
 * - Versiones portrait y landscape
 * - Animación de escala en la imagen
 *
 * @param navController Controlador de navegación para volver atrás
 * @param gameId ID del juego a mostrar (pasado desde HomeScreen)
 * @author Cristina Fernández
 * @date Enero 2026
 */



package com.crfete.gameDex.ui.screens.detail

import android.content.res.Configuration
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.crfete.gameDex.ui.screens.home.getSampleGames

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(
    navController: NavController,
    gameId: Int
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val game = getSampleGames().find { it.id == gameId }

    if (game == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Juego no encontrado")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        if (isLandscape) {
            GameDetailLandscape(game = game, paddingValues = paddingValues)
        } else {
            GameDetailPortrait(game = game, paddingValues = paddingValues)
        }
    }
}

/**
 * Versión portrait del detalle del juego
 * Layout vertical: Imagen arriba, información abajo
 */
@Composable
fun GameDetailPortrait(
    game: com.crfete.gameDex.ui.screens.home.Game,
    paddingValues: PaddingValues
) {
    // Animación de zoom en la imagen
    val infiniteTransition = rememberInfiniteTransition(label = "scale")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        // Box de la imagen con clip para que no se salga del contenedor
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RectangleShape)
        ) {
            // Imagen con animación de zoom
            Image(
                painter = painterResource(id = game.imageRes),
                contentDescription = game.title,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale),
                contentScale = ContentScale.Crop
            )

            // Gradiente oscuro sobre la imagen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )
        }

        // Información del juego
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Título y Rating en la misma fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = game.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )

                // Rating con estrella
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = game.rating.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Plataforma
            Text(
                text = game.platform,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Cards de información con iconos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoCard(
                    title = "Género",
                    value = game.genre,
                    icon = Icons.Default.Info,  // ℹ️ Icono de información
                    modifier = Modifier.weight(1f)
                )
                InfoCard(
                    title = "Año",
                    value = game.releaseYear.toString(),
                    icon = Icons.Default.DateRange,  // 📅 Icono de calendario
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card de desarrollador con icono
            InfoCard(
                title = "Desarrollador",
                value = game.developer,
                icon = Icons.Default.Build,  // 🔧 Icono de herramientas
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Descripción
            Text(
                text = "Descripción",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = game.description,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                lineHeight = 24.sp
            )
        }
    }
}

/**
 * Versión landscape del detalle del juego
 * Layout horizontal: Imagen a la izquierda, información a la derecha
 */
@Composable
fun GameDetailLandscape(
    game: com.crfete.gameDex.ui.screens.home.Game,
    paddingValues: PaddingValues
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        // Imagen del juego (40% del ancho)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.4f)
        ) {
            Image(
                painter = painterResource(id = game.imageRes),
                contentDescription = game.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Información del juego (60% del ancho)
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.6f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Título y Rating en la misma fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = game.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                // Rating con estrella
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = game.rating.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Plataforma
            Text(
                text = game.platform,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Cards de información con iconos
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoCard(
                    title = "Género",
                    value = game.genre,
                    icon = Icons.Default.Info,  // ℹ️ Icono de información
                    modifier = Modifier.weight(1f)
                )
                InfoCard(
                    title = "Año",
                    value = game.releaseYear.toString(),
                    icon = Icons.Default.DateRange,  // 📅 Icono de calendario
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Card de desarrollador con icono
            InfoCard(
                title = "Desarrollador",
                value = game.developer,
                icon = Icons.Default.Build,  // 🔧 Icono de herramientas
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción
            Text(
                text = "Descripción",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = game.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                lineHeight = 20.sp
            )
        }
    }
}

/**
 * Card de información
 *
 * Muestra un título, valor e icono en una tarjeta.
 * El icono aparece a la izquierda del texto.
 *
 * @param title Etiqueta del campo (ejem: "Género")
 * @param value Valor del campo (ejem: "Acción, Aventura")
 * @param icon Icono
 * @param modifier Modificador para la personalización
 */
@Composable
fun InfoCard(
    title: String,
    value: String,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            // Texto (título y valor)
            Column {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                Text(
                    text = value,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}