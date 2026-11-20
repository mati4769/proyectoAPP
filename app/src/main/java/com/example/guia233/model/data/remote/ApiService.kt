package com.example.guia233.model.data.remote

import com.example.guia233.model.Post
import retrofit2.http.*

// Esta interfaz define los endpoints HTTP
interface ApiService {

    // Define una solicitud GET al endpoint /posts
    @GET("/posts")
    suspend fun getPosts(): List<Post>

    // Crear un post
    @POST("/posts")
    suspend fun createPost(@Body post: Post): Post

    // Reemplazar un post completo
    @PUT("/posts/{id}")
    suspend fun updatePost(@Path("id") id: Int, @Body post: Post): Post

    // Modificar solo algunos campos
    @PATCH("/posts/{id}")
    suspend fun patchPost(@Path("id") id: Int, @Body campos: Map<String, String>): Post

    // Eliminar un post
    @DELETE("/posts/{id}")
    suspend fun deletePost(@Path("id") id: Int)
}