package com.domocodetech.homedc.login.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay

@Composable
fun GetStartedScreen(navController: NavController) {
    val imageUrls = listOf(
        "https://media.gq.com.mx/photos/604458fef0cc2a1d8969755c/16:9/w_2560%2Cc_limit/10%2520autos%2520que%2520quieren%2520ser%2520el%2520mejor%2520de%25202021%2520-%2520BMW%25204-Series%25202021%2520(1).jpg",
        "https://www.autonocion.com/wp-content/uploads/2020/08/Prueba-Peugeot-e-2008-Allure-2020-8.jpg",
        "https://www.pontgrup.com/wp-content/uploads/2018/01/motos-trail.jpg",
        "https://www.galgo.com/wp-content/uploads/2023/05/tipos-de-motos-deportivas.jpg"
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D47A1), Color(0xFF42A5F5))
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Carros y Motos",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )

            ImageCarousel(imageUrls)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                Text(
                    text = "Get Started",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(0xFF0D47A1),
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun ImageCarousel(imageUrls: List<String>) {
    var currentIndex by remember { mutableStateOf(0) }
    val transition = updateTransition(currentIndex, label = "Image Carousel Transition")
    val scale by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        },
        label = "Scale Animation"
    ) { index ->
        if (index == currentIndex) 1.1f else 0.9f
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentIndex = (currentIndex + 1) % imageUrls.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        imageUrls.forEachIndexed { index, imageUrl ->
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .scale(if (index == currentIndex) scale else 0.9f)
                    .graphicsLayer {
                        alpha = if (index == currentIndex) 1f else 0.6f
                        translationX = if (index == currentIndex) 0f else 40f
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { currentIndex = index })
                    }
                    .size(280.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GetStartedScreenPreview() {
    GetStartedScreen(navController = rememberNavController())
}
