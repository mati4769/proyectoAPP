package com.example.guia233.model

// Modelo que representa el estado de los campos de la pantalla de detalle
data class DetalleUiState(
    val comentario: String = "",                    // Comentario que se escribe
    val categoria: String = "",                     // Nueva categoría (administrador)
    val errores: DetalleErrores = DetalleErrores()  // Objeto que contiene los errores por campo
)