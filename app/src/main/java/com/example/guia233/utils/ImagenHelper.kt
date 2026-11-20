package com.example.guia233.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File

// Copia la imagen elegida a la carpeta de la app y devuelve su ruta.
// Así la evidencia se puede ver aunque se cierre la app.
fun copiarImagen(context: Context, uri: Uri): String {
    val archivo = File(context.filesDir, "evidencia_${System.currentTimeMillis()}.jpg")
    context.contentResolver.openInputStream(uri)?.use { entrada ->
        archivo.outputStream().use { salida ->
            entrada.copyTo(salida)
        }
    }
    return archivo.absolutePath
}

// Lee una imagen guardada (4 veces más pequeña para no usar tanta memoria)
fun cargarImagen(ruta: String): Bitmap? {
    val opciones = BitmapFactory.Options()
    opciones.inSampleSize = 4
    return BitmapFactory.decodeFile(ruta, opciones)
}