package com.example.guia233.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val correoError: String? = null,
    val claveError: String? = null
)

class LoginViewModel : ViewModel() {

    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, correoError = null) }
    }

    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, claveError = null) }
    }

    fun validarFormulario(): Boolean {
        val e = _estado.value
        val correoError = if (!e.correo.contains("@")) "Correo inválido" else null
        val claveError = if (e.clave.length < 6) "Debe tener al menos 6 caracteres" else null

        _estado.update { it.copy(correoError = correoError, claveError = claveError) }

        return correoError == null && claveError == null
    }

    fun cerrarSesion() {
        _estado.value = LoginUiState()
    }
}