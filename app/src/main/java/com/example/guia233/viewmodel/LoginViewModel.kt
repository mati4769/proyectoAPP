package com.example.guia233.viewmodel

import androidx.lifecycle.ViewModel
import com.example.guia233.model.LoginErrores
import com.example.guia233.model.LoginUiState
import com.example.guia233.model.Usuario
import com.example.guia233.model.usuariosDemo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

// ViewModel del login: guarda el estado y valida los datos
class LoginViewModel : ViewModel() {

    // Estado que observa la pantalla (StateFlow)
    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado

    // Actualiza el campo correo y limpia su error
    fun onCorreoChange(valor: String) {
        _estado.update {
            it.copy(
                correo = valor,
                errores = it.errores.copy(correo = null)
            )
        }
    }

    // Actualiza el campo contraseña y limpia su error
    fun onClaveChange(valor: String) {
        _estado.update {
            it.copy(
                clave = valor,
                errores = it.errores.copy(clave = null)
            )
        }
    }

    // Validación del login
    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value

        var errores = LoginErrores(
            correo = if (!estadoActual.correo.contains("@"))
                "Correo inválido"
            else null,

            clave = if (estadoActual.clave.length < 6)
                "Debe tener al menos 6 caracteres"
            else null
        )

        // Si el formato está bien, se busca la cuenta ficticia
        var usuario: Usuario? = null
        if (errores.correo == null && errores.clave == null) {
            usuario = usuariosDemo.find { it.correo == estadoActual.correo && it.clave == estadoActual.clave }
            if (usuario == null) {
                errores = errores.copy(clave = "Correo o contraseña incorrectos")
            }
        }

        _estado.update {
            it.copy(errores = errores, usuario = usuario)
        }

        // true si encontró la cuenta
        return usuario != null
    }

    // Limpia el login al cerrar sesión
    fun cerrarSesion() {
        _estado.value = LoginUiState()
    }
}