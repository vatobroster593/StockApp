package com.stockapp.ui.screens.ventas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaVentaScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Nueva Venta") }) }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Nueva Venta — Fase 3", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentasScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Historial de Ventas") }) }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Ventas — Fase 3", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleVentaScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Detalle de Venta") }) }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Detalle Venta — Fase 3", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
