package com.domocodetech.homedc.getstarted

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.domocodetech.homedc.commons.AnimatedButton
import com.domocodetech.homedc.commons.ImageCarousel

@Composable
fun GetStartedScreen(navController: NavController, viewModel: ImageViewModel = hiltViewModel()) {
    val images by viewModel.images.collectAsState()

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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Your Cars Collection",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))

            ImageCarousel(
                imageUrls = images.map { it.url },
                movieTitles = images.map { it.name }
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Discover and book the latest movies at your favorite cinemas Easy and fast!",
                fontSize = 14.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(24.dp))
            AnimatedButton(
                text = "Get Started",
                onClickAction = { navController.navigate("login") },
                textColor = Color.White,
                backgroundColors = listOf(Color(0xFF000000), Color(0xFF00BCD4)),
            )
        }
    }
}