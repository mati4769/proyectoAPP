package com.example.guia233.ui.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.guia233.ui.components.Etiqueta
import com.example.guia233.ui.theme.colorEstado
import com.example.guia233.viewmodel.SolicitudViewModel

// Formulario para crear o editar una solicitud
@Composable
fun SolicitudFormScreen(
    navController: NavController,
    viewModel: SolicitudViewModel
) {
    // Observa el estado del formulario en el ViewModel
    val estado by viewModel.estado.collectAsState()
    // Contexto para mostrar el mensaje de confirmación (Toast)
    val context = LocalContext.current

    // Abre la galería del dispositivo para elegir una imagen
    val elegirImagen = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            viewModel.onEvidenciaChange(uri.toString())
        }
    }

    // verticalScroll permite deslizar el formulario si no cabe en la pantalla
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        // Si hay una solicitud cargada se está editando; si no, es nueva
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
                // Cada campo muestra su error en rojo si no es válido
                OutlinedTextField(
                    value = estado.titulo,
                    onValueChange = viewModel::onTituloChange,
                    label = { Text("Título") },
                    isError = estado.errores.titulo != null,
                    supportingText = { Text(estado.errores.titulo ?: "") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = estado.descripcion,
                    onValueChange = viewModel::onDescripcionChange,
                    label = { Text("Descripción") },
                    isError = estado.errores.descripcion != null,
                    supportingText = { Text(estado.errores.descripcion ?: "") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = estado.categoria,
                    onValueChange = viewModel::onCategoriaChange,
                    label = { Text("Categoría (ej: Hosting, Incidente técnico)") },
                    isError = estado.errores.categoria != null,
                    supportingText = { Text(estado.errores.categoria ?: "") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = estado.servicio,
                    onValueChange = viewModel::onServicioChange,
                    label = { Text("Servicio relacionado") },
                    isError = estado.errores.servicio != null,
                    supportingText = { Text(estado.errores.servicio ?: "") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Abre el catálogo externo para elegir el servicio y su categoría
                Button(
                    onClick = { navController.navigate("catalogo") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Abrir catálogo")
                }
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

                // Muestra la imagen adjunta (también al editar)
                val imagen = remember(estado.evidenciaUri) { viewModel.imagenEvidencia(estado.evidenciaUri) }
                if (imagen != null) {
                    Image(
                        bitmap = imagen.asImageBitmap(),
                        contentDescription = "Evidencia adjunta",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                }
                Button(
                    // Abre la galería para elegir la imagen
                    onClick = { elegirImagen.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (estado.evidenciaUri != null) "Cambiar evidencia" else "Adjuntar evidencia (imagen)")
                }
            }
        }

        Button(
            onClick = {
                // Solo guarda si todos los campos son válidos
                if (viewModel.validarFormulario()) {
                    viewModel.guardar {
                        // Mensaje de confirmación: la solicitud quedó guardada en Room
                        Toast.makeText(context, "Solicitud guardada correctamente", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                        // Si es nueva, se abre su detalle para ver el código y el QR
                        if (editando == null) {
                            navController.navigate("detalle")
                        }
                    }
                }
            },
            // Se desactiva mientras guarda para no guardar dos veces
            enabled = !estado.guardando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (editando != null) "Guardar cambios" else "Guardar solicitud")
        }
    }
}