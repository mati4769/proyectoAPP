package com.example.guia233

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.guia233.model.Post
import com.example.guia233.ui.screen.PostsScreen
import com.example.guia233.viewmodel.PostViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class PostScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()
    @Test
    fun el_titulo_de_post_debe_aparecer_en_pantalla() {
        // Simulamos los datos que el ViewModel entregaría
        val fakePosts = listOf(
            Post(userId = 1, id = 1, title = "Título 1", body = "Contenido 1"),
            Post(userId = 2, id = 2, title = "Título 2", body = "Contenido 2")
        )
        // Subclase falsa de PostViewModel con StateFlow simulado
        val fakeViewModel = object : PostViewModel() {
            override val postList = MutableStateFlow(fakePosts)
        }
        // Renderizamos el PostScreen con el ViewModel falso
        composeRule.setContent {
            PostsScreen(viewModel = fakeViewModel)
        }
        // Validamos que los títulos se muestran correctamente en la UI
        composeRule.onNodeWithText("Título: Título 1").assertIsDisplayed()
        composeRule.onNodeWithText("Título: Título 2").assertIsDisplayed()
    }
}