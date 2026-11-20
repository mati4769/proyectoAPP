package com.example.guia233.model

// Un servicio del catálogo externo (viene de la API REST)
data class Servicio(
    val id: Int,             // ID del servicio
    val servicio: String,    // Nombre del servicio
    val categoria: String,   // Categoría
    val descripcion: String  // Descripción
)