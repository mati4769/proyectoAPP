package com.example.guia233.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

// Ayudante para revisar el permiso de cámara
object CameraPermissionHelper {
    // Devuelve true si la app ya tiene permiso de cámara
    fun hasCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
}