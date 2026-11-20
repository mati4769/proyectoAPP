package com.example.guia233.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.guia233.model.Servicio
import com.example.guia233.viewmodel.CatalogoViewModel

// Catálogo externo de servicios: el cliente elige el servicio de su solicitud
@Composable
fun CatalogoScreen(
    viewModel: CatalogoViewModel,
    onSeleccionar: (Servicio) -> Unit
) {
    // Observamos el catálogo que trae el ViewModel
    val servicios = viewModel.servicios.collectAsState().value

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
                Text(
                    "Catálogo de servicios",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Servicios que ofrece la empresa. Desde Nueva solicitud, toca uno para elegirlo.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Si el catálogo está vacío se puede volver a pedir a la API
                if (servicios.isEmpty()) {
                    Text("El catálogo está vacío")
                    Button(
                        onClick = { viewModel.cargarServicios() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Recargar catálogo")
                    }
                }
            }
        }

        // ---------- Servicios del catálogo ----------
        items(servicios) { servicio ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSeleccionar(servicio) },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    Modifier.padding(all = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 4.dp)
                ) {
                    Text(servicio.servicio, style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Categoría: ${servicio.categoria}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(servicio.descripcion, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}