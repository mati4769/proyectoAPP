package com.example.guia233.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

// Convierte un texto (el código de la solicitud) en una imagen QR
fun generarQr(texto: String, tamano: Int = 512): Bitmap {
    // ZXing calcula los puntos del QR
    val matriz = QRCodeWriter().encode(texto, BarcodeFormat.QR_CODE, tamano, tamano)
    // Imagen vacía donde se dibuja el QR
    val bitmap = Bitmap.createBitmap(tamano, tamano, Bitmap.Config.RGB_565)

    // Se pinta cada punto: negro si es parte del QR, blanco si no
    for (x in 0 until tamano) {
        for (y in 0 until tamano) {
            bitmap.setPixel(x, y, if (matriz[x, y]) Color.BLACK else Color.WHITE)
        }
    }
    return bitmap
}