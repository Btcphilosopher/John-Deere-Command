package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = DeereGreen,
  onPrimary = Color.White,
  primaryContainer = DeereGreenDark,
  onPrimaryContainer = DeereTextPrimary,
  secondary = DeereYellow,
  onSecondary = Color.Black,
  secondaryContainer = DeereYellowMuted,
  onSecondaryContainer = Color.Black,
  tertiary = StatusWorkingGreen,
  background = CockpitBackground,
  onBackground = DeereTextPrimary,
  surface = CockpitSurface,
  onSurface = DeereTextPrimary,
  surfaceVariant = CockpitSurfaceVariant,
  onSurfaceVariant = DeereTextSecondary,
  outline = CockpitBorder,
  error = StatusErrorRed,
  onError = Color.White
)

private val LightColorScheme = lightColorScheme(
  primary = DeereGreen,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFE2F3DF),
  onPrimaryContainer = DeereGreenDark,
  secondary = DeereYellowMuted,
  onSecondary = Color.Black,
  tertiary = DeereGreenBright,
  background = Color(0xFFF4F6F4),
  onBackground = Color(0xFF101610),
  surface = Color.White,
  onSurface = Color(0xFF101610),
  surfaceVariant = Color(0xFFE5EAE5),
  onSurfaceVariant = Color(0xFF424D42),
  outline = Color(0xFFB0BEB0),
  error = StatusErrorRed,
  onError = Color.White
)

@Composable
fun JohnDeereTheme(
  darkTheme: Boolean = true, // Default to cockpit dark mode for tractor tablet cab experience
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

