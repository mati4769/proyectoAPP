package com.example.guia233.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.guia233.ui.screen.LoginScreen
import com.example.guia233.ui.screen.MenuScreen
import com.example.guia233.ui.screen.ListaSolicitudesScreen
import com.example.guia233.ui.screen.QrScannerScreen
import com.example.guia233.ui.screen.SolicitudFormScreen
import com.example.guia233.viewmodel.SolicitudViewModel
import com.example.guia233.viewmodel.LoginViewModel

@Composable
fun AppNavigation(
    hasCameraPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    val navController = rememberNavController()

    val loginViewModel: LoginViewModel = viewModel()
    val solicitudViewModel: SolicitudViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            LoginScreen(navController, loginViewModel)
        }
        composable(route = "menu") {
            MenuScreen(navController, onCerrarSesion = loginViewModel::cerrarSesion)
        }
        composable(route = "lista") {
            ListaSolicitudesScreen(navController, solicitudViewModel)
        }
        composable(route = "formulario") {
            SolicitudFormScreen(navController, solicitudViewModel)
        }
        composable(route = "qr") {
            QrScannerScreen(navController, solicitudViewModel, hasCameraPermission, onRequestPermission)
        }
    }
}