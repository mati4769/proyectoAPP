package com.example.guia233.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AzulOscuro,
    onPrimary = Color.White,
    primaryContainer = AzulClaro,
    onPrimaryContainer = AzulOscuro,
    secondary = Turquesa,
    onSecondary = Color.White,
    secondaryContainer = TurquesaClaro,
    onSecondaryContainer = Color(0xFF00463F),
    background = FondoClaro,
    surface = Color.White,
    surfaceVariant = Color(0xFFE8EEF5)
)

private val DarkColorScheme = darkColorScheme(
    primary = AzulNoche,
    onPrimary = Color(0xFF0A2540),
    primaryContainer = AzulNocheContenedor,
    onPrimaryContainer = AzulClaro,
    secondary = TurquesaNoche,
    onSecondary = Color(0xFF00332D),
    secondaryContainer = Color(0xFF0F4A43),
    onSecondaryContainer = TurquesaClaro,
    background = FondoOscuro,
    surface = SuperficieOscura,
    surfaceVariant = Color(0xFF243142)
)

// Se quitó el color dinámico para que la app siempre use los colores de la empresa.
// El modo oscuro se activa solo si el teléfono lo tiene activado.
@Composable
fun Guia233Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}