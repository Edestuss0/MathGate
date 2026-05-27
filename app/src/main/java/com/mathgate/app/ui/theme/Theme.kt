package com.mathgate.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary                = Indigo80,
    onPrimary              = Color(0xFF0D0F2E),
    primaryContainer       = Color(0xFF1E2050),
    onPrimaryContainer     = Color(0xFFD0D3FF),
    secondary              = Slate80,
    onSecondary            = Color(0xFF0F1520),
    secondaryContainer     = Color(0xFF1E2531),
    onSecondaryContainer   = Color(0xFFDDE4EE),
    tertiary               = Emerald80,
    onTertiary             = Color(0xFF022016),
    tertiaryContainer      = Color(0xFF0A2E22),
    onTertiaryContainer    = Color(0xFFB0F0D8),
    background             = BackgroundDark,
    onBackground           = OnDark,
    surface                = SurfaceDark,
    onSurface              = OnDark,
    surfaceVariant         = SurfaceVariantDark,
    onSurfaceVariant       = OnDarkMuted,
    outline                = BorderDark,
    outlineVariant         = Color(0xFF1E2531)
)

private val LightColorScheme = lightColorScheme(
    primary                = Indigo40,
    onPrimary              = Color.White,
    primaryContainer       = Color(0xFFE6E8FF),
    onPrimaryContainer     = Color(0xFF1A1D72),
    secondary              = Slate40,
    onSecondary            = Color.White,
    secondaryContainer     = Color(0xFFEEF0F6),
    onSecondaryContainer   = Color(0xFF2D3A50),
    tertiary               = Emerald40,
    onTertiary             = Color.White,
    tertiaryContainer      = Color(0xFFD6F5EA),
    onTertiaryContainer    = Color(0xFF053D28),
    background             = BackgroundLight,
    onBackground           = OnLight,
    surface                = SurfaceLight,
    onSurface              = OnLight,
    surfaceVariant         = SurfaceVariantLight,
    onSurfaceVariant       = OnLightMuted,
    outline                = BorderLight,
    outlineVariant         = Color(0xFFE8EDF5)
)

@Composable
fun MathGateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
        typography  = Typography,
        content     = content
    )
}