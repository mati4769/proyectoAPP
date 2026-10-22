package com.example.guia233.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.guia233.model.Usuario

@Composable
fun MenuScreen(
    navController: NavController,
    usuario: Usuario?,
    onCerrarSesion: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        // ---------- Saludo ----------
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Column(Modifier.padding(all = 20.dp)) {
                Text(
                    "Hola, ${usuario?.nombre ?: ""}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    "Perfil: ${usuario?.rol ?: ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Text("¿Qué quieres hacer?", style = MaterialTheme.typography.titleMedium)

        // El texto de la opción cambia según el perfil
        val textoLista = when (usuario?.rol) {
            "Administrador" -> "Todas las solicitudes"
            "Técnico" -> "Solicitudes asignadas"
            else -> "Mis solicitudes"
        }
        val detalleLista = when (usuario?.rol) {
            "Administrador" -> "Clasificar, priorizar y asignar"
            "Técnico" -> "Registrar avances y resolver"
            else -> "Crear y seguir tus requerimientos"
        }

        OpcionMenu(textoLista, detalleLista) { navController.navigate("lista") }

        // Solo el administrador ve el resumen general
        if (usuario?.rol == "Administrador") {
            OpcionMenu("Resumen general", "Cantidades por estado, prioridad y técnico") {
                navController.navigate("resumen")
            }
        }

        // El técnico solo trabaja con sus solicitudes asignadas
        if (usuario?.rol != "Técnico") {
            OpcionMenu("Escanear código QR", "Buscar una solicitud por su código") {
                navController.navigate("qr")
            }
        }

        Button(
            onClick = {
                onCerrarSesion()
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Cerrar sesión")
        }
    }
}

// Tarjeta que se puede tocar, con título y una descripción corta
@Composable
fun OpcionMenu(titulo: String, descripcion: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(Modifier.padding(all = 16.dp)) {
            Text(
                titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}