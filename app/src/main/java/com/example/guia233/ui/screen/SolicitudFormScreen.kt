package com.example.guia233.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.guia233.viewmodel.SolicitudViewModel

@Composable
fun SolicitudFormScreen(
    navController: NavController,
    viewModel: SolicitudViewModel
) {
    val estado by viewModel.estado.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        Text("Nueva solicitud", style = MaterialTheme.typography.headlineMedium)

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

        Button(
            onClick = {
                if (viewModel.validarFormulario()) {
                    viewModel.guardar {
                        navController.popBackStack()
                    }
                }
            },
            enabled = !estado.guardando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar solicitud")
        }
    }
}