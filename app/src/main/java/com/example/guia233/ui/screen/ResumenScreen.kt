package com.example.guia233.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.guia233.viewmodel.UsuarioViewModel

@Composable
fun ResumenScreen(viewModel: UsuarioViewModel) {
    val ultimo by viewModel.ultimoUsuario.collectAsState(initial = null)

    Column(Modifier.padding(all = 16.dp)) {
        Text("Resumen del Registro", style = MaterialTheme.typography.headlineMedium)
        Text("Nombre: ${ultimo?.nombre ?: ""}")
        Text("Correo: ${ultimo?.correo ?: ""}")
        Text("Dirección: ${ultimo?.direccion ?: ""}")
        Text("Contraseña: ${"*".repeat(ultimo?.clave?.length ?: 0)}")
        Text("Términos: ${if (ultimo?.aceptaTerminos == true) "Aceptados" else "No aceptados"}")
    }
}