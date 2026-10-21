package com.example.guia233.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.guia233.viewmodel.SolicitudViewModel

@Composable
fun DetalleSolicitudScreen(
    navController: NavController,
    viewModel: SolicitudViewModel
) {
    val solicitudes by viewModel.solicitudes.collectAsState(initial = emptyList())
    val historial by remember { viewModel.historial(viewModel.seleccionadaId) }
        .collectAsState(initial = emptyList())
    val rol = viewModel.rolActual()

    // Se busca la solicitud elegida en la lista
    val s = solicitudes.find { it.id == viewModel.seleccionadaId }

    if (s == null) {
        Text("Cargando...", modifier = Modifier.padding(16.dp))
        return
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        // Datos de la solicitud
        item {
            Column {
                Text(s.codigo, style = MaterialTheme.typography.headlineMedium)
                Text("Título: ${s.titulo}")
                Text("Descripción: ${s.descripcion}")
                Text("Categoría: ${s.categoria}")
                Text("Servicio: ${s.servicio}")
                Text("Estado: ${s.estado}")
                Text("Prioridad: ${s.prioridad}")
                Text("Responsable: ${s.responsable ?: "Sin asignar"}")
                Text("Evidencia: ${if (s.evidenciaUri != null) "Sí" else "No"}")
                Text("Creada: ${s.fechaCreacion}")
                Text("Última actualización: ${s.ultimaActualizacion}")
                if (s.resolucion != null) {
                    Text("Resolución: ${s.resolucion}")
                }

                // El cliente puede editar mientras la solicitud no esté cerrada o cancelada
                if (rol == "Cliente" && s.estado != "Cerrada" && s.estado != "Cancelada") {
                    Button(
                        onClick = {
                            viewModel.cargarSolicitud(s)
                            navController.navigate("formulario")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        Text("Editar solicitud")
                    }
                }

                Text(
                    "Historial",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }
        }

        // Historial cronológico de cambios
        items(historial) { h ->
            Column(Modifier.padding(vertical = 8.dp)) {
                Text("${h.fecha} - ${h.accion}", style = MaterialTheme.typography.titleSmall)
                Text("Estado: ${h.estadoAnterior} → ${h.estadoNuevo}")
                Text("Por: ${h.usuario} (${h.perfil})")
                if (h.comentario.isNotBlank()) {
                    Text("Comentario: ${h.comentario}")
                }
            }
            HorizontalDivider()
        }
    }
}