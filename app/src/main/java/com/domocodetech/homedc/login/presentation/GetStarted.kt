package com.domocodetech.homedc.login.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun GetStartedScreen(navController: NavController) {

    val imageUrls = listOf(
        "https://media.gq.com.mx/photos/604458fef0cc2a1d8969755c/16:9/w_2560%2Cc_limit/10%2520autos%2520que%2520quieren%2520ser%2520el%2520mejor%2520de%25202021%2520-%2520BMW%25204-Series%25202021%2520(1).jpg",
        "https://www.autonocion.com/wp-content/uploads/2020/08/Prueba-Peugeot-e-2008-Allure-2020-8.jpg",
        "https://www.pontgrup.com/wp-content/uploads/2018/01/motos-trail.jpg",
        "https://www.galgo.com/wp-content/uploads/2023/05/tipos-de-motos-deportivas.jpg"
    )
        ImageCarousel(imageUrls = imageUrls, modifier = Modifier.fillMaxSize())

}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageCarousel(imageUrls: List<String>, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {
        HorizontalPager(
            count = imageUrls.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            val painter = rememberAsyncImagePainter(model = imageUrls[page])
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            items(imageUrls.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 12.dp else 8.dp)
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                            shape = CircleShape
                        )
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                        .padding(4.dp)
                )
            }
        }
    }
}