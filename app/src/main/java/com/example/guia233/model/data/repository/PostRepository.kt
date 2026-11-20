package com.example.guia233.model.data.repository

import com.example.guia233.model.Post
import com.example.guia233.model.data.remote.RetrofitInstance

// Este repositorio se encarga de acceder a los datos usando Retrofit
open class PostRepository {

    // Función que obtiene los posts desde la API
    open suspend fun getPosts(): List<Post> {
        return RetrofitInstance.api.getPosts()
    }

    // Crea un post (POST)
    suspend fun createPost(post: Post): Post {
        return RetrofitInstance.api.createPost(post)
    }

    // Reemplaza un post (PUT)
    suspend fun updatePost(post: Post): Post {
        return RetrofitInstance.api.updatePost(post.id, post)
    }

    // Modifica algunos campos (PATCH)
    suspend fun patchPost(id: Int, campos: Map<String, String>): Post {
        return RetrofitInstance.api.patchPost(id, campos)
    }

    // Elimina un post (DELETE)
    suspend fun deletePost(id: Int) {
        RetrofitInstance.api.deletePost(id)
    }
}