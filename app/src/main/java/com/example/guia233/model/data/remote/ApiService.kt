package com.example.guia233.model.data.remote

import com.example.guia233.model.Servicio
import retrofit2.http.GET

// Esta interfaz define los endpoints HTTP de la API externa
interface ApiService {

    // GET del catálogo externo de servicios
    @GET("/mati4769/proyectoAPP/main/catalogo/servicios.json")
    suspend fun getServicios(): List<Servicio>
}