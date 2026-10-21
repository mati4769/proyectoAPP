package com.example.guia233.model

// Usuario ficticio con su perfil: "Cliente", "Administrador" o "Técnico"
data class Usuario(
    val correo: String,
    val clave: String,
    val nombre: String,
    val rol: String
)

// Cuentas de prueba (todas ficticias)
val usuariosDemo = listOf(
    Usuario("cliente@pymedemo.cl", "cliente123", "Pyme Demo", "Cliente"),
    Usuario("cliente2@comerciodemo.cl", "cliente456", "Comercio Demo", "Cliente"),
    Usuario("admin@techsoluciones.cl", "admin123", "Coordinador Demo", "Administrador"),
    Usuario("tecnico@techsoluciones.cl", "tecnico123", "Técnico Uno", "Técnico"),
    Usuario("tecnico2@techsoluciones.cl", "tecnico456", "Técnico Dos", "Técnico")
)