package com.stockapp.ui.screens.clientes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.stockapp.ui.screens.ventas.VentasViewModel
import com.stockapp.ui.util.toDollarString
import com.stockapp.ui.util.toDateString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleClienteScreen(
    navController: NavController,
    clientesViewModel: ClientesViewModel = hiltViewModel(),
    ventasViewModel: VentasViewModel = hiltViewModel()
) {
    val clienteId = navController.currentBackStackEntry
        ?.arguments?.getLong("clienteId") ?: return

    val clientesState by clientesViewModel.uiState.collectAsStateWithLifecycle()
    val ventasState by ventasViewModel.uiState.collectAsStateWithLifecycle()

    val cc = clientesState.clientes.find { it.cliente.id == clienteId }
    val ventas = ventasState.ventas.filter { it.venta.clienteId == clienteId }
    val cliente = cc?.cliente

    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(cliente?.nombre ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (cliente != null) {
                        IconButton(onClick = {
                            navController.navigate(Screen.EditarCliente.createRoute(clienteId))
                        }) { Icon(Icons.Filled.Edit, contentDescription = "Editar") }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (cliente == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(bottom = 16.dp)) {

            // Header con info del cliente
            item {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (!cliente.telefono.isNullOrBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Filled.Phone, contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                            Text(cliente.telefono, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    if (!cliente.notas.isNullOrBlank()) {
                        Text(cliente.notas, style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Tarjeta de deuda pendiente (si tiene)
            if ((cc?.saldoPendiente ?: 0.0) > 0) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically) {
                            Text("Deuda total pendiente", style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onErrorContainer)
                            Text(cc!!.saldoPendiente.toDollarString(),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            // Historial de compras
            item {
                Text("Historial de compras",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }

            if (ventas.isEmpty()) {
                item {
                    Text("Sin compras registradas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp))
                }
            } else {
                items(ventas, key = { it.venta.id }) { vd ->
                    VentaResumenItem(vd = vd, onClick = {
                        navController.navigate(Screen.DetalleVenta.createRoute(vd.venta.id))
                    })
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
private fun VentaResumenItem(vd: VentaConDetalle, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(vd.venta.fecha.toDateString(), style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${vd.items.size} producto(s)", style = MaterialTheme.typography.bodyMedium)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(vd.venta.total.toDollarString(), style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold)
            EstadoChip(estado = vd.venta.estadoPago)
        }
    }
}

@Composable
fun EstadoChip(estado: String, modifier: Modifier = Modifier) {
    val (containerColor, contentColor, label) = when (estado) {
        EstadoPago.PAGADO.name  -> Triple(MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer, "Pagado")
        EstadoPago.FIADO.name   -> Triple(MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer, "Fiado")
        else                    -> Triple(MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer, "Parcial")
    }
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = containerColor,
        modifier = modifier
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold,
            color = contentColor, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
    }
}
