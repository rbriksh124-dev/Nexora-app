package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NexoraDarkColorScheme = darkColorScheme(
  primary = ElectricPurple,
  onPrimary = Color.White,
  primaryContainer = ElectricPurpleDark,
  onPrimaryContainer = ElectricPurpleLight,
  secondary = VibrantCyan,
  onSecondary = Color.Black,
  secondaryContainer = VibrantCyanDark,
  onSecondaryContainer = VibrantCyanLight,
  tertiary = EmeraldAccent,
  background = NexoraDeepBlack,
  onBackground = TextPrimaryDark,
  surface = NexoraMidnightNavy,
  onSurface = TextPrimaryDark,
  surfaceVariant = NexoraSurfaceDark,
  onSurfaceVariant = TextSecondaryDark,
  outline = NexoraBorderDark,
  error = RoseAccent,
  onError = Color.White
)

private val NexoraLightColorScheme = lightColorScheme(
  primary = ElectricPurpleDark,
  onPrimary = Color.White,
  primaryContainer = ElectricPurpleLight,
  onPrimaryContainer = NexoraDeepBlack,
  secondary = VibrantCyanDark,
  onSecondary = Color.White,
  secondaryContainer = VibrantCyanLight,
  onSecondaryContainer = Color.Black,
  tertiary = EmeraldAccent,
  background = NexoraLightBackground,
  onBackground = TextPrimaryLight,
  surface = NexoraLightSurface,
  onSurface = TextPrimaryLight,
  surfaceVariant = NexoraLightSurfaceVariant,
  onSurfaceVariant = TextSecondaryLight,
  outline = NexoraLightBorder,
  error = RoseAccent,
  onError = Color.White
)

@Composable
fun NexoraTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) NexoraDarkColorScheme else NexoraLightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
