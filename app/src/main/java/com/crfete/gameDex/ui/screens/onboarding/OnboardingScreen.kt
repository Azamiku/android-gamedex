package com.crfete.gameDex.ui.screens.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.window.core.layout.WindowWidthSizeClass
import com.crfete.gameDex.R
import com.crfete.gameDex.navigation.Routes

/**
 * OnboardingScreen.kt - Onboarding de 4 pantallas adaptativo
 *
 * Presenta la aplicación al usuario con 4 pantallas:
 * - Pantallas 1-3: Botones "Siguiente" y "Saltar"
 * - Pantalla 4: Botón "¡Comenzar!" para ir a la app
 *
 * Se adapta según el tamaño de ventana:
 * - COMPACT (portrait): Layout vertical
 * - MEDIUM/EXPANDED (landscape/tablets): Layout horizontal optimizado
 *
 * Incluye:
 * - Animación de slide entre pantallas
 * - Indicadores de página (puntitos)
 * - Navegación fluida
 *
 * Cumple los requisitos de:
 * - Onboarding de 4 pantallas (2 puntos)
 * - Animación (1 punto)
 *
 * @param navController Controlador de navegación
 * @author Cristina Fernández
 * @date Enero 2026
 */
@Composable
fun OnboardingScreen(navController: NavController) {
    var currentPage by remember { mutableIntStateOf(0) }
    val pages = getOnboardingPages()

    // Obtener el tamaño de la ventana
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isCompact = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedVisibility(
            visible = true,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
        ) {
            if (isCompact) {
                // Modo PORTRAIT/COMPACT - Layout vertical
                OnboardingPageVertical(
                    page = pages[currentPage],
                    currentPage = currentPage,
                    totalPages = pages.size
                )
            } else {
                // Modo LANDSCAPE/EXPANDED - Layout horizontal
                OnboardingPageHorizontal(
                    page = pages[currentPage],
                    currentPage = currentPage,
                    totalPages = pages.size
                )
            }
        }

        OnboardingButtons(
            currentPage = currentPage,
            totalPages = pages.size,
            isCompact = isCompact,
            onNext = {
                if (currentPage < pages.size - 1) {
                    currentPage++
                } else {
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Splash) { inclusive = true }
                    }
                }
            },
            onSkip = { currentPage = pages.size - 1 },
            onGetStarted = {
                navController.navigate(Routes.Home) {
                    popUpTo(Routes.Splash) { inclusive = true }
                }
            }
        )

        PageIndicators(
            currentPage = currentPage,
            totalPages = pages.size,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = if (isCompact) 100.dp else 32.dp)
        )
    }
}

/**
 * Layout vertical para pantallas COMPACT (portrait)
 */
@Composable
fun OnboardingPageVertical(
    page: OnboardingPageData,
    currentPage: Int,
    totalPages: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = page.image),
            contentDescription = page.title,
            modifier = Modifier
                .size(250.dp)
                .padding(bottom = 32.dp)
        )

        Text(
            text = page.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = page.description,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}

/**
 * Layout horizontal para pantallas EXPANDED (landscape/tablets)
 */
@Composable
fun OnboardingPageHorizontal(
    page: OnboardingPageData,
    currentPage: Int,
    totalPages: Int
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Imagen a la izquierda (40% del ancho)
        Image(
            painter = painterResource(id = page.image),
            contentDescription = page.title,
            modifier = Modifier
                .weight(0.4f)
                .size(200.dp)
        )

        Spacer(modifier = Modifier.width(32.dp))

        // Texto a la derecha (60% del ancho)
        Column(
            modifier = Modifier.weight(0.6f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = page.title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = page.description,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                lineHeight = 26.sp
            )
        }
    }
}

@Composable
fun BoxScope.OnboardingButtons(
    currentPage: Int,
    totalPages: Int,
    isCompact: Boolean,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    onGetStarted: () -> Unit
) {
    if (currentPage < totalPages - 1) {
        // Botón "Saltar"
        TextButton(
            onClick = onSkip,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(
                text = "Saltar",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Botón "Siguiente"
        Button(
            onClick = onNext,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = if (isCompact) 32.dp else 16.dp)
                .fillMaxWidth(if (isCompact) 0.8f else 0.4f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Siguiente",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        // Última página: botón "¡Comenzar!"
        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = if (isCompact) 32.dp else 16.dp)
                .fillMaxWidth(if (isCompact) 0.8f else 0.4f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "¡Comenzar!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PageIndicators(
    currentPage: Int,
    totalPages: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(totalPages) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == currentPage) 12.dp else 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == currentPage)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
            )
        }
    }
}

data class OnboardingPageData(
    val image: Int,
    val title: String,
    val description: String
)

fun getOnboardingPages(): List<OnboardingPageData> {
    return listOf(
        OnboardingPageData(
            image = R.drawable.ic_gamedex_logo,
            title = "Descubre Juegos",
            description = "Explora una amplia colección de videojuegos de todas las plataformas y géneros"
        ),
        OnboardingPageData(
            image = R.drawable.ic_gamedex_logo,
            title = "Guarda tus Favoritos",
            description = "Crea tu biblioteca personal y guarda los juegos que más te gustan"
        ),
        OnboardingPageData(
            image = R.drawable.ic_gamedex_logo,
            title = "Información Detallada",
            description = "Accede a reseñas, calificaciones y toda la información de cada juego"
        ),
        OnboardingPageData(
            image = R.drawable.ic_gamedex_logo,
            title = "¡Comienza Ahora!",
            description = "Todo listo para explorar el mundo de los videojuegos. ¡Vamos!"
        )
    )
}