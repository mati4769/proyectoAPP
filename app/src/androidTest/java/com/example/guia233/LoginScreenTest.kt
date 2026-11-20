package com.example.guia233

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.guia233.ui.screen.LoginScreen
import com.example.guia233.viewmodel.LoginViewModel
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun correo_sin_arroba_muestra_error_en_el_formulario() {
        // Renderizamos el formulario de login con su ViewModel
        composeRule.setContent {
            LoginScreen(
                navController = rememberNavController(),
                viewModel = LoginViewModel(),
                onIngresar = {}
            )
        }
        // Escribimos un correo sin @ y una contraseña, y presionamos Ingresar
        composeRule.onNodeWithTag("campoCorreo").performTextInput("clientepymedemo.cl")
        composeRule.onNodeWithTag("campoClave").performTextInput("cliente123")
        composeRule.onNodeWithText("Ingresar").performClick()
        // El error que controla el ViewModel debe verse en el campo
        composeRule.onNodeWithText("Correo inválido").assertIsDisplayed()
    }
}