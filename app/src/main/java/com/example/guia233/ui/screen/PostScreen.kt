package com.example.guia233.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.guia233.viewmodel.PostViewModel

// Pantalla que muestra los datos de prueba obtenidos desde la API REST
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsScreen(viewModel: PostViewModel) {
    // Observamos el flujo de datos del ViewModel
    val posts = viewModel.postList.collectAsState().value
    // Scaffold con TopAppBar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Solicitudes desde API") },
                // Botón para crear un post
                actions = {
                    TextButton(onClick = { viewModel.createPost() }) { Text("POST") }
                }
            )
        }
    ) { innerPadding ->
        // Aplicamos el padding de seguridad del sistema
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            // Lista de publicaciones
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp) // Espaciado interior del contenido
            ) {
                items(posts) { post ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Título: ${post.title}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = post.body,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            // Botones PUT, PATCH y DELETE
                            Row {
                                TextButton(onClick = { viewModel.updatePost(post) }) { Text("PUT") }
                                TextButton(onClick = { viewModel.patchPost(post) }) { Text("PATCH") }
                                TextButton(onClick = { viewModel.deletePost(post) }) { Text("DELETE") }
                            }
                        }
                    }
                }
            }
        }
    }
}