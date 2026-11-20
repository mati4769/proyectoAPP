package com.example.guia233.model

// Modelo que representa el estado del formulario de login
data class LoginUiState(
    val correo: String = "",                    // Correo ingresado
    val clave: String = "",                     // Contraseña ingresada
    val usuario: Usuario? = null,               // Cuenta encontrada al ingresar
    val errores: LoginErrores = LoginErrores()  // Objeto que contiene los errores por campo
)