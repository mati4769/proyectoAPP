package com.example.guia233.model.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SolicitudDao {

    // Devuelve el id de la solicitud nueva (se usa para el historial)
    @Insert
    suspend fun insertar(solicitud: SolicitudEntity): Long

    @Update
    suspend fun actualizar(solicitud: SolicitudEntity)

    @Query("SELECT * FROM solicitudes ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<SolicitudEntity>>

    @Query("SELECT * FROM solicitudes WHERE codigo = :codigo")
    suspend fun buscarPorCodigo(codigo: String): SolicitudEntity?

    // Historial
    @Insert
    suspend fun insertarHistorial(historial: HistorialEntity)

    @Query("SELECT * FROM historial WHERE solicitudId = :solicitudId ORDER BY id ASC")
    fun obtenerHistorial(solicitudId: Long): Flow<List<HistorialEntity>>
}