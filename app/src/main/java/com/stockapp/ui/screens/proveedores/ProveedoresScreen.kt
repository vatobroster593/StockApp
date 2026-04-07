package com.stockapp.ui.screens.proveedores

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProveedoresScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Proveedores") }) }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Text("Proveedores — Fase 5", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProveedorScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Detalle Proveedor") }) }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Text("Detalle Proveedor — Fase 5", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProveedorScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Agregar Proveedor") }) }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Text("Agregar Proveedor — Fase 5", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaCompraScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Nueva Compra") }) }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Text("Nueva Compra — Fase 5", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
