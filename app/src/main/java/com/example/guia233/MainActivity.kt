package com.example.guia233

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.guia233.navigation.AppNavigation
import com.example.guia233.ui.theme.Guia233Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Guia233Theme {
                Scaffold { innerPadding ->
                    // Carga la navegación de la app: parte en el Login y sigue al Menú, la lista de solicitudes y el formulario
                    Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}