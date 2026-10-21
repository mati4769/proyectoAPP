package com.example.guia233.ui.screen

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.guia233.model.usuariosDemo
import com.example.guia233.viewmodel.SolicitudViewModel

@Composable
fun DetalleSolicitudScreen(
    navController: NavController,
    viewModel: SolicitudViewModel
) {
    val solicitudes by viewModel.solicitudes.collectAsState(initial = emptyList())
    val historial by remember { viewModel.historial(viewModel.seleccionadaId) }
        .collectAsState(initial = emptyList())
    val detalle by viewModel.detalle.collectAsState()
    val rol = viewModel.rolActual()

    // Se busca la solicitud elegida en la lista
    val s = solicitudes.find { it.id == viewModel.seleccionadaId }

    if (s == null) {
        Text("Cargando...", modifier = Modifier.padding(16.dp))
        return
    }

    // Una solicitud cerrada o cancelada ya no se puede modificar
    val abierta = s.estado != "Cerrada" && s.estado != "Cancelada"

    // El cliente no ve los comentarios internos
    val historialVisible = if (rol == "Cliente") {
        historial.filter { it.accion != "Comentario interno" }
    } else {
        historial
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {

                // ---------- Datos de la solicitud ----------
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

                // ---------- Campo de comentario (lo usan varias acciones) ----------
                if (abierta) {
                    OutlinedTextField(
                        value = detalle.comentario,
                        onValueChange = viewModel::onComentarioChange,
                        label = { Text("Comentario") },
                        isError = detalle.comentarioError != null,
                        supportingText = { Text(detalle.comentarioError ?: "") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // ---------- Acciones del cliente ----------
                if (rol == "Cliente" && abierta) {
                    Button(
                        onClick = {
                            viewModel.cargarSolicitud(s)
                            navController.navigate("formulario")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Editar solicitud")
                    }

                    if (s.estado == "Pendiente de información") {
                        Button(
                            onClick = { viewModel.cambiarEstado(s, "En revisión", "Cliente respondió información", true) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Responder información")
                        }
                    }

                    if (s.estado == "Resuelta") {
                        Button(
                            onClick = { viewModel.cambiarEstado(s, "Cerrada", "Cliente confirmó la resolución") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Confirmar resolución")
                        }
                        Button(
                            onClick = { viewModel.cambiarEstado(s, "En revisión", "Cliente solicitó reapertura", true) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Solicitar reapertura")
                        }
                    }
                }

                // ---------- Acciones del administrador ----------
                if (rol == "Administrador" && abierta) {
                    Text("Estado", style = MaterialTheme.typography.titleMedium)
                    Button(
                        onClick = { viewModel.cambiarEstado(s, "En revisión", "Solicitud en revisión") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Poner en revisión")
                    }
                    Button(
                        onClick = { viewModel.cambiarEstado(s, "Pendiente de información", "Se solicitó información al cliente", true) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Pedir información al cliente")
                    }

                    Text("Clasificar", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(
                        value = detalle.categoria,
                        onValueChange = viewModel::onCategoriaDetalleChange,
                        label = { Text("Nueva categoría") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = { viewModel.cambiarCategoria(s) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar categoría")
                    }

                    Text("Prioridad", style = MaterialTheme.typography.titleMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
                        Button(onClick = { viewModel.cambiarPrioridad(s, "Baja") }, modifier = Modifier.weight(1f)) {
                            Text("Baja")
                        }
                        Button(onClick = { viewModel.cambiarPrioridad(s, "Media") }, modifier = Modifier.weight(1f)) {
                            Text("Media")
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
                        Button(onClick = { viewModel.cambiarPrioridad(s, "Alta") }, modifier = Modifier.weight(1f)) {
                            Text("Alta")
                        }
                        Button(onClick = { viewModel.cambiarPrioridad(s, "Urgente") }, modifier = Modifier.weight(1f)) {
                            Text("Urgente")
                        }
                    }

                    Text("Asignar responsable", style = MaterialTheme.typography.titleMedium)
                    usuariosDemo.filter { it.rol == "Técnico" }.forEach { tecnico ->
                        Button(
                            onClick = { viewModel.asignar(s, tecnico) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Asignar a ${tecnico.nombre}")
                        }
                    }

                    Text("Seguimiento", style = MaterialTheme.typography.titleMedium)
                    Button(
                        onClick = { viewModel.comentar(s, "Comentario interno") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Agregar comentario interno")
                    }
                    Button(
                        onClick = { viewModel.cambiarEstado(s, "Cerrada", "Solicitud cerrada por el administrador") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cerrar solicitud")
                    }
                    Button(
                        onClick = { viewModel.cambiarEstado(s, "Cancelada", "Solicitud cancelada", true) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancelar (escribe el motivo)")
                    }
                }

                // ---------- Acciones del técnico ----------
                if (rol == "Técnico" && abierta) {
                    Button(
                        onClick = { viewModel.cambiarEstado(s, "En proceso", "Trabajo iniciado") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Iniciar trabajo (En proceso)")
                    }
                    Button(
                        onClick = { viewModel.comentar(s, "Avance registrado") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Registrar avance")
                    }
                    Button(
                        onClick = { viewModel.cambiarEstado(s, "Pendiente de información", "Técnico requiere información", true) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Requiere información")
                    }
                    Button(
                        onClick = { viewModel.cambiarEstado(s, "Resuelta", "Solución registrada", true) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Marcar resuelta (escribe la solución)")
                    }
                }

                Text(
                    "Historial",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }

        // ---------- Historial cronológico ----------
        items(historialVisible) { h ->
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