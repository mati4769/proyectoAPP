package com.example.guia233.model

// Modelo que almacena posibles errores individuales del formulario
data class UsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val direccion: String? = null
)