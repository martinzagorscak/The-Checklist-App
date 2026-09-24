package com.example.thechecklistapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Primary80,
    secondary = Secondary40,
    tertiary = Secondary20,
    outline = Secondary30,
)

private val LightColorScheme = lightColorScheme(
    primary = Primary40,
    secondary = Secondary60,
    tertiary = Secondary80,
    outline = Secondary70,
)

@Composable
fun TheChecklistAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
