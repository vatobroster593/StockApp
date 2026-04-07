package com.stockapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary          = Purple40,
    onPrimary        = Neutral99,
    primaryContainer = Purple90,
    onPrimaryContainer = Purple10,
    secondary        = Rose40,
    onSecondary      = Neutral99,
    secondaryContainer = Rose90,
    onSecondaryContainer = Rose10,
    background       = Neutral99,
    onBackground     = Neutral10,
    surface          = Neutral95,
    onSurface        = Neutral10,
    surfaceVariant   = NeutralVariant80,
    onSurfaceVariant = NeutralVariant30,
    error            = ErrorRed,
    outline          = NeutralVariant50
)

private val DarkColorScheme = darkColorScheme(
    primary          = Purple80,
    onPrimary        = Purple20,
    primaryContainer = Purple30,
    onPrimaryContainer = Purple90,
    secondary        = Rose80,
    onSecondary      = Rose20,
    secondaryContainer = Rose30,
    onSecondaryContainer = Rose90,
    background       = Neutral10,
    onBackground     = Neutral90,
    surface          = Neutral20,
    onSurface        = Neutral90,
    surfaceVariant   = NeutralVariant30,
    onSurfaceVariant = NeutralVariant80,
    error            = Rose80,
    outline          = NeutralVariant50
)

@Composable
fun StockAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
