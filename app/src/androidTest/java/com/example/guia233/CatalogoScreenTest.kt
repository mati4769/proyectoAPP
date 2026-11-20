package com.example.guia233

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.guia233.model.Servicio
import com.example.guia233.ui.screen.CatalogoScreen
import com.example.guia233.viewmodel.CatalogoViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class CatalogoScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()
    @Test
    fun el_catalogo_debe_mostrar_los_servicios_en_pantalla() {
        // Simulamos los datos que el ViewModel entregaría
        val fakeServicios = listOf(
            Servicio(id = 1, servicio = "Mantenimiento web", categoria = "Web", descripcion = "Actualización y soporte de sitios web"),
            Servicio(id = 3, servicio = "Hosting", categoria = "Infraestructura", descripcion = "Alojamiento y gestión de sitios")
        )
        // Subclase falsa de CatalogoViewModel con StateFlow simulado
        val fakeViewModel = object : CatalogoViewModel() {
            override val servicios = MutableStateFlow(fakeServicios)
        }
        // Renderizamos el catálogo con el ViewModel falso
        composeRule.setContent {
            CatalogoScreen(viewModel = fakeViewModel, onSeleccionar = {})
        }
        // Validamos que los servicios se muestran en la UI
        composeRule.onNodeWithText("Mantenimiento web").assertIsDisplayed()
        composeRule.onNodeWithText("Hosting").assertIsDisplayed()
    }
}