package com.example.guia233.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.guia233.model.usuariosDemo
import com.example.guia233.ui.components.Etiqueta
import com.example.guia233.ui.theme.colorEstado
import com.example.guia233.ui.theme.colorPrioridad
import com.example.guia233.utils.generarQr
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
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        item {
            Column(
                Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(space = 12.dp)
            ) {

                // ---------- Encabezado: código, estado y prioridad ----------
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Column(
                        Modifier.padding(all = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                    ) {
                        Text(
                            s.codigo,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            s.titulo,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
                            Etiqueta(s.estado, colorEstado(s.estado))
                            Etiqueta("Prioridad: ${s.prioridad}", colorPrioridad(s.prioridad))
                        }
                    }
                }

                // ---------- Datos de la solicitud ----------
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        Modifier.padding(all = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(space = 6.dp)
                    ) {
                        Text(s.descripcion, style = MaterialTheme.typography.bodyLarge)
                        Dato("Categoría", s.categoria)
                        Dato("Servicio", s.servicio)
                        Dato("Responsable", s.responsable ?: "Sin asignar")
                        Dato("Evidencia", if (s.evidenciaUri != null) "Sí" else "No")
                        Dato("Creada", s.fechaCreacion)
                        Dato("Última actualización", s.ultimaActualizacion)
                        if (s.resolucion != null) {
                            Dato("Resolución", "${s.resolucion}")
                        }
                    }
                }

                // ---------- QR de la solicitud (contiene su código) ----------
                // remember evita volver a generar el QR cada vez que se redibuja la pantalla
                val qr = remember(s.codigo) { generarQr(s.codigo) }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                    ) {
                        Text("Código QR de seguimiento", style = MaterialTheme.typography.titleMedium)
                        Image(
                            bitmap = qr.asImageBitmap(),
                            contentDescription = "QR de ${s.codigo}",
                            modifier = Modifier.size(200.dp)
                        )
                        Text(
                            "Escanéalo desde el menú para abrir esta solicitud",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
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
                    Seccion("Acciones") {
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
                }

                // ---------- Acciones del administrador ----------
                if (rol == "Administrador" && abierta) {
                    Seccion("Estado") {
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
                    }

                    Seccion("Clasificar") {
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
                    }

                    Seccion("Prioridad") {
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
                    }

                    Seccion("Asignar responsable") {
                        usuariosDemo.filter { it.rol == "Técnico" }.forEach { tecnico ->
                            Button(
                                onClick = { viewModel.asignar(s, tecnico) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Asignar a ${tecnico.nombre}")
                            }
                        }
                    }

                    Seccion("Seguimiento") {
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
                }

                // ---------- Eliminar (solo administrador y solo si está finalizada) ----------
                if (rol == "Administrador" && !abierta) {
                    Seccion("Solicitud finalizada") {
                        Button(
                            onClick = {
                                viewModel.eliminar(s)
                                navController.popBackStack()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Eliminar solicitud")
                        }
                    }
                }

                // ---------- Acciones del técnico ----------
                if (rol == "Técnico" && abierta) {
                    Seccion("Acciones") {
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
                }

                Text(
                    "Historial",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // ---------- Historial cronológico ----------
        items(historialVisible) { h ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    Modifier.padding(all = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 4.dp)
                ) {
                    Text(h.accion, style = MaterialTheme.typography.titleSmall)
                    Text(
                        h.fecha,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
                        Etiqueta(h.estadoAnterior, colorEstado(h.estadoAnterior))
                        Text("→")
                        Etiqueta(h.estadoNuevo, colorEstado(h.estadoNuevo))
                    }
                    Text("Por: ${h.usuario} (${h.perfil})", style = MaterialTheme.typography.bodySmall)
                    if (h.comentario.isNotBlank()) {
                        Text("\"${h.comentario}\"", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(16.dp))
        }
    }
}

// Un dato con su nombre arriba (en gris) y el valor abajo
@Composable
fun Dato(etiqueta: String, valor: String) {
    Column {
        Text(
            etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(valor, style = MaterialTheme.typography.bodyMedium)
    }
}

// Tarjeta con un título y botones dentro, para ordenar las acciones
@Composable
fun Seccion(titulo: String, contenido: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            Modifier.padding(all = 12.dp),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            Text(
                titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            contenido()
        }
    }
}