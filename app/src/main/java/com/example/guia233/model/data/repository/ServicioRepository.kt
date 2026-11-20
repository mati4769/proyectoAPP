package com.example.guia233.model.data.repository

import com.example.guia233.model.Servicio
import com.example.guia233.model.data.remote.RetrofitInstance

// Este repositorio obtiene el catálogo de servicios usando Retrofit
open class ServicioRepository {

    // Función que obtiene los servicios desde la API externa
    open suspend fun getServicios(): List<Servicio> {
        return RetrofitInstance.api.getServicios()
    }
}