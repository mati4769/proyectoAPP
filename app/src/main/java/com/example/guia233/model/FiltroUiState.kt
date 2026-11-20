package com.example.guia233.model

// Modelo que representa la búsqueda y los filtros de la lista
data class FiltroUiState(
    val busqueda: String = "",          // Texto de búsqueda
    val estado: String = "Todos",       // Filtro por estado
    val prioridad: String = "Todas",    // Filtro por prioridad
    val cliente: String = "Todos",      // Filtro por cliente
    val responsable: String = "Todos",  // Filtro por técnico
    val soloAtrasadas: Boolean = false, // Ver solo atrasadas
    val mostrarFiltros: Boolean = false // Botones de filtros abiertos o cerrados
)