package com.domocodetech.homedc.login.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
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
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF020202), Color(0xFF42A5F5))
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AppTitle()
            Spacer(modifier = Modifier.height(16.dp))
            val imageUrls = listOf(
                "https://media.gq.com.mx/photos/604458fef0cc2a1d8969755c/16:9/w_2560%2Cc_limit/10%2520autos%2520que%2520quieren%2520ser%2520el%2520mejor%2520de%25202021%2520-%2520BMW%25204-Series%25202021%2520(1).jpg",
                "https://www.autonocion.com/wp-content/uploads/2020/08/Prueba-Peugeot-e-2008-Allure-2020-8.jpg",
                "https://www.pontgrup.com/wp-content/uploads/2018/01/motos-trail.jpg",
                "https://www.galgo.com/wp-content/uploads/2023/05/tipos-de-motos-deportivas.jpg"
            )
            ImageCarousel(imageUrls = imageUrls, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            GetStartedButton(navController)
        }
    }
}

@Composable
fun AppTitle() {
    Text(
        text = "MotoCar Hub",
        fontSize = 32.sp,
        color = Color.White,
    )
}

@Composable
fun ImageCarousel(imageUrls: List<String>, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        HorizontalPager(
            count = imageUrls.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 48.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) { page ->
            val painter = rememberAsyncImagePainter(model = imageUrls[page])
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
                    .padding(8.dp)
                    .graphicsLayer {
                        val pageOffset = (pagerState.currentPage - page + pagerState.currentPageOffset).absoluteValue
                        scaleX = 1f - pageOffset * 0.2f
                        scaleY = 1f - pageOffset * 0.2f
                        alpha = 1f - pageOffset * 0.3f
                        shadowElevation = 8.dp.toPx()
                    }
            )
        }

        // Indicadores de página en la parte inferior
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp)
        ) {
            HorizontalPagerIndicator(
                pagerState = pagerState,
                activeColor = Color.White,
                inactiveColor = Color.Gray,
                indicatorWidth = 8.dp,
                spacing = 8.dp
            )
        }
    }
}


@Composable
fun GetStartedButton(navController: NavController) {
    Button(
        onClick = { navController.navigate("login") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(text = "Get Started")
    }
}