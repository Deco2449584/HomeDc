package com.domocodetech.homedc.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.absoluteValue

@Composable
fun ImageCarousel(
    imageUrls: List<String>,
    movieTitles: List<String>,
) {
    val middleIndex = imageUrls.size / 2
    val pagerState = rememberPagerState(initialPage = middleIndex)

    Column {
        HorizontalPager(
            count = imageUrls.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 150.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            val painter = rememberAsyncImagePainter(model = imageUrls[page])
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = (pagerState.currentPage - page + pagerState.currentPageOffset).absoluteValue
                        scaleX = 1f - pageOffset * 0.1f
                        scaleY = 1f - pageOffset * 0.1f
                        alpha = 1f - pageOffset * 0.3f
                    }
                    .clip(RoundedCornerShape(5.dp))
            )
        }

        if (pagerState.currentPage < movieTitles.size) {
            Text(
                text = movieTitles[pagerState.currentPage],
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }

}




