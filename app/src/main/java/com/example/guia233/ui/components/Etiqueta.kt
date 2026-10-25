package com.example.guia233.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Pequeña etiqueta de color para mostrar estado, prioridad o "Atrasada"
@Composable
fun Etiqueta(texto: String, color: Color) {
    Surface(
        color = color,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            texto,
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}