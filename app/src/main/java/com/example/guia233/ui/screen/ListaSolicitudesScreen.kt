package com.example.guia233.ui.screen

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.guia233.viewmodel.SolicitudViewModel
import androidx.compose.foundation.clickable

@Composable
fun ListaSolicitudesScreen(
    navController: NavController,
    viewModel: SolicitudViewModel
) {
    val solicitudes by viewModel.solicitudes.collectAsState(initial = emptyList())

    Column(
        Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        Text("Mis solicitudes", style = MaterialTheme.typography.headlineMedium)

        Button(
            onClick = {
                viewModel.nuevaSolicitud()
                navController.navigate("formulario")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nueva solicitud")
        }

        if (solicitudes.isEmpty()) {
            Text("Todavía no hay solicitudes registradas")
        }

        LazyColumn {
            items(solicitudes) { s ->
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.cargarSolicitud(s)
                            navController.navigate("formulario")
                        }
                        .padding(vertical = 8.dp)
                ) {
                    Text(s.codigo, style = MaterialTheme.typography.titleMedium)
                    Text("Título: ${s.titulo}")
                    Text("Categoría: ${s.categoria}")
                    Text("Estado: ${s.estado}")
                    Text("Creada: ${s.fechaCreacion}")
                }
                HorizontalDivider()
            }
        }
    }
}