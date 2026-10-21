package com.example.guia233.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.guia233.viewmodel.SolicitudViewModel

@Composable
fun ListaSolicitudesScreen(
    navController: NavController,
    viewModel: SolicitudViewModel
) {
    val solicitudes by viewModel.solicitudes.collectAsState(initial = emptyList())
    val filtro by viewModel.filtro.collectAsState()
    val rol = viewModel.rolActual()

    // Lista después de aplicar la búsqueda y los filtros
    val visibles = viewModel.aplicarFiltros(solicitudes, filtro)

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
                val titulo = when (rol) {
                    "Administrador" -> "Todas las solicitudes"
                    "Técnico" -> "Solicitudes asignadas"
                    else -> "Mis solicitudes"
                }
                Text(titulo, style = MaterialTheme.typography.headlineMedium)

                // Solo el cliente puede registrar solicitudes
                if (rol == "Cliente") {
                    Button(
                        onClick = {
                            viewModel.nuevaSolicitud()
                            navController.navigate("formulario")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Nueva solicitud")
                    }
                }

                // ---------- Búsqueda ----------
                OutlinedTextField(
                    value = filtro.busqueda,
                    onValueChange = viewModel::onBusquedaChange,
                    label = { Text("Buscar por código o título") },
                    modifier = Modifier.fillMaxWidth()
                )

                // ---------- Filtros (cada toque pasa a la siguiente opción) ----------
                Row(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
                    Button(onClick = { viewModel.cambiarFiltroEstado() }, modifier = Modifier.weight(1f)) {
                        Text("Estado: ${filtro.estado}")
                    }
                    Button(onClick = { viewModel.cambiarFiltroPrioridad() }, modifier = Modifier.weight(1f)) {
                        Text("Prioridad: ${filtro.prioridad}")
                    }
                }

                // Filtros que solo usa el administrador
                if (rol == "Administrador") {
                    Row(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
                        Button(onClick = { viewModel.cambiarFiltroCliente() }, modifier = Modifier.weight(1f)) {
                            Text("Cliente: ${filtro.cliente.substringBefore("@")}")
                        }
                        Button(onClick = { viewModel.cambiarFiltroResponsable() }, modifier = Modifier.weight(1f)) {
                            Text("Técnico: ${filtro.responsable.substringBefore("@")}")
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
                    Button(onClick = { viewModel.cambiarSoloAtrasadas() }, modifier = Modifier.weight(1f)) {
                        Text(if (filtro.soloAtrasadas) "Viendo atrasadas" else "Solo atrasadas")
                    }
                    Button(onClick = { viewModel.limpiarFiltros() }, modifier = Modifier.weight(1f)) {
                        Text("Limpiar filtros")
                    }
                }

                Text("Mostrando ${visibles.size} de ${solicitudes.size}")

                if (visibles.isEmpty()) {
                    Text("No hay solicitudes para mostrar")
                }
            }
        }

        // ---------- Lista ----------
        items(visibles) { s ->
            Column(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        // Todos los perfiles pueden ver el detalle
                        viewModel.verDetalle(s.id)
                        navController.navigate("detalle")
                    }
                    .padding(vertical = 8.dp)
            ) {
                Text(s.codigo, style = MaterialTheme.typography.titleMedium)
                if (viewModel.esAtrasada(s)) {
                    Text("ATRASADA", color = MaterialTheme.colorScheme.error)
                }
                Text("Título: ${s.titulo}")
                Text("Categoría: ${s.categoria}")
                Text("Estado: ${s.estado}")
                Text("Prioridad: ${s.prioridad}")
                Text("Responsable: ${s.responsable ?: "Sin asignar"}")
                Text("Creada: ${s.fechaCreacion}")
                Text("Última actualización: ${s.ultimaActualizacion}")
            }
            HorizontalDivider()
        }
    }
}