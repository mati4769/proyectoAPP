package com.example.guia233.viewmodel

import androidx.lifecycle.ViewModel
import com.example.guia233.model.Usuario
import com.example.guia233.model.usuariosDemo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

// Estado del login: campos, errores y usuario que ingresó
data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    // Mensajes de error (null = sin error)
    val correoError: String? = null,
    val claveError: String? = null,
    // Cuenta encontrada al ingresar
    val usuario: Usuario? = null
)

// ViewModel del login: guarda el estado y valida los datos
class LoginViewModel : ViewModel() {

    // Estado que observa la pantalla (StateFlow)
    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado

    // Actualiza el correo y borra su error
    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, correoError = null) }
    }

    // Actualiza la contraseña y borra su error
    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, claveError = null) }
    }

    // Revisa el formato del correo y la contraseña
    fun validarFormulario(): Boolean {
        val e = _estado.value
        val correoError = if (!e.correo.contains("@")) "Correo inválido" else null
        var claveError = if (e.clave.length < 6) "Debe tener al menos 6 caracteres" else null

        // Si el formato está bien, se busca la cuenta ficticia
        var usuario: Usuario? = null
        if (correoError == null && claveError == null) {
            usuario = usuariosDemo.find { it.correo == e.correo && it.clave == e.clave }
            if (usuario == null) {
                claveError = "Correo o contraseña incorrectos"
            }
        }

        _estado.update { it.copy(correoError = correoError, claveError = claveError, usuario = usuario) }

        // true si encontró la cuenta
        return usuario != null
    }

    // Limpia el login al cerrar sesión
    fun cerrarSesion() {
        _estado.value = LoginUiState()
    }
}