package com.example.guia233.model.data.repository

import com.example.guia233.model.data.local.HistorialEntity
import com.example.guia233.model.data.local.SolicitudDao
import com.example.guia233.model.data.local.SolicitudEntity

// Repository: intermediario entre el ViewModel y el DAO
class SolicitudRepository(private val dao: SolicitudDao) {
    // Lista de solicitudes que se actualiza sola (Flow)
    val solicitudes = dao.obtenerTodas()

    // Inserta una solicitud nueva y devuelve su id
    suspend fun insertar(solicitud: SolicitudEntity): Long {
        return dao.insertar(solicitud)
    }

    // Guarda los cambios de una solicitud
    suspend fun actualizar(solicitud: SolicitudEntity) {
        dao.actualizar(solicitud)
    }

    // Borra primero el historial y después la solicitud
    suspend fun eliminar(solicitud: SolicitudEntity) {
        dao.eliminarHistorial(solicitud.id)
        dao.eliminar(solicitud)
    }

    // Busca una solicitud por su código
    suspend fun buscarPorCodigo(codigo: String): SolicitudEntity? {
        return dao.buscarPorCodigo(codigo)
    }

    // Guarda un cambio en el historial
    suspend fun insertarHistorial(historial: HistorialEntity) {
        dao.insertarHistorial(historial)
    }

    // Historial de una solicitud
    fun historial(solicitudId: Long) = dao.obtenerHistorial(solicitudId)
}