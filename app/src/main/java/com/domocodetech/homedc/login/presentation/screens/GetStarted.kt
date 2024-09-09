package com.domocodetech.homedc.login.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.domocodetech.homedc.commons.AnimatedButton
import com.domocodetech.homedc.commons.ImageCarousel
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.absoluteValue

@Composable
fun GetStartedScreen(navController: NavController) {
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
                text = "Cinema Ticket Booking",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Lista de URLs de las imágenes (ejemplo de coches)
            val imageUrls = listOf(
                "https://i.pinimg.com/736x/73/32/25/733225a9e7d62bdaa5196dfcaef18262.jpg",
                "https://i.pinimg.com/736x/73/32/25/733225a9e7d62bdaa5196dfcaef18262.jpg",
                "https://i.pinimg.com/736x/73/32/25/733225a9e7d62bdaa5196dfcaef18262.jpg",
                "https://i.pinimg.com/736x/73/32/25/733225a9e7d62bdaa5196dfcaef18262.jpg",
                "https://i.pinimg.com/736x/73/32/25/733225a9e7d62bdaa5196dfcaef18262.jpg",
                "https://i.pinimg.com/736x/73/32/25/733225a9e7d62bdaa5196dfcaef18262.jpg",
                "https://i.pinimg.com/736x/73/32/25/733225a9e7d62bdaa5196dfcaef18262.jpg",


            )

            // Lista de títulos de las imágenes
            val movieTitles = listOf(
                "BMW 4-Series",
                "Peugeot e-2008",
                "Car Trail",
                "Car Sport",
                "Moto Custom"
            )

            ImageCarousel(
                imageUrls = imageUrls,
                movieTitles = movieTitles, // Pasa los títulos también
            )
            Spacer(modifier = Modifier.height(8.dp))

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



