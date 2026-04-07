package com.stockapp.ui.screens.clientes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientesScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Clientes") }) }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Clientes — Fase 3", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
