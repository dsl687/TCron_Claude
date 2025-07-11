package com.tcron.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = TCronTeal80,
    secondary = TCronTealGrey80,
    tertiary = TCronGreen80,
    background = TCronDarkBackground,
    surface = TCronDarkSurface,
    onPrimary = TCronDarkBackground,
    onSecondary = TCronDarkBackground,
    onTertiary = TCronDarkBackground,
    onBackground = TCronDarkOnSurface,
    onSurface = TCronDarkOnSurface,
    surfaceVariant = TCronDarkCard,
    onSurfaceVariant = TCronDarkOnSurface
)

private val LightColorScheme = lightColorScheme(
    primary = TCronTeal40,
    secondary = TCronTealGrey40,
    tertiary = TCronGreen40,
    background = TCronLightBackground,
    surface = TCronLightSurface,
    onPrimary = TCronLightBackground,
    onSecondary = TCronLightBackground,
    onTertiary = TCronLightBackground,
    onBackground = TCronLightOnSurface,
    onSurface = TCronLightOnSurface,
    surfaceVariant = TCronLightCard,
    onSurfaceVariant = TCronLightOnSurface
)

@Composable
fun TCronTheme(
    themeMode: com.tcron.core.common.ThemeMode = com.tcron.core.common.ThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val systemInDarkTheme = isSystemInDarkTheme()
    
    val darkTheme = when (themeMode) {
        com.tcron.core.common.ThemeMode.LIGHT -> false
        com.tcron.core.common.ThemeMode.DARK -> true
        com.tcron.core.common.ThemeMode.SYSTEM -> systemInDarkTheme
    }
    
    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}