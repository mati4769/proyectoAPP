package com.example.guia233.model

// Modelo que almacena los posibles errores del formulario de solicitud
data class SolicitudErrores(
    val titulo: String? = null,
    val descripcion: String? = null,
    val categoria: String? = null,
    val servicio: String? = null
)