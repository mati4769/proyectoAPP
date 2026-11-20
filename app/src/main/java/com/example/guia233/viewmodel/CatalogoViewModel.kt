package com.example.guia233.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guia233.model.Servicio
import com.example.guia233.model.data.repository.ServicioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel que mantiene el catálogo de servicios obtenido de la API
open class CatalogoViewModel : ViewModel() {

    protected val repository = ServicioRepository()
    // Flujo mutable que contiene la lista de servicios
    protected val _servicios = MutableStateFlow<List<Servicio>>(emptyList())
    // Flujo público de solo lectura
    open val servicios: StateFlow<List<Servicio>> = _servicios
    // Se llama automáticamente al iniciar
    init {
        cargarServicios()
    }
    // Obtiene el catálogo en segundo plano (también sirve para recargarlo)
    open fun cargarServicios() {
        viewModelScope.launch {
            try {
                _servicios.value = repository.getServicios()
            } catch (e: Exception) {
                println("Error al obtener el catálogo: ${e.localizedMessage}")
            }
        }
    }
}