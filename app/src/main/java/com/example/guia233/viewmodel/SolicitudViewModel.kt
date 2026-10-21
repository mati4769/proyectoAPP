package com.example.guia233.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guia233.model.Usuario
import com.example.guia233.model.data.local.AppDatabase
import com.example.guia233.model.data.local.HistorialEntity
import com.example.guia233.model.data.local.SolicitudEntity
import com.example.guia233.model.data.repository.SolicitudRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
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
    val evidenciaUri: String? = null,
    val tituloError: String? = null,
    val descripcionError: String? = null,
    val categoriaError: String? = null,
    val servicioError: String? = null,
    val guardando: Boolean = false,
    val guardadoExitosamente: Boolean = false,
    val solicitudEditando: SolicitudEntity? = null
)

class SolicitudViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = SolicitudRepository(AppDatabase.getDatabase(app).solicitudDao())

    // Usuario que inició sesión
    private var usuario: Usuario? = null

    fun iniciarSesion(u: Usuario) {
        usuario = u
    }

    fun rolActual(): String {
        return usuario?.rol ?: ""
    }

    // Cada perfil ve solo las solicitudes que le corresponden
    val solicitudes = repository.solicitudes.map { lista ->
        when (usuario?.rol) {
            "Cliente" -> lista.filter { it.clienteCorreo == usuario?.correo }
            "Técnico" -> lista.filter { it.responsable == usuario?.correo }
            else -> lista
        }
    }

    // Fecha y hora actual en texto
    private fun ahora(): String {
        return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
    }

    // Solicitud elegida en la lista para ver su detalle
    var seleccionadaId: Long = 0

    fun verDetalle(id: Long) {
        seleccionadaId = id
    }

    // Historial de una solicitud, del más antiguo al más nuevo
    fun historial(solicitudId: Long) = repository.historial(solicitudId)

    // Guarda un cambio en el historial con la fecha y el usuario actual
    private suspend fun registrarHistorial(
        solicitudId: Long,
        estadoAnterior: String,
        estadoNuevo: String,
        accion: String,
        comentario: String = ""
    ) {
        repository.insertarHistorial(
            HistorialEntity(
                solicitudId = solicitudId,
                estadoAnterior = estadoAnterior,
                estadoNuevo = estadoNuevo,
                accion = accion,
                comentario = comentario,
                usuario = usuario?.correo ?: "",
                perfil = usuario?.rol ?: "",
                fecha = ahora()
            )
        )
    }
    private val _estado = MutableStateFlow(SolicitudUiState())
    val estado: StateFlow<SolicitudUiState> = _estado

    // Limpia el formulario para crear una solicitud nueva
    fun nuevaSolicitud() {
        _estado.value = SolicitudUiState()
    }

    // Carga una solicitud de la lista en el formulario para consultarla o editarla
    fun cargarSolicitud(solicitud: SolicitudEntity) {
        _estado.value = SolicitudUiState(
            titulo = solicitud.titulo,
            descripcion = solicitud.descripcion,
            categoria = solicitud.categoria,
            servicio = solicitud.servicio,
            evidenciaUri = solicitud.evidenciaUri,
            solicitudEditando = solicitud
        )
    }

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

    // Guarda la imagen elegida desde el dispositivo
    fun onEvidenciaChange(uri: String?) {
        _estado.update { it.copy(evidenciaUri = uri) }
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
            val editando = s.solicitudEditando
            if (editando != null) {
                // Editar: se mantienen el código, el estado y la fecha de creación
                repository.actualizar(
                    editando.copy(
                        titulo = s.titulo,
                        descripcion = s.descripcion,
                        categoria = s.categoria,
                        servicio = s.servicio,
                        evidenciaUri = s.evidenciaUri,
                        ultimaActualizacion = ahora()
                    )
                )
                registrarHistorial(editando.id, editando.estado, editando.estado, "Solicitud editada por el cliente")
            } else {
                val nuevoId = repository.insertar(
                    SolicitudEntity(
                        codigo = "SOL-" + System.currentTimeMillis(),
                        titulo = s.titulo,
                        descripcion = s.descripcion,
                        categoria = s.categoria,
                        servicio = s.servicio,
                        estado = "Recibida",
                        evidenciaUri = s.evidenciaUri,
                        fechaCreacion = ahora(),
                        clienteCorreo = usuario?.correo ?: "",
                        ultimaActualizacion = ahora()
                    )
                )
                registrarHistorial(nuevoId, "-", "Recibida", "Solicitud creada")
            }
            _estado.value = SolicitudUiState(guardadoExitosamente = true)
            onListo()
        }
    }

    // Lectura de código QR
    private val _qrLeido = MutableStateFlow<String?>(null)
    val qrLeido: StateFlow<String?> = _qrLeido

    private val _solicitudQr = MutableStateFlow<SolicitudEntity?>(null)
    val solicitudQr: StateFlow<SolicitudEntity?> = _solicitudQr

    fun onQrDetected(codigo: String) {
        viewModelScope.launch {
            val encontrada = repository.buscarPorCodigo(codigo)
            // El cliente no puede ver solicitudes de otros clientes
            if (usuario?.rol == "Cliente" && encontrada?.clienteCorreo != usuario?.correo) {
                _solicitudQr.value = null
            } else {
                _solicitudQr.value = encontrada
            }
            _qrLeido.value = codigo
        }
    }

    fun clearResult() {
        _qrLeido.value = null
        _solicitudQr.value = null
    }
}