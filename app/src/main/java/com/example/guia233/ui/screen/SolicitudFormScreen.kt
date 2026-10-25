package com.example.guia233.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.guia233.ui.components.Etiqueta
import com.example.guia233.ui.theme.colorEstado
import com.example.guia233.viewmodel.SolicitudViewModel

@Composable
fun SolicitudFormScreen(
    navController: NavController,
    viewModel: SolicitudViewModel
) {
    val estado by viewModel.estado.collectAsState()

    // Abre la galería del dispositivo para elegir una imagen
    val elegirImagen = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            viewModel.onEvidenciaChange(uri.toString())
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        val editando = estado.solicitudEditando
        if (editando != null) {
            Text(
                "Editar solicitud",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    Modifier.padding(all = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 4.dp)
                ) {
                    Text("Código: ${editando.codigo}", style = MaterialTheme.typography.titleMedium)
                    Etiqueta(editando.estado, colorEstado(editando.estado))
                    Text("Creada: ${editando.fechaCreacion}", style = MaterialTheme.typography.bodySmall)
                }
            }
        } else {
            Text(
                "Nueva solicitud",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "Cuéntanos qué necesitas. Al guardar recibirás un código de seguimiento.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // ---------- Datos de la solicitud ----------
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(Modifier.padding(all = 16.dp)) {
                OutlinedTextField(
                    value = estado.titulo,
                    onValueChange = viewModel::onTituloChange,
                    label = { Text("Título") },
                    isError = estado.tituloError != null,
                    supportingText = { Text(estado.tituloError ?: "") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = estado.descripcion,
                    onValueChange = viewModel::onDescripcionChange,
                    label = { Text("Descripción") },
                    isError = estado.descripcionError != null,
                    supportingText = { Text(estado.descripcionError ?: "") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = estado.categoria,
                    onValueChange = viewModel::onCategoriaChange,
                    label = { Text("Categoría (ej: Hosting, Incidente técnico)") },
                    isError = estado.categoriaError != null,
                    supportingText = { Text(estado.categoriaError ?: "") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = estado.servicio,
                    onValueChange = viewModel::onServicioChange,
                    label = { Text("Servicio relacionado") },
                    isError = estado.servicioError != null,
                    supportingText = { Text(estado.servicioError ?: "") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // ---------- Evidencia ----------
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                Modifier.padding(all = 16.dp),
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                Text("Evidencia", style = MaterialTheme.typography.titleMedium)
                Text(
                    if (estado.evidenciaUri != null) "Imagen adjunta" else "Sin adjuntar",
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(
                    onClick = { elegirImagen.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (estado.evidenciaUri != null) "Cambiar evidencia" else "Adjuntar evidencia (imagen)")
                }
            }
        }

        Button(
            onClick = {
                if (viewModel.validarFormulario()) {
                    viewModel.guardar {
                        navController.popBackStack()
                        // Si es nueva, se abre su detalle para ver el código y el QR
                        if (editando == null) {
                            navController.navigate("detalle")
                        }
                    }
                }
            },
            enabled = !estado.guardando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (editando != null) "Guardar cambios" else "Guardar solicitud")
        }
    }
}