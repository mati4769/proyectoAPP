package com.example.guia233.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guia233.model.Post
import com.example.guia233.model.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel que mantiene el estado de los datos obtenidos desde la API
open class PostViewModel : ViewModel() {

    protected val repository = PostRepository()
    // Flujo mutable que contiene la lista de posts
    protected val _postList = MutableStateFlow<List<Post>>(emptyList())
    // Flujo público de solo lectura
    open val postList: StateFlow<List<Post>> = _postList
    // Se llama automáticamente al iniciar
    init {
        fetchPosts()
    }
    // Función que obtiene los datos en segundo plano
    open fun fetchPosts() {
        viewModelScope.launch {
            try {
                _postList.value = repository.getPosts().take(10)
            } catch (e: Exception) {
                println("Error al obtener datos: ${e.localizedMessage}")
            }
        }
    }

    // POST: crea un post nuevo
    fun createPost() {
        viewModelScope.launch {
            try {
                val nuevo = repository.createPost(
                    Post(userId = 1, id = 0, title = "Nuevo post", body = "Creado con POST")
                )
                _postList.value = listOf(nuevo) + _postList.value
            } catch (e: Exception) {
                println("Error en POST: ${e.localizedMessage}")
            }
        }
    }

    // PUT: reemplaza un post completo
    fun updatePost(post: Post) {
        viewModelScope.launch {
            try {
                val actualizado = repository.updatePost(
                    post.copy(title = "Post actualizado (PUT)", body = "Contenido reemplazado")
                )
                _postList.value = _postList.value.map { if (it.id == post.id) actualizado else it }
            } catch (e: Exception) {
                println("Error en PUT: ${e.localizedMessage}")
            }
        }
    }

    // PATCH: modifica solo el título
    fun patchPost(post: Post) {
        viewModelScope.launch {
            try {
                val nuevoTitulo = "Título modificado (PATCH)"
                repository.patchPost(post.id, mapOf("title" to nuevoTitulo))
                _postList.value = _postList.value.map { if (it.id == post.id) it.copy(title = nuevoTitulo) else it }
            } catch (e: Exception) {
                println("Error en PATCH: ${e.localizedMessage}")
            }
        }
    }

    // DELETE: elimina un post
    fun deletePost(post: Post) {
        viewModelScope.launch {
            try {
                repository.deletePost(post.id)
                _postList.value = _postList.value.filter { it.id != post.id }
            } catch (e: Exception) {
                println("Error en DELETE: ${e.localizedMessage}")
            }
        }
    }
}