package com.example.guia233.model

import com.example.guia233.model.data.local.SolicitudEntity

// Modelo que representa el estado del formulario de solicitud
data class SolicitudUiState(
    val titulo: String = "",                          // Título de la solicitud
    val descripcion: String = "",                     // Descripción de la necesidad
    val categoria: String = "",                       // Categoría
    val servicio: String = "",                        // Servicio relacionado
    val evidenciaUri: String? = null,                 // Imagen adjunta (null si no tiene)
    val guardando: Boolean = false,                   // true mientras se guarda
    val guardadoExitosamente: Boolean = false,        // true cuando terminó de guardar
    val solicitudEditando: SolicitudEntity? = null,   // Solicitud que se edita (null si es nueva)
    val errores: SolicitudErrores = SolicitudErrores() // Objeto que contiene los errores por campo
)