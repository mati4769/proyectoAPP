package com.example.guia233.ui.screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.guia233.viewmodel.LoginViewModel
import com.example.guia233.model.Usuario

// Pantalla de inicio de sesión
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel,
    onIngresar: (Usuario) -> Unit
) {
    // Observa el estado del login en el ViewModel
    val estado by viewModel.estado.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ---------- Encabezado con el nombre de la app ----------
        Text(
            "proyecto_evaluacion_APP",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            "Registra y sigue tus requerimientos de soporte, web y hosting",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        // ---------- Formulario dentro de una tarjeta ----------
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                Modifier.padding(all = 16.dp),
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                Text("Iniciar sesión", style = MaterialTheme.typography.titleLarge)

                // Campo de correo con su mensaje de error
                OutlinedTextField(
                    value = estado.correo,
                    onValueChange = viewModel::onCorreoChange,
                    label = { Text("Ingrese Correo") },
                    isError = estado.errores.correo != null,
                    supportingText = { Text(estado.errores.correo ?: "") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("campoCorreo") // Etiqueta para la prueba de interfaz
                )

                // Campo de contraseña (oculta los caracteres)
                OutlinedTextField(
                    value = estado.clave,
                    onValueChange = viewModel::onClaveChange,
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = estado.errores.clave != null,
                    supportingText = { Text(estado.errores.clave ?: "") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("campoClave") // Etiqueta para la prueba de interfaz
                )

                Button(
                    onClick = {
                        // Si los datos son correctos, avisa quién entró y abre el menú
                        if (viewModel.validarFormulario()) {
                            onIngresar(viewModel.estado.value.usuario!!)
                            navController.navigate("menu")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ingresar")
                }
            }
        }

        // Aviso de que las cuentas son de prueba
        Text(
            "Solo cuentas ficticias de prueba",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}