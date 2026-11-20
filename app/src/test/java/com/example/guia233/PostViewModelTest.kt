package com.example.guia233

import com.example.guia233.model.Post
import com.example.guia233.viewmodel.PostViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest : StringSpec({

    "postList debe contener los datos esperados después de fetchPosts()" {
        // Subclase falsa de PostViewModel que no llama a la API real
        val fakePosts = listOf(
            Post(userId = 1, id = 1, title = "Título 1", body = "Contenido 1"),
            Post(userId = 2, id = 2, title = "Título 2", body = "Contenido 2")
        )

        val testViewModel = object : PostViewModel() {
            override fun fetchPosts() {
                _postList.value = fakePosts
            }
        }

        runTest {
            testViewModel.fetchPosts()
            testViewModel.postList.value shouldContainExactly fakePosts
        }
    }
})