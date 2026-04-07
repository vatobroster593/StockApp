package com.stockapp.ui.screens.proveedores

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.stockapp.data.local.relation.CompraConPagos
import com.stockapp.domain.model.EstadoCompra
import com.stockapp.ui.navigation.Screen
import com.stockapp.ui.util.toDateString
import com.stockapp.ui.util.toDollarString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProveedorScreen(
    navController: NavController,
    viewModel: DetalleProveedorViewModel = hiltViewModel()
) {
    val proveedorId = navController.currentBackStackEntry?.arguments?.getLong("proveedorId") ?: return
    val uiState by viewModel.uiState.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var pagoDialog: CompraConPagos? by remember { mutableStateOf(null) }

    LaunchedEffect(proveedorId) { viewModel.cargar(proveedorId) }

    LaunchedEffect(Unit) {
        viewModel.pagoGuardado.collect { pagoDialog = null }
    }
    LaunchedEffect(Unit) {
        viewModel.eliminado.collect { navController.popBackStack() }
    }

    val totalDeuda = uiState.compras.filter {
        it.compra.estadoPago in listOf(EstadoCompra.PENDIENTE.name, EstadoCompra.PARCIAL.name)
    }.sumOf { it.saldoPendiente }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.proveedor?.nombre ?: "Proveedor") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.EditarProveedor.createRoute(proveedorId))
                    }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.NuevaCompra.createRoute(proveedorId))
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Nueva compra")
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Info proveedor
            item {
                uiState.proveedor?.let { proveedor ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(proveedor.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            if (!proveedor.telefono.isNullOrBlank()) {
                                Text(proveedor.telefono, style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            if (!proveedor.notas.isNullOrBlank()) {
                                Text(proveedor.notas, style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // Deuda total
            if (totalDeuda > 0) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Deuda pendiente total",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onErrorContainer)
                            Text(totalDeuda.toDollarString(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer)
                        }
                    }
                }
            }

            // Header compras
            item {
                Text("Compras",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp))
            }

            if (uiState.compras.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("Sin compras registradas",
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                items(uiState.compras, key = { it.compra.id }) { compraConPagos ->
                    CompraCard(
                        compraConPagos = compraConPagos,
                        onRegistrarPago = { pagoDialog = compraConPagos }
                    )
                }
            }
        }
    }

    // Dialogo eliminar proveedor
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar proveedor") },
            text = { Text("Se eliminarán el proveedor y todas sus compras. Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = { showDeleteDialog = false; viewModel.eliminarProveedor() },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
            }
        )
    }

    // Dialogo registrar pago
    pagoDialog?.let { compra ->
        RegistrarPagoDialog(
            compraConPagos = compra,
            onDismiss = { pagoDialog = null },
            onConfirm = { monto, notas ->
                viewModel.registrarPago(compra.compra.id, compra.compra.total, monto, notas)
            }
        )
    }
}

@Composable
private fun CompraCard(
    compraConPagos: CompraConPagos,
    onRegistrarPago: () -> Unit
) {
    val compra = compraConPagos.compra
    val estadoColor = when (compra.estadoPago) {
        EstadoCompra.PAGADO.name   -> MaterialTheme.colorScheme.tertiaryContainer
        EstadoCompra.PENDIENTE.name -> MaterialTheme.colorScheme.errorContainer
        else                        -> MaterialTheme.colorScheme.secondaryContainer
    }
    val estadoTextColor = when (compra.estadoPago) {
        EstadoCompra.PAGADO.name   -> MaterialTheme.colorScheme.onTertiaryContainer
        EstadoCompra.PENDIENTE.name -> MaterialTheme.colorScheme.onErrorContainer
        else                        -> MaterialTheme.colorScheme.onSecondaryContainer
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(compra.descripcion,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f))
                Surface(color = estadoColor, shape = MaterialTheme.shapes.small) {
                    Text(
                        text = when (compra.estadoPago) {
                            EstadoCompra.PAGADO.name -> "Pagado"
                            EstadoCompra.PENDIENTE.name -> "Pendiente"
                            else -> "Parcial"
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = estadoTextColor
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(compra.fecha.toDateString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Total: ${compra.total.toDollarString()}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium)
            }

            if (compra.estadoPago != EstadoCompra.PAGADO.name && compraConPagos.saldoPendiente > 0) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pagado: ${compraConPagos.totalPagado.toDollarString()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Saldo: ${compraConPagos.saldoPendiente.toDollarString()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold)
                }
            }

            // Pagos registrados
            if (compraConPagos.pagos.isNotEmpty()) {
                HorizontalDivider()
                Text("Pagos:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                compraConPagos.pagos.forEach { pago ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(pago.fecha.toDateString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(pago.monto.toDollarString(),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium)
                    }
                    if (!pago.notas.isNullOrBlank()) {
                        Text(pago.notas,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            if (compra.estadoPago != EstadoCompra.PAGADO.name) {
                OutlinedButton(
                    onClick = onRegistrarPago,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar pago")
                }
            }
        }
    }
}

@Composable
private fun RegistrarPagoDialog(
    compraConPagos: CompraConPagos,
    onDismiss: () -> Unit,
    onConfirm: (monto: Double, notas: String?) -> Unit
) {
    var montoStr by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }
    var errorMonto by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar pago") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Saldo pendiente: ${compraConPagos.saldoPendiente.toDollarString()}",
                    style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(
                    value = montoStr,
                    onValueChange = { montoStr = it; errorMonto = false },
                    label = { Text("Monto") },
                    prefix = { Text("$") },
                    isError = errorMonto,
                    supportingText = if (errorMonto) ({ Text("Ingresa un monto válido") }) else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notas,
                    onValueChange = { notas = it },
                    label = { Text("Notas (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val monto = montoStr.toDoubleOrNull()
                if (monto == null || monto <= 0) { errorMonto = true; return@TextButton }
                onConfirm(monto, notas.trim().ifBlank { null })
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
