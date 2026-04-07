package com.stockapp.ui.screens.ventas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.stockapp.data.local.entity.AbonoEntity
import com.stockapp.data.local.entity.VentaItemEntity
import com.stockapp.data.local.relation.VentaConDetalle
import com.stockapp.domain.model.EstadoPago
import com.stockapp.domain.model.TipoPrecio
import com.stockapp.ui.screens.clientes.EstadoChip
import com.stockapp.ui.util.toDollarString
import com.stockapp.ui.util.toDateString
import com.stockapp.ui.util.toDateTimeString
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleVentaScreen(
    navController: NavController,
    viewModel: DetalleVentaViewModel = hiltViewModel()
) {
    val ventaId = navController.currentBackStackEntry
        ?.arguments?.getLong("ventaId") ?: return

    val ventaFlow = remember(ventaId) { viewModel.getVenta(ventaId) }
    val vd by ventaFlow.collectAsStateWithLifecycle(initialValue = null)

    var showAbonoDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.abonoGuardado.collectLatest { showAbonoDialog = false }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Venta") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            val pendiente = vd?.venta?.estadoPago != EstadoPago.PAGADO.name
            if (pendiente && vd != null) {
                ExtendedFloatingActionButton(
                    onClick = { showAbonoDialog = true },
                    icon = { Icon(Icons.Filled.AddCard, contentDescription = null) },
                    text = { Text("Registrar abono") },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    ) { innerPadding ->
        if (vd == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val detalle = vd!!

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            // Header
            item {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            detalle.cliente?.nombre ?: "Sin cliente registrado",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        EstadoChip(estado = detalle.venta.estadoPago)
                    }
                    Text(detalle.venta.fecha.toDateTimeString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (!detalle.venta.notas.isNullOrBlank()) {
                        Text(detalle.venta.notas, style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Saldo pendiente (si aplica)
            if (detalle.venta.estadoPago != EstadoPago.PAGADO.name) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Row(Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("Saldo pendiente",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer)
                                Text("Total: ${detalle.venta.total.toDollarString()}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f))
                            }
                            Text(detalle.saldoPendiente.toDollarString(),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            // Productos
            item {
                Text("Productos",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }

            items(detalle.items, key = { it.id }) { item ->
                VentaItemRow(item = item)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            // Total
            item {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(detalle.venta.total.toDollarString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary)
                }
                HorizontalDivider()
            }

            // Abonos
            if (detalle.abonos.isNotEmpty()) {
                item {
                    Text("Abonos registrados",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                }
                items(detalle.abonos, key = { it.id }) { abono ->
                    AbonoRow(abono = abono)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total abonado", style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(detalle.totalAbonado.toDollarString(),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }
    }

    // Dialog registrar abono
    if (showAbonoDialog && vd != null) {
        AlertDialog(
            onDismissRequest = { showAbonoDialog = false; viewModel.montoAbono = ""; viewModel.notasAbono = "" },
            title = { Text("Registrar abono") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Saldo pendiente: ${vd!!.saldoPendiente.toDollarString()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)

                    OutlinedTextField(
                        value = viewModel.montoAbono,
                        onValueChange = { viewModel.montoAbono = it },
                        label = { Text("Monto abonado *") },
                        leadingIcon = { Text("$") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = viewModel.errorAbono != null
                    )

                    OutlinedTextField(
                        value = viewModel.notasAbono,
                        onValueChange = { viewModel.notasAbono = it },
                        label = { Text("Notas (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )

                    if (viewModel.errorAbono != null) {
                        Text(viewModel.errorAbono!!, color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.registrarAbono(ventaId, vd!!.venta.total) },
                    enabled = !viewModel.isSavingAbono
                ) {
                    if (viewModel.isSavingAbono) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp),
                            color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Registrar")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showAbonoDialog = false; viewModel.montoAbono = ""; viewModel.notasAbono = "" }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun VentaItemRow(item: VentaItemEntity) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                item.productoNombre.ifBlank { "Producto #${item.productoId}" },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            val detalle = buildString {
                append("${item.cantidad} × ${item.precioUnitario.toDollarString()}")
                append(" · ${TipoPrecio.entries.find { it.name == item.tipoPrecio }?.label ?: item.tipoPrecio}")
                if (!item.varianteLabel.isNullOrBlank()) append(" · ${item.varianteLabel}")
            }
            Text(detalle, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(item.subtotal.toDollarString(), style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun AbonoRow(abono: AbonoEntity) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text(abono.fecha.toDateString(), style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (!abono.notas.isNullOrBlank()) {
                Text(abono.notas, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Text(abono.monto.toDollarString(), style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.secondary)
    }
}
