package com.example.guia233.model

// Esta clase representa un post obtenido desde la API (se usa como solicitud de prueba)
data class Post(
    val userId: Int,   // ID del usuario que creó el post
    val id: Int,       // ID del post
    val title: String, // Título del post
    val body: String   // Cuerpo o contenido del post
)