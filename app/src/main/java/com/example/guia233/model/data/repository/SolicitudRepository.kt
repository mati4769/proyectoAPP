package com.example.guia233.model.data.repository

import com.example.guia233.model.data.local.SolicitudDao
import com.example.guia233.model.data.local.SolicitudEntity

class SolicitudRepository(private val dao: SolicitudDao) {
    val solicitudes = dao.obtenerTodas()

    suspend fun insertar(solicitud: SolicitudEntity) {
        dao.insertar(solicitud)
    }

    suspend fun buscarPorCodigo(codigo: String): SolicitudEntity? {
        return dao.buscarPorCodigo(codigo)
    }
}