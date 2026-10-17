package com.example.guia233.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guia233.model.data.local.AppDatabase
import com.example.guia233.model.data.local.SolicitudEntity
import com.example.guia233.model.data.repository.SolicitudRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SolicitudUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: String = "",
    val servicio: String = "",
    val tituloError: String? = null,
    val descripcionError: String? = null,
    val categoriaError: String? = null,
    val servicioError: String? = null,
    val guardando: Boolean = false,
    val guardadoExitosamente: Boolean = false
)

class SolicitudViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = SolicitudRepository(AppDatabase.getDatabase(app).solicitudDao())

    val solicitudes = repository.solicitudes

    private val _estado = MutableStateFlow(SolicitudUiState())
    val estado: StateFlow<SolicitudUiState> = _estado

    fun onTituloChange(valor: String) {
        _estado.update { it.copy(titulo = valor, tituloError = null) }
    }

    fun onDescripcionChange(valor: String) {
        _estado.update { it.copy(descripcion = valor, descripcionError = null) }
    }

    fun onCategoriaChange(valor: String) {
        _estado.update { it.copy(categoria = valor, categoriaError = null) }
    }

    fun onServicioChange(valor: String) {
        _estado.update { it.copy(servicio = valor, servicioError = null) }
    }

    fun validarFormulario(): Boolean {
        val e = _estado.value
        val tituloError = if (e.titulo.isBlank()) "Campo obligatorio" else null
        val descripcionError = if (e.descripcion.length < 10) "Debe tener al menos 10 caracteres" else null
        val categoriaError = if (e.categoria.isBlank()) "Campo obligatorio" else null
        val servicioError = if (e.servicio.isBlank()) "Campo obligatorio" else null

        _estado.update {
            it.copy(
                tituloError = tituloError,
                descripcionError = descripcionError,
                categoriaError = categoriaError,
                servicioError = servicioError
            )
        }

        return listOfNotNull(tituloError, descripcionError, categoriaError, servicioError).isEmpty()
    }

    fun guardar(onListo: () -> Unit) {
        val s = _estado.value
        _estado.update { it.copy(guardando = true) }
        viewModelScope.launch {
            repository.insertar(
                SolicitudEntity(
                    codigo = "SOL-" + System.currentTimeMillis(),
                    titulo = s.titulo,
                    descripcion = s.descripcion,
                    categoria = s.categoria,
                    servicio = s.servicio,
                    estado = "Recibida",
                    fechaCreacion = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                )
            )
            _estado.value = SolicitudUiState(guardadoExitosamente = true)
            onListo()
        }
    }
}