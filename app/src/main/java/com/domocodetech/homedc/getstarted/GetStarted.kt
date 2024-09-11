package com.domocodetech.homedc.getstarted

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.domocodetech.homedc.commons.AnimatedButton
import com.domocodetech.homedc.commons.ImageCarousel

@Composable
fun GetStartedScreen(navController: NavController, viewModel: ImageViewModel = hiltViewModel()) {
    val images by viewModel.images.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var currentDescription by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.primary
                    )
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else if (images.isNotEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "AutoDirectorio",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "¡Encuentra, vende y compra vehículos de manera rápida y segura!. " +
                            "Descubre las mejores ofertas en vehículos nuevos y usados.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                ImageCarousel(
                    imageUrls = images.map { it.url },
                    movieTitles = images.map { it.name },
                    onPageChanged = { page ->
                        currentDescription = images[page].description
                    }
                )

                Text(
                    text = currentDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.background,
                    maxLines = 6
                )
                Spacer(modifier = Modifier.height(16.dp))
                AnimatedButton(
                    text = "Get Started",
                    onClickAction = { navController.navigate("login") },
                    textColor = Color.White,
                    backgroundColors = listOf(Color(0xFF000000), Color(0xFF00BCD4)),
                )
            }
        } else {
            Text(
                text = "No images available",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}