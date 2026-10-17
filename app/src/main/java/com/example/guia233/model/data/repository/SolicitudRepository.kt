package com.example.guia233.data.repository

import com.example.guia233.data.local.SolicitudDao
import com.example.guia233.data.local.SolicitudEntity

class SolicitudRepository(private val dao: SolicitudDao) {

    val solicitudes = dao.obtenerTodas()

    suspend fun insertar(solicitud: SolicitudEntity) {
        dao.insertar(solicitud)
    }
}