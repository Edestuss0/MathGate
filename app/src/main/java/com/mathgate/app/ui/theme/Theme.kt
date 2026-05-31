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
    primary                = Gray80,
    onPrimary              = Color(0xFF111111),
    primaryContainer       = Color(0xFF242424),
    onPrimaryContainer     = Color(0xFFF5F5F5),
    secondary              = Neutral80,
    onSecondary            = Color(0xFF111111),
    secondaryContainer     = Color(0xFF1C1C1C),
    onSecondaryContainer   = Color(0xFFE5E7EB),
    tertiary               = Dark80,
    onTertiary             = Color(0xFF111111),
    tertiaryContainer      = Color(0xFF262626),
    onTertiaryContainer    = Color(0xFFF5F5F5),
    background             = BackgroundDark,
    onBackground           = OnDark,
    surface                = SurfaceDark,
    onSurface              = OnDark,
    surfaceVariant         = SurfaceVariantDark,
    onSurfaceVariant       = OnDarkMuted,
    outline                = BorderDark,
    outlineVariant         = Color(0xFF202020)
)

private val LightColorScheme = lightColorScheme(
    primary                = Gray40,
    onPrimary              = Color.White,
    primaryContainer       = Color(0xFFEAEAEA),
    onPrimaryContainer     = Color(0xFF111111),
    secondary              = Neutral40,
    onSecondary            = Color.White,
    secondaryContainer     = Color(0xFFF1F1F1),
    onSecondaryContainer   = Color(0xFF222222),
    tertiary               = Dark40,
    onTertiary             = Color.White,
    tertiaryContainer      = Color(0xFFE5E5E5),
    onTertiaryContainer    = Color(0xFF111111),
    background             = BackgroundLight,
    onBackground           = OnLight,
    surface                = SurfaceLight,
    onSurface              = OnLight,
    surfaceVariant         = SurfaceVariantLight,
    onSurfaceVariant       = OnLightMuted,
    outline                = BorderLight,
    outlineVariant         = Color(0xFFEAEAEA)
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