package com.example.guia233.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guia233.model.Usuario
import com.example.guia233.model.usuariosDemo
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

// Estado de los campos de la pantalla de detalle
data class DetalleUiState(
    val comentario: String = "",
    val comentarioError: String? = null,
    val categoria: String = ""
)

// Filtros y búsqueda de la lista
data class FiltroUiState(
    val busqueda: String = "",
    val estado: String = "Todos",
    val prioridad: String = "Todas",
    val cliente: String = "Todos",
    val responsable: String = "Todos",
    val soloAtrasadas: Boolean = false,
    val mostrarFiltros: Boolean = false
)

class SolicitudViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = SolicitudRepository(AppDatabase.getDatabase(app).solicitudDao())

    // Usuario que inició sesión
    private var usuario: Usuario? = null

    fun iniciarSesion(u: Usuario) {
        usuario = u
        _filtro.value = FiltroUiState()
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
        _detalle.value = DetalleUiState()
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
                // Se deja seleccionada para abrir su detalle (con su QR) al terminar
                verDetalle(nuevoId)
            }
            _estado.value = SolicitudUiState(guardadoExitosamente = true)
            onListo()
        }
    }

    // ---------- Acciones del detalle (administrador, técnico y cliente) ----------

    private val _detalle = MutableStateFlow(DetalleUiState())
    val detalle: StateFlow<DetalleUiState> = _detalle

    fun onComentarioChange(valor: String) {
        _detalle.update { it.copy(comentario = valor, comentarioError = null) }
    }

    fun onCategoriaDetalleChange(valor: String) {
        _detalle.update { it.copy(categoria = valor) }
    }

    // Revisa que se haya escrito un comentario cuando la acción lo necesita
    private fun comentarioValido(): Boolean {
        if (_detalle.value.comentario.isBlank()) {
            _detalle.update { it.copy(comentarioError = "Escribe un comentario") }
            return false
        }
        return true
    }

    // Cambia el estado y lo deja registrado en el historial
    fun cambiarEstado(s: SolicitudEntity, nuevoEstado: String, accion: String, pideComentario: Boolean = false) {
        if (pideComentario && !comentarioValido()) return
        val comentario = _detalle.value.comentario
        viewModelScope.launch {
            repository.actualizar(
                s.copy(
                    estado = nuevoEstado,
                    ultimaActualizacion = ahora(),
                    resolucion = if (nuevoEstado == "Resuelta") comentario else s.resolucion
                )
            )
            registrarHistorial(s.id, s.estado, nuevoEstado, accion, comentario)
            _detalle.update { it.copy(comentario = "") }
        }
    }

    fun cambiarPrioridad(s: SolicitudEntity, prioridad: String) {
        viewModelScope.launch {
            repository.actualizar(s.copy(prioridad = prioridad, ultimaActualizacion = ahora()))
            registrarHistorial(s.id, s.estado, s.estado, "Prioridad cambiada a $prioridad")
        }
    }

    fun cambiarCategoria(s: SolicitudEntity) {
        val categoria = _detalle.value.categoria
        if (categoria.isBlank()) return
        viewModelScope.launch {
            repository.actualizar(s.copy(categoria = categoria, ultimaActualizacion = ahora()))
            registrarHistorial(s.id, s.estado, s.estado, "Clasificada como $categoria")
            _detalle.update { it.copy(categoria = "") }
        }
    }

    fun asignar(s: SolicitudEntity, tecnico: Usuario) {
        viewModelScope.launch {
            repository.actualizar(
                s.copy(responsable = tecnico.correo, estado = "Asignada", ultimaActualizacion = ahora())
            )
            registrarHistorial(s.id, s.estado, "Asignada", "Asignada a ${tecnico.nombre}")
        }
    }

    // Agrega un comentario o avance sin cambiar el estado
    fun comentar(s: SolicitudEntity, accion: String) {
        if (!comentarioValido()) return
        val comentario = _detalle.value.comentario
        viewModelScope.launch {
            repository.actualizar(s.copy(ultimaActualizacion = ahora()))
            registrarHistorial(s.id, s.estado, s.estado, accion, comentario)
            _detalle.update { it.copy(comentario = "") }
        }
    }

    // Elimina una solicitud finalizada (solo la usa el administrador)
    fun eliminar(s: SolicitudEntity) {
        viewModelScope.launch {
            repository.eliminar(s)
        }
    }

    // ---------- Filtros, búsqueda y atrasadas ----------

    // Opciones de cada filtro (la primera significa "sin filtrar")
    val estados = listOf(
        "Todos", "Recibida", "En revisión", "Pendiente de información",
        "Asignada", "En proceso", "Resuelta", "Cerrada", "Cancelada"
    )
    val prioridades = listOf("Todas", "Sin definir", "Baja", "Media", "Alta", "Urgente")
    val clientes = listOf("Todos") + usuariosDemo.filter { it.rol == "Cliente" }.map { it.correo }
    val responsables = listOf("Todos") + usuariosDemo.filter { it.rol == "Técnico" }.map { it.correo }

    private val _filtro = MutableStateFlow(FiltroUiState())
    val filtro: StateFlow<FiltroUiState> = _filtro

    fun onBusquedaChange(valor: String) {
        _filtro.update { it.copy(busqueda = valor) }
    }

    // Devuelve la opción que sigue en la lista (al llegar al final vuelve al inicio)
    private fun siguiente(opciones: List<String>, actual: String): String {
        val i = opciones.indexOf(actual)
        return opciones[(i + 1) % opciones.size]
    }

    fun cambiarFiltroEstado() {
        _filtro.update { it.copy(estado = siguiente(estados, it.estado)) }
    }

    fun cambiarFiltroPrioridad() {
        _filtro.update { it.copy(prioridad = siguiente(prioridades, it.prioridad)) }
    }

    fun cambiarFiltroCliente() {
        _filtro.update { it.copy(cliente = siguiente(clientes, it.cliente)) }
    }

    fun cambiarFiltroResponsable() {
        _filtro.update { it.copy(responsable = siguiente(responsables, it.responsable)) }
    }

    fun cambiarSoloAtrasadas() {
        _filtro.update { it.copy(soloAtrasadas = !it.soloAtrasadas) }
    }

    // Muestra u oculta los botones de filtros
    fun cambiarMostrarFiltros() {
        _filtro.update { it.copy(mostrarFiltros = !it.mostrarFiltros) }
    }

    // Limpia los filtros, pero deja los botones abiertos si estaban abiertos
    fun limpiarFiltros() {
        _filtro.value = FiltroUiState(mostrarFiltros = _filtro.value.mostrarFiltros)
    }

    // Tiempo máximo sin resolver: 3 días.
    // Para probarlo en la demo se puede cambiar a 2 * 60 * 1000L (2 minutos).
    private val limiteAtraso = 3 * 24 * 60 * 60 * 1000L

    // Una solicitud está atrasada si sigue abierta y pasó el tiempo límite
    fun esAtrasada(s: SolicitudEntity): Boolean {
        val terminada = s.estado == "Resuelta" || s.estado == "Cerrada" || s.estado == "Cancelada"
        return !terminada && System.currentTimeMillis() - s.creadaEnMillis > limiteAtraso
    }

    // Aplica la búsqueda y todos los filtros a la lista
    fun aplicarFiltros(lista: List<SolicitudEntity>, f: FiltroUiState): List<SolicitudEntity> {
        return lista.filter { s ->
            val coincideBusqueda = f.busqueda.isBlank() ||
                    s.codigo.contains(f.busqueda, ignoreCase = true) ||
                    s.titulo.contains(f.busqueda, ignoreCase = true)
            // Las finalizadas no se muestran en "Todos", solo si se elige ese estado
            val finalizada = s.estado == "Cerrada" || s.estado == "Cancelada"
            val coincideEstado = if (f.estado == "Todos") !finalizada else s.estado == f.estado
            val coincidePrioridad = f.prioridad == "Todas" || s.prioridad == f.prioridad
            val coincideCliente = f.cliente == "Todos" || s.clienteCorreo == f.cliente
            val coincideResponsable = f.responsable == "Todos" || s.responsable == f.responsable
            val coincideAtraso = !f.soloAtrasadas || esAtrasada(s)

            coincideBusqueda && coincideEstado && coincidePrioridad &&
                    coincideCliente && coincideResponsable && coincideAtraso
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