package com.example.guia233.model.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solicitudes")
data class SolicitudEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val codigo: String,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val servicio: String,
    val estado: String,
    val fechaCreacion: String,
    val evidenciaUri: String? = null,
    val clienteCorreo: String,
    val prioridad: String = "Sin definir",
    val responsable: String? = null,
    val ultimaActualizacion: String,
    val creadaEnMillis: Long = System.currentTimeMillis(),
    val resolucion: String? = null
)