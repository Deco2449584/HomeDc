package com.domocodetech.homedc.getstarted

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
    // Collecting state from the ViewModel
    val images by viewModel.images.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var currentDescription by remember { mutableStateOf("") }
    var currentPage by remember { mutableIntStateOf(0) }

    // Animation for the background gradient
    val infiniteTransition = rememberInfiniteTransition(label = "")
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
        // Loading indicator
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

        // Main content when not loading and images are available
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

                // App description
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

                // Improved animated description
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                ) {
                    AnimatedContent(
                        targetState = currentDescription,
                        transitionSpec = {
                            (slideInVertically { height -> height } + fadeIn() with
                                    slideOutVertically { height -> -height } + fadeOut()).using(
                                SizeTransform(clip = false)
                            )
                        }, label = ""
                    ) { description ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.animateContentSize()
                            )
                        }
                    }
                }

                // Animated dots indicator
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    images.forEachIndexed { index, _ ->
                        val dotSize by animateDpAsState(
                            targetValue = if (index == currentPage) 12.dp else 8.dp, label = ""
                        )
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(dotSize)
                                .clip(RoundedCornerShape(50))
                                .background(
                                    if (index == currentPage)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                                .animateContentSize()
                        )
                    }
                }

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

        // Message when no images are available
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