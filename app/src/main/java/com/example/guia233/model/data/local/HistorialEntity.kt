package com.example.guia233.model.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Cada fila es un cambio realizado sobre una solicitud
@Entity(tableName = "historial")
data class HistorialEntity(
    // Llave primaria, Room genera el número solo
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    // Id de la solicitud a la que pertenece este cambio
    val solicitudId: Long,
    // Estado antes y después del cambio
    val estadoAnterior: String,
    val estadoNuevo: String,
    // Qué se hizo y comentario opcional
    val accion: String,
    val comentario: String = "",
    // Quién hizo el cambio, con qué perfil y cuándo
    val usuario: String,
    val perfil: String,
    val fecha: String
)