package com.example.guia233.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
        Text("Menú", style = MaterialTheme.typography.headlineMedium)
        Text("Hola, ${usuario?.nombre ?: ""} (${usuario?.rol ?: ""})")

        // El texto del botón cambia según el perfil
        val textoLista = when (usuario?.rol) {
            "Administrador" -> "Todas las solicitudes"
            "Técnico" -> "Solicitudes asignadas"
            else -> "Mis solicitudes"
        }

        Button(
            onClick = { navController.navigate("lista") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(textoLista)
        }

        // Solo el administrador ve el resumen general
        if (usuario?.rol == "Administrador") {
            Button(
                onClick = { navController.navigate("resumen") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Resumen general")
            }
        }

        // El técnico solo trabaja con sus solicitudes asignadas
        if (usuario?.rol != "Técnico") {
            Button(
                onClick = { navController.navigate("qr") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Escanear código QR")
            }
        }

        Button(
            onClick = {
                onCerrarSesion()
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}