package com.domocodetech.homedc.login.presentation.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

// Update AnimatedLogoAndTitle to allow customization of its position
@Composable
fun AnimatedLogoAndTitle(
    logoResId: Int,
    titleText: String,
    modifier: Modifier = Modifier
) {
    var displayedTitleText by remember { mutableStateOf("") }
    val logoOffset = remember { Animatable(0f) }

    // Animation for typing effect
    LaunchedEffect(titleText) {
        for (i in titleText.indices) {
            displayedTitleText = titleText.substring(0, i + 1)
            delay(100)
        }
    }

    // Animation for logo movement
    LaunchedEffect(Unit) {
        logoOffset.animateTo(
            targetValue = 100f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2000),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = logoResId),
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
                .offset { IntOffset(0, logoOffset.value.toInt()) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = displayedTitleText,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}