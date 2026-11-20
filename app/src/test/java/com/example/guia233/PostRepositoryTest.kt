package com.example.guia233

import com.example.guia233.model.Post
import com.example.guia233.model.data.remote.ApiService
import com.example.guia233.model.data.repository.PostRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

// Subclase de PostRepository para poder inyectar el ApiService manualmente
class TestablePostRepository(private val testApi: ApiService) : PostRepository() {
    override suspend fun getPosts(): List<Post> {
        return testApi.getPosts()
    }
}

class PostRepositoryTest : StringSpec({
    "getPosts() debe retornar una lista de posts simulada" {
        // 1. Simulamos el resultado de la API
        val fakePosts = listOf(
            Post(userId = 1, id = 1, title = "Título 1", body = "Cuerpo 1"),
            Post(userId = 2, id = 2, title = "Título 2", body = "Cuerpo 2")
        )
        // 2. Creamos un mock de ApiService
        val mockApi = mockk<ApiService>()
        coEvery { mockApi.getPosts() } returns fakePosts
        // 3. Usamos la clase de test inyectando el mock
        val repo = TestablePostRepository(mockApi)
        // 4. Ejecutamos el test
        runTest {
            val result = repo.getPosts()
            result shouldContainExactly fakePosts
        }
    }
})