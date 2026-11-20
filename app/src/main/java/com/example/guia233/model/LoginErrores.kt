package com.example.guia233.model

// Modelo que almacena los posibles errores del formulario de login
data class LoginErrores(
    val correo: String? = null,
    val clave: String? = null
)