package com.example.guia233.model.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import androidx.room.Delete

// DAO: aquí están las consultas a la base de datos
@Dao
interface SolicitudDao {

    // Devuelve el id de la solicitud nueva (se usa para el historial)
    @Insert
    suspend fun insertar(solicitud: SolicitudEntity): Long

    // Guarda los cambios de una solicitud que ya existe
    @Update
    suspend fun actualizar(solicitud: SolicitudEntity)

    // Borra una solicitud de la base de datos
    @Delete
    suspend fun eliminar(solicitud: SolicitudEntity)

    // Borra el historial de una solicitud (para no dejar registros sueltos)
    @Query("DELETE FROM historial WHERE solicitudId = :solicitudId")
    suspend fun eliminarHistorial(solicitudId: Long)

    // Trae todas las solicitudes (las más nuevas primero) y avisa cuando cambian
    @Query("SELECT * FROM solicitudes ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<SolicitudEntity>>

    // Busca una solicitud por su código (se usa al leer el QR)
    @Query("SELECT * FROM solicitudes WHERE codigo = :codigo")
    suspend fun buscarPorCodigo(codigo: String): SolicitudEntity?

    // Historial
    @Insert
    suspend fun insertarHistorial(historial: HistorialEntity)

    // Trae el historial de una solicitud, del más antiguo al más nuevo
    @Query("SELECT * FROM historial WHERE solicitudId = :solicitudId ORDER BY id ASC")
    fun obtenerHistorial(solicitudId: Long): Flow<List<HistorialEntity>>
}