package com.example.guia233.ui.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.guia233.utils.QrScanner
import com.example.guia233.viewmodel.SolicitudViewModel

// Pantalla para escanear el QR de una solicitud
@Composable
fun QrScannerScreen(
    navController: NavController,
    viewModel: SolicitudViewModel,
    hasCameraPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    // Observa el código leído y la solicitud encontrada
    val qrLeido by viewModel.qrLeido.collectAsState()
    val solicitud by viewModel.solicitudQr.collectAsState()
    // Contexto para mostrar el mensaje (Toast)
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1) Sin permiso: pide permiso de cámara
        if (!hasCameraPermission) {
            Text(
                "Permiso de cámara requerido",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Para escanear códigos QR, necesitamos acceso a la cámara",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            Button(onClick = onRequestPermission) {
                Text("Conceder permiso de cámara")
            }
        } else if (qrLeido == null) {
            // 2) Con permiso y sin leer: muestra la cámara
            Text(
                "Escanea el QR de una solicitud",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                // Cámara que lee el QR y se lo pasa al ViewModel
                QrScanner(
                    onQrCodeScanned = { qrContent ->
                        viewModel.onQrDetected(qrContent)
                        Toast.makeText(context, "QR detectado!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Marco para ayudar al escaneo
                Surface(
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.Center),
                    color = Color.Transparent,
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                ) {}
            }

            Spacer(Modifier.height(16.dp))
            Text(
                "Enfoca el código QR en el marco central",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            // 3) QR leído: muestra la solicitud encontrada
            Text(
                "QR Detectado:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(Modifier.padding(16.dp)) {
                    val s = solicitud
                    if (s != null) {
                        Text(s.codigo, style = MaterialTheme.typography.titleMedium)
                        Text("Título: ${s.titulo}")
                        Text("Descripción: ${s.descripcion}")
                        Text("Categoría: ${s.categoria}")
                        Text("Estado: ${s.estado}")
                    } else {
                        Text("No existe una solicitud con el código: $qrLeido")
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            // Vuelve a mostrar la cámara
            Button(
                onClick = { viewModel.clearResult() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Escanear otro código QR")
            }
            // Vuelve a la pantalla anterior (menú o lista)
            Button(
                onClick = {
                    viewModel.clearResult()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}