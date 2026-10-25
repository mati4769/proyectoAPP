package com.example.guia233.model.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Base de datos Room con las tablas solicitudes e historial
@Database(entities = [SolicitudEntity::class, HistorialEntity::class], version = 4)
abstract class AppDatabase : RoomDatabase() {

    // Da acceso a las consultas del DAO
    abstract fun solicitudDao(): SolicitudDao

    // Una sola base de datos para toda la app
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // La crea la primera vez y después reutiliza la misma
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "solicitudes_database"
                    // Si cambia la versión, borra y vuelve a crear las tablas
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}