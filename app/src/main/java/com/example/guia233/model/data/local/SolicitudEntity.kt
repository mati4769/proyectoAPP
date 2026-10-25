package com.example.guia233.model.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla "solicitudes": cada propiedad es una columna
@Entity(tableName = "solicitudes")
data class SolicitudEntity(
    // Llave primaria, Room genera el número solo
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    // Código de seguimiento (SOL-...), también va dentro del QR
    val codigo: String,
    // Datos que escribe el cliente en el formulario
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val servicio: String,
    // Estado actual (Recibida, En proceso, Resuelta, Cerrada...)
    val estado: String,
    // Fecha de creación en texto (para mostrar)
    val fechaCreacion: String,
    // Ruta de la imagen adjunta (null si no tiene)
    val evidenciaUri: String? = null,
    // Correo del cliente que creó la solicitud
    val clienteCorreo: String,
    // Prioridad que define el administrador
    val prioridad: String = "Sin definir",
    // Correo del técnico asignado (null si no tiene)
    val responsable: String? = null,
    // Fecha del último cambio
    val ultimaActualizacion: String,
    // Momento de creación en milisegundos (para saber si está atrasada)
    val creadaEnMillis: Long = System.currentTimeMillis(),
    // Solución que escribió el técnico al resolverla
    val resolucion: String? = null
)