package com.example.guia233.ui.theme

import androidx.compose.ui.graphics.Color

// ---------- Colores de la marca (empresa de servicios informáticos) ----------
val AzulOscuro = Color(0xFF0D3B66)
val AzulClaro = Color(0xFFD6E6F5)
val Turquesa = Color(0xFF00897B)
val TurquesaClaro = Color(0xFFCCEFEA)
val FondoClaro = Color(0xFFF4F7FB)

val AzulNoche = Color(0xFF9CC7F0)
val AzulNocheContenedor = Color(0xFF1E3A56)
val TurquesaNoche = Color(0xFF66CDBF)
val FondoOscuro = Color(0xFF111821)
val SuperficieOscura = Color(0xFF1A2430)

// ---------- Un color por cada estado de la solicitud ----------
val ColorRecibida = Color(0xFF1E88E5)
val ColorEnRevision = Color(0xFF8E24AA)
val ColorPendiente = Color(0xFFEF6C00)
val ColorAsignada = Color(0xFF3949AB)
val ColorEnProceso = Color(0xFF00897B)
val ColorResuelta = Color(0xFF2E7D32)
val ColorCerrada = Color(0xFF546E7A)
val ColorCancelada = Color(0xFFC62828)

// ---------- Un color por cada prioridad ----------
val ColorBaja = Color(0xFF43A047)
val ColorMedia = Color(0xFFF9A825)
val ColorAlta = Color(0xFFEF6C00)
val ColorUrgente = Color(0xFFC62828)
val ColorSinDefinir = Color(0xFF78909C)

// Devuelve el color según el estado
fun colorEstado(estado: String): Color {
    return when (estado) {
        "Recibida" -> ColorRecibida
        "En revisión" -> ColorEnRevision
        "Pendiente de información" -> ColorPendiente
        "Asignada" -> ColorAsignada
        "En proceso" -> ColorEnProceso
        "Resuelta" -> ColorResuelta
        "Cerrada" -> ColorCerrada
        "Cancelada" -> ColorCancelada
        else -> ColorSinDefinir
    }
}

// Devuelve el color según la prioridad
fun colorPrioridad(prioridad: String): Color {
    return when (prioridad) {
        "Baja" -> ColorBaja
        "Media" -> ColorMedia
        "Alta" -> ColorAlta
        "Urgente" -> ColorUrgente
        else -> ColorSinDefinir
    }
}