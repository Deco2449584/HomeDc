package com.domocodetech.homedc.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val ModernDarkColorScheme = darkColorScheme(
    primary = Blue700,
    onPrimary = White,
    primaryContainer = Blue500,
    onPrimaryContainer = White,
    secondary = Teal700,
    onSecondary = White,
    secondaryContainer = Teal500,
    onSecondaryContainer = White,
    tertiary = Purple700,
    onTertiary = White,
    tertiaryContainer = Purple500,
    onTertiaryContainer = White,
    background = BackgroundDark,
    onBackground = White,
    surface = SurfaceDark,
    onSurface = White,
    error = Red700,
    onError = White
)

private val ModernLightColorScheme = lightColorScheme(
    primary = Blue500,
    onPrimary = White,
    primaryContainer = Blue200,
    onPrimaryContainer = Black,
    secondary = Teal500,
    onSecondary = Black,
    secondaryContainer = Teal200,
    onSecondaryContainer = Black,
    tertiary = Purple500,
    onTertiary = Black,
    tertiaryContainer = Purple200,
    onTertiaryContainer = Black,
    background = BackgroundLight,
    onBackground = Black,
    surface = SurfaceLight,
    onSurface = Black,
    error = Red500,
    onError = White
)

@Composable
fun HomeDcTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> ModernDarkColorScheme
        else -> ModernLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CustomTypography,
        content = content
    )
}