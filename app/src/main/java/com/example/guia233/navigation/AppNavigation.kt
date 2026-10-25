package com.example.guia233.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.guia233.ui.screen.DetalleSolicitudScreen
import com.example.guia233.ui.screen.LoginScreen
import com.example.guia233.ui.screen.MenuScreen
import com.example.guia233.ui.screen.ListaSolicitudesScreen
import com.example.guia233.ui.screen.QrScannerScreen
import com.example.guia233.ui.screen.ResumenScreen
import com.example.guia233.ui.screen.SolicitudFormScreen
import com.example.guia233.viewmodel.SolicitudViewModel
import com.example.guia233.viewmodel.LoginViewModel

// Navegación: define todas las pantallas (rutas) de la app
@Composable
fun AppNavigation(
    hasCameraPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    // Controla el cambio entre pantallas
    val navController = rememberNavController()

    // Los ViewModels se crean una sola vez y se comparten entre pantallas
    val loginViewModel: LoginViewModel = viewModel()
    val solicitudViewModel: SolicitudViewModel = viewModel()

    // Usuario que inició sesión
    val loginEstado by loginViewModel.estado.collectAsState()

    // La app parte en el login
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Login: al ingresar le avisa al SolicitudViewModel quién entró
        composable(route = "login") {
            LoginScreen(navController, loginViewModel, onIngresar = solicitudViewModel::iniciarSesion)
        }
        // Menú con las opciones según el perfil
        composable(route = "menu") {
            MenuScreen(navController, loginEstado.usuario, onCerrarSesion = loginViewModel::cerrarSesion)
        }
        // Lista de solicitudes con búsqueda y filtros
        composable(route = "lista") {
            ListaSolicitudesScreen(navController, solicitudViewModel)
        }
        // Formulario para crear o editar una solicitud
        composable(route = "formulario") {
            SolicitudFormScreen(navController, solicitudViewModel)
        }
        // Detalle de una solicitud con su QR, acciones e historial
        composable(route = "detalle") {
            DetalleSolicitudScreen(navController, solicitudViewModel)
        }
        // Resumen general (solo administrador)
        composable(route = "resumen") {
            ResumenScreen(solicitudViewModel)
        }
        // Escáner de códigos QR
        composable(route = "qr") {
            QrScannerScreen(navController, solicitudViewModel, hasCameraPermission, onRequestPermission)
        }
    }
}