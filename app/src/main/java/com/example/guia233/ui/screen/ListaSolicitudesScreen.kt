package com.example.guia233.ui.screen

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.guia233.ui.theme.colorEstado
import com.example.guia233.ui.theme.colorPrioridad
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
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        item {
            Column(
                Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                val titulo = when (rol) {
                    "Administrador" -> "Todas las solicitudes"
                    "Técnico" -> "Solicitudes asignadas"
                    else -> "Mis solicitudes"
                }
                Text(
                    titulo,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                // Solo el cliente puede registrar solicitudes
                if (rol == "Cliente") {
                    Button(
                        onClick = {
                            viewModel.nuevaSolicitud()
                            navController.navigate("formulario")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("+ Nueva solicitud")
                    }
                }

                // ---------- Búsqueda ----------
                OutlinedTextField(
                    value = filtro.busqueda,
                    onValueChange = viewModel::onBusquedaChange,
                    label = { Text("Buscar por código o título") },
                    modifier = Modifier.fillMaxWidth()
                )

                // ---------- Botón para mostrar u ocultar los filtros ----------
                Button(onClick = { viewModel.cambiarMostrarFiltros() }, modifier = Modifier.fillMaxWidth()) {
                    Text(if (filtro.mostrarFiltros) "Ocultar filtros" else "Mostrar filtros")
                }

                // ---------- Filtros (aparecen y desaparecen con animación) ----------
                AnimatedVisibility(visible = filtro.mostrarFiltros) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            Modifier.padding(all = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                        ) {
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
                        }
                    }
                }

                Text(
                    "Mostrando ${visibles.size} de ${solicitudes.size}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (visibles.isEmpty()) {
                    Text("No hay solicitudes para mostrar")
                }
            }
        }

        // ---------- Lista ----------
        // key = id permite animar cada solicitud cuando cambia la lista
        items(visibles, key = { it.id }) { s ->
            Card(
                modifier = Modifier
                    .animateItem()
                    .fillMaxWidth()
                    .clickable {
                        // Todos los perfiles pueden ver el detalle
                        viewModel.verDetalle(s.id)
                        navController.navigate("detalle")
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    Modifier.padding(all = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 6.dp)
                ) {
                    // Código a la izquierda y estado a la derecha
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            s.codigo,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Etiqueta(s.estado, colorEstado(s.estado))
                    }

                    Text(s.titulo, style = MaterialTheme.typography.titleMedium)
                    Text(
                        s.categoria,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
                        Etiqueta("Prioridad: ${s.prioridad}", colorPrioridad(s.prioridad))
                        if (viewModel.esAtrasada(s)) {
                            Etiqueta("ATRASADA", MaterialTheme.colorScheme.error)
                        }
                    }

                    Text(
                        "Responsable: ${s.responsable ?: "Sin asignar"}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        "Actualizada: ${s.ultimaActualizacion}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}