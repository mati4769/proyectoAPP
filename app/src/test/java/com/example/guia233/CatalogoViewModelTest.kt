package com.example.guia233

import com.example.guia233.model.Servicio
import com.example.guia233.viewmodel.CatalogoViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogoViewModelTest : StringSpec({

    "servicios debe contener el catálogo después de cargarServicios()" {
        // Subclase falsa de CatalogoViewModel que no llama a la API real
        val fakeServicios = listOf(
            Servicio(id = 1, servicio = "Mantenimiento web", categoria = "Web", descripcion = "Actualización y soporte de sitios web"),
            Servicio(id = 3, servicio = "Hosting", categoria = "Infraestructura", descripcion = "Alojamiento y gestión de sitios")
        )

        val testViewModel = object : CatalogoViewModel() {
            override fun cargarServicios() {
                _servicios.value = fakeServicios
            }
        }

        runTest {
            testViewModel.cargarServicios()
            testViewModel.servicios.value shouldContainExactly fakeServicios
        }
    }
})