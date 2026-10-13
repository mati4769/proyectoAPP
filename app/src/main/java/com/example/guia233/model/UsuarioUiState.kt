package com.example.guia233.model

// Modelo principal que representa el estado del formulario del usuario
data class UsuarioUiState(
    val nombre: String = "",              // Nombre del usuario
    val correo: String = "",              // Correo electrónico
    val clave: String = "",               // Contraseña
    val direccion: String = "",           // Dirección del usuario
    val aceptaTerminos: Boolean = false,  // Confirmación de términos
    val errores: UsuarioErrores = UsuarioErrores() // Objeto que contiene los errores por campo
)