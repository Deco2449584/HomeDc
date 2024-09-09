package com.domocodetech.homedc.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.absoluteValue

@Composable
fun ImageCarousel(
    imageUrls: List<String>,
    movieTitles: List<String>,
) {
    val pagerState = rememberPagerState()


        Column {
            HorizontalPager(
                count = imageUrls.size,
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 48.dp), // Ajustar el padding para mostrar imágenes adyacentes
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Ajustar la altura para las imágenes
            ) { page ->
                val painter = rememberAsyncImagePainter(model = imageUrls[page])
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .graphicsLayer {
                            // Cálculo del desplazamiento de la página
                            val pageOffset =
                                (pagerState.currentPage - page + pagerState.currentPageOffset).absoluteValue
                            scaleX = 1f - pageOffset * 0.1f // Escalado más sutil
                            scaleY = 1f - pageOffset * 0.1f
                            alpha = 1f - pageOffset * 0.3f // Opacidad ajustada
                        }
                        .clip(RoundedCornerShape(40.dp)) // Redondear las esquinas
                )
            }

            // Título de la película correspondiente
            if (pagerState.currentPage < movieTitles.size) {
                Text(
                    text = movieTitles[pagerState.currentPage],
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Indicadores de página en la parte inferior
        Box(
            modifier = Modifier
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




