package com.example.guia233

import com.example.guia233.model.Servicio
import com.example.guia233.model.data.remote.ApiService
import com.example.guia233.model.data.repository.ServicioRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

// Subclase de ServicioRepository para poder inyectar el ApiService manualmente
class TestableServicioRepository(private val testApi: ApiService) : ServicioRepository() {
    override suspend fun getServicios(): List<Servicio> {
        return testApi.getServicios()
    }
}

class ServicioRepositoryTest : StringSpec({
    "getServicios() debe retornar el catálogo simulado" {
        // 1. Simulamos el resultado de la API
        val fakeServicios = listOf(
            Servicio(id = 1, servicio = "Mantenimiento web", categoria = "Web", descripcion = "Actualización y soporte de sitios web"),
            Servicio(id = 2, servicio = "Soporte de correo", categoria = "Soporte", descripcion = "Configuración y solución de problemas")
        )
        // 2. Creamos un mock de ApiService
        val mockApi = mockk<ApiService>()
        coEvery { mockApi.getServicios() } returns fakeServicios
        // 3. Usamos la clase de test inyectando el mock
        val repo = TestableServicioRepository(mockApi)
        // 4. Ejecutamos el test
        runTest {
            val result = repo.getServicios()
            result shouldContainExactly fakeServicios
        }
    }
})