package com.example.guia233.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.guia233.model.usuariosDemo
import com.example.guia233.ui.components.Etiqueta
import com.example.guia233.ui.theme.colorEstado
import com.example.guia233.ui.theme.colorPrioridad
import com.example.guia233.viewmodel.SolicitudViewModel

@Composable
fun ResumenScreen(viewModel: SolicitudViewModel) {
    // Se usan las mismas solicitudes guardadas en Room, así los números coinciden
    val solicitudes by viewModel.solicitudes.collectAsState(initial = emptyList())

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(space = 12.dp)) {
                Text(
                    "Resumen general",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                // ---------- Totales grandes ----------
                Row(horizontalArrangement = Arrangement.spacedBy(space = 12.dp)) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Column(Modifier.padding(all = 16.dp)) {
                            Text(
                                "${solicitudes.size}",
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Text("Total", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Column(Modifier.padding(all = 16.dp)) {
                            Text(
                                "${solicitudes.count { viewModel.esAtrasada(it) }}",
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.onError
                            )
                            Text("Atrasadas", color = MaterialTheme.colorScheme.onError)
                        }
                    }
                }

                // ---------- Por estado ----------
                Seccion("Por estado") {
                    viewModel.estados.drop(1).forEach { estado ->
                        FilaConteo(estado, solicitudes.count { it.estado == estado }, colorEstado(estado))
                    }
                }

                // ---------- Por prioridad ----------
                Seccion("Por prioridad") {
                    viewModel.prioridades.drop(1).forEach { prioridad ->
                        FilaConteo(prioridad, solicitudes.count { it.prioridad == prioridad }, colorPrioridad(prioridad))
                    }
                }

                // ---------- Por responsable ----------
                Seccion("Por responsable") {
                    usuariosDemo.filter { it.rol == "Técnico" }.forEach { tecnico ->
                        FilaConteo(
                            tecnico.nombre,
                            solicitudes.count { it.responsable == tecnico.correo },
                            MaterialTheme.colorScheme.primary
                        )
                    }
                    FilaConteo(
                        "Sin asignar",
                        solicitudes.count { it.responsable == null },
                        MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

// Nombre a la izquierda y la cantidad en una etiqueta de color a la derecha
@Composable
fun FilaConteo(nombre: String, cantidad: Int, color: Color) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(nombre, color = MaterialTheme.colorScheme.onSecondaryContainer)
        Etiqueta("$cantidad", color)
    }
}