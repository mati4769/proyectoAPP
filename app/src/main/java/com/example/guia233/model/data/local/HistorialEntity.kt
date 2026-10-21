package com.example.guia233.model.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Cada fila es un cambio realizado sobre una solicitud
@Entity(tableName = "historial")
data class HistorialEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val solicitudId: Long,
    val estadoAnterior: String,
    val estadoNuevo: String,
    val accion: String,
    val comentario: String = "",
    val usuario: String,
    val perfil: String,
    val fecha: String
)