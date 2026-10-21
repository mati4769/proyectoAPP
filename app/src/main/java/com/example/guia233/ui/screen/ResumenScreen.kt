package com.example.guia233.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.guia233.model.usuariosDemo
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
            Column(verticalArrangement = Arrangement.spacedBy(space = 4.dp)) {
                Text("Resumen general", style = MaterialTheme.typography.headlineMedium)
                Text("Total de solicitudes: ${solicitudes.size}")
                Text(
                    "Atrasadas: ${solicitudes.count { viewModel.esAtrasada(it) }}",
                    color = MaterialTheme.colorScheme.error
                )

                // ---------- Por estado ----------
                Text(
                    "Por estado",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
                viewModel.estados.drop(1).forEach { estado ->
                    Text("$estado: ${solicitudes.count { it.estado == estado }}")
                }

                // ---------- Por prioridad ----------
                Text(
                    "Por prioridad",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
                viewModel.prioridades.drop(1).forEach { prioridad ->
                    Text("$prioridad: ${solicitudes.count { it.prioridad == prioridad }}")
                }

                // ---------- Por responsable ----------
                Text(
                    "Por responsable",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
                usuariosDemo.filter { it.rol == "Técnico" }.forEach { tecnico ->
                    Text("${tecnico.nombre}: ${solicitudes.count { it.responsable == tecnico.correo }}")
                }
                Text("Sin asignar: ${solicitudes.count { it.responsable == null }}")
            }
        }
    }
}