package com.stockapp.ui.screens.ventas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.stockapp.data.local.relation.VentaConDetalle
import com.stockapp.domain.model.EstadoPago
import com.stockapp.ui.navigation.Screen
import com.stockapp.ui.screens.clientes.EstadoChip
import com.stockapp.ui.util.toDollarString
import com.stockapp.ui.util.toDateString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentasScreen(
    navController: NavController,
    viewModel: VentasViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Ventas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding)) {
            // Chips de filtro
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = uiState.filtroEstado == null,
                        onClick = { viewModel.setFiltroEstado(null) },
                        label = { Text("Todas") }
                    )
                }
                items(EstadoPago.entries) { ep ->
                    FilterChip(
                        selected = uiState.filtroEstado == ep.name,
                        onClick = { viewModel.setFiltroEstado(ep.name) },
                        label = { Text(ep.label) }
                    )
                }
            }

            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.ventas.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Filled.ReceiptLong, contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
                        Text("Sin ventas registradas",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                LazyColumn {
                    items(uiState.ventas, key = { it.venta.id }) { vd ->
                        VentaItem(
                            vd = vd,
                            onClick = { navController.navigate(Screen.DetalleVenta.createRoute(vd.venta.id)) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun VentaItem(vd: VentaConDetalle, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.ReceiptLong, contentDescription = null,
            tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = vd.cliente?.nombre ?: "Sin cliente",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${vd.venta.fecha.toDateString()} · ${vd.items.size} producto(s)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (vd.venta.estadoPago != EstadoPago.PAGADO.name && vd.saldoPendiente > 0) {
                Text(
                    "Pendiente: ${vd.saldoPendiente.toDollarString()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(vd.venta.total.toDollarString(), style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold)
            EstadoChip(estado = vd.venta.estadoPago)
        }
    }
}
