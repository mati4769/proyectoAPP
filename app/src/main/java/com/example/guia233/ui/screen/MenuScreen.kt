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

@Composable
fun MenuScreen(
    navController: NavController,
    onCerrarSesion: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        Text("Menú", style = MaterialTheme.typography.headlineMedium)

        Button(
            onClick = { navController.navigate("lista") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Solicitudes")
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