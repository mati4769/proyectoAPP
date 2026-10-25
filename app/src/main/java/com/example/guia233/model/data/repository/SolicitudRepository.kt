package com.example.guia233.model.data.repository

import com.example.guia233.model.data.local.HistorialEntity
import com.example.guia233.model.data.local.SolicitudDao
import com.example.guia233.model.data.local.SolicitudEntity

class SolicitudRepository(private val dao: SolicitudDao) {
    val solicitudes = dao.obtenerTodas()

    suspend fun insertar(solicitud: SolicitudEntity): Long {
        return dao.insertar(solicitud)
    }

    suspend fun actualizar(solicitud: SolicitudEntity) {
        dao.actualizar(solicitud)
    }

    suspend fun eliminar(solicitud: SolicitudEntity) {
        dao.eliminarHistorial(solicitud.id)
        dao.eliminar(solicitud)
    }

    suspend fun buscarPorCodigo(codigo: String): SolicitudEntity? {
        return dao.buscarPorCodigo(codigo)
    }

    suspend fun insertarHistorial(historial: HistorialEntity) {
        dao.insertarHistorial(historial)
    }

    fun historial(solicitudId: Long) = dao.obtenerHistorial(solicitudId)
}