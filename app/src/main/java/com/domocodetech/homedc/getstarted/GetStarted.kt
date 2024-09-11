package com.domocodetech.homedc.getstarted

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.domocodetech.homedc.R
import com.domocodetech.homedc.commons.AnimatedButton
import com.domocodetech.homedc.commons.ImageCarousel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GetStartedScreen(
    navController: NavController,
    viewModel: ImageViewModel = hiltViewModel()
) {
    val images by viewModel.images.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var currentDescription by remember { mutableStateOf("") }
    var currentPage by remember { mutableStateOf(0) }

    // Animation for the background gradient
    val infiniteTransition = rememberInfiniteTransition()
    val backgroundAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = backgroundAlpha)
                    )
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        }

        AnimatedVisibility(
            visible = !isLoading && images.isNotEmpty(),
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Animated title
                var titleVisible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    titleVisible = true
                }
                AnimatedVisibility(
                    visible = titleVisible,
                    enter = fadeIn() + expandVertically(expandFrom = Alignment.Top)
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.graphicsLayer(
                            shadowElevation = 8f,
                            shape = RoundedCornerShape(8.dp)
                        )
                    )
                }

                Text(
                    text = stringResource(R.string.app_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                // Animated ImageCarousel
                var carouselVisible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    carouselVisible = true
                }
                AnimatedVisibility(
                    visible = carouselVisible,
                    enter = fadeIn() + expandIn(expandFrom = Alignment.Center)
                ) {
                    ImageCarousel(
                        imageUrls = images.map { it.url },
                        movieTitles = images.map { it.name },
                        onPageChanged = { page ->
                            currentDescription = images[page].description
                            currentPage = page
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .graphicsLayer(
                                shadowElevation = 16f
                            )
                    )
                }

                // Animated description
                AnimatedContent(
                    targetState = currentDescription,
                    transitionSpec = {
                        fadeIn() + slideInVertically { height -> height } with
                                fadeOut() + slideOutVertically { height -> -height }
                    }, label = ""
                ) { description ->
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Animated progress indicator
                LinearProgressIndicator(
                    progress = (currentPage + 1) / images.size.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .animateContentSize()
                )

                Spacer(modifier = Modifier.weight(1f))

                // Pulsating button animation
                val scale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.05f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000),
                        repeatMode = RepeatMode.Reverse
                    ), label = ""
                )

                AnimatedButton(
                    text = stringResource(R.string.get_started),
                    onClickAction = { navController.navigate("login") },
                    textColor = Color.White,
                    backgroundColors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer(scaleX = scale, scaleY = scale)
                )
            }
        }

        AnimatedVisibility(
            visible = !isLoading && images.isEmpty(),
            enter = fadeIn() + expandIn(),
            exit = fadeOut() + shrinkOut()
        ) {
            Text(
                text = stringResource(R.string.no_images_available),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}