package com.stockapp.ui.screens.ventas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.stockapp.data.local.entity.ClienteEntity
import com.stockapp.data.local.entity.ProductoEntity
import com.stockapp.domain.model.EstadoPago
import com.stockapp.domain.model.TipoPrecio
import com.stockapp.ui.navigation.Screen
import com.stockapp.ui.util.toDollarString
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaVentaScreen(
    navController: NavController,
    viewModel: NuevaVentaViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.ventaGuardada.collectLatest { ventaId ->
            viewModel.reset()
            navController.navigate(Screen.DetalleVenta.createRoute(ventaId)) {
                popUpTo(Screen.NuevaVenta.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Nueva Venta") },
                    actions = {
                        if (viewModel.paso > 1) {
                            TextButton(onClick = { viewModel.paso-- }) { Text("Atrás") }
                        }
                    }
                )
                StepIndicator(pasoActual = viewModel.paso)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (viewModel.paso) {
                1 -> Paso1Cliente(viewModel)
                2 -> Paso2Productos(viewModel)
                3 -> Paso3Pago(viewModel)
            }
        }
    }
}

@Composable
private fun StepIndicator(pasoActual: Int) {
    val pasos = listOf("Cliente", "Productos", "Pago")
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        pasos.forEachIndexed { index, label ->
            val paso = index + 1
            val activo = paso == pasoActual
            val completado = paso < pasoActual
            val color = when {
                activo     -> MaterialTheme.colorScheme.primary
                completado -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                else       -> MaterialTheme.colorScheme.outlineVariant
            }
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.fillMaxWidth().height(4.dp)
                    .clip(RoundedCornerShape(2.dp)).background(color))
                Spacer(Modifier.height(4.dp))
                Text(label, style = MaterialTheme.typography.labelSmall,
                    color = if (activo) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

// ----- PASO 1: CLIENTE -----
@Composable
private fun Paso1Cliente(viewModel: NuevaVentaViewModel) {
    val clientes by viewModel.clientes.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = viewModel.busquedaCliente,
            onValueChange = { viewModel.busquedaCliente = it },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            placeholder = { Text("Buscar cliente...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            trailingIcon = {
                if (viewModel.busquedaCliente.isNotEmpty()) {
                    IconButton(onClick = { viewModel.busquedaCliente = "" }) {
                        Icon(Icons.Filled.Close, contentDescription = "Limpiar")
                    }
                }
            },
            shape = RoundedCornerShape(28.dp),
            singleLine = true
        )

        LazyColumn {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .clickable { viewModel.seleccionarCliente(null) }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Filled.PersonOff, contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Venta sin cliente registrado",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
            items(clientes, key = { it.id }) { cliente ->
                ClienteSelectItem(cliente = cliente, onClick = { viewModel.seleccionarCliente(cliente) })
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
private fun ClienteSelectItem(cliente: ClienteEntity, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(40.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Text(cliente.nombre.first().uppercase(), fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(cliente.nombre, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            if (!cliente.telefono.isNullOrBlank()) {
                Text(cliente.telefono, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// ----- PASO 2: PRODUCTOS -----
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Paso2Productos(viewModel: NuevaVentaViewModel) {
    val productos by viewModel.productos.collectAsStateWithLifecycle()
    var productoSeleccionado by remember { mutableStateOf<ProductoEntity?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        if (viewModel.items.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text("${viewModel.items.size} producto(s) en carrito",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text(viewModel.subtotal.toDollarString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        OutlinedTextField(
            value = viewModel.busquedaProducto,
            onValueChange = { viewModel.busquedaProducto = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            placeholder = { Text("Buscar producto...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            trailingIcon = {
                if (viewModel.busquedaProducto.isNotEmpty()) {
                    IconButton(onClick = { viewModel.busquedaProducto = "" }) {
                        Icon(Icons.Filled.Close, contentDescription = null)
                    }
                }
            },
            shape = RoundedCornerShape(28.dp),
            singleLine = true
        )

        LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(bottom = 80.dp)) {
            items(productos, key = { it.id }) { producto ->
                ProductoSelectItem(
                    producto = producto,
                    onClick = { if (producto.stock > 0) productoSeleccionado = producto }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }

        Button(
            onClick = { viewModel.paso = 3 },
            enabled = viewModel.items.isNotEmpty(),
            modifier = Modifier.fillMaxWidth().padding(16.dp).height(52.dp)
        ) {
            Text("Continuar al pago", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Filled.ArrowForward, contentDescription = null)
        }
    }

    productoSeleccionado?.let { producto ->
        AgregarItemDialog(
            producto = producto,
            onDismiss = { productoSeleccionado = null },
            onConfirm = { qty, tipo, precioNeg ->
                viewModel.agregarItem(producto, qty, tipo, precioNeg)
                productoSeleccionado = null
            }
        )
    }
}

@Composable
private fun ProductoSelectItem(producto: ProductoEntity, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.size(52.dp).clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)) {
            if (producto.fotoUri != null) {
                AsyncImage(model = producto.fotoUri, contentDescription = null,
                    contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Inventory2, contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
                }
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(producto.nombre, style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            val variacion = listOfNotNull(producto.color, producto.talla).joinToString(" · ")
            if (variacion.isNotBlank()) {
                Text(variacion, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(producto.precioNormal.toDollarString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary)
        }
        Text("Stock: ${producto.stock}",
            style = MaterialTheme.typography.labelSmall,
            color = if (producto.stock == 0) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AgregarItemDialog(
    producto: ProductoEntity,
    onDismiss: () -> Unit,
    onConfirm: (cantidad: Int, tipoPrecio: TipoPrecio, precioNegociado: Double?) -> Unit
) {
    var cantidad by remember { mutableStateOf(1) }
    var tipoPrecio by remember { mutableStateOf(TipoPrecio.NORMAL) }
    var precioNegociado by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(producto.nombre, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Cantidad
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Cantidad:", style = MaterialTheme.typography.bodyMedium)
                        Text("Disponible: ${producto.stock}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    IconButton(onClick = { if (cantidad > 1) cantidad-- },
                        modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Filled.Remove, contentDescription = "Menos")
                    }
                    Text("$cantidad", style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold)
                    IconButton(
                        onClick = { if (cantidad < producto.stock) cantidad++ },
                        modifier = Modifier.size(36.dp),
                        enabled = cantidad < producto.stock
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Más")
                    }
                }

                HorizontalDivider()

                Text("Precio:", style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                TipoPrecio.entries.forEach { tipo ->
                    val label = when (tipo) {
                        TipoPrecio.NORMAL    -> "Normal: ${producto.precioNormal.toDollarString()}"
                        TipoPrecio.MAYOR     -> "Por mayor: ${producto.precioPorMayor.toDollarString()}"
                        TipoPrecio.NEGOCIADO -> "Negociado (regateado)"
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { tipoPrecio = tipo }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = tipoPrecio == tipo, onClick = { tipoPrecio = tipo })
                        Spacer(Modifier.width(8.dp))
                        Text(label, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                if (tipoPrecio == TipoPrecio.NEGOCIADO) {
                    OutlinedTextField(
                        value = precioNegociado,
                        onValueChange = { precioNegociado = it },
                        label = { Text("Precio acordado *") },
                        leadingIcon = { Text("$") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val negPrecio = if (tipoPrecio == TipoPrecio.NEGOCIADO)
                        precioNegociado.toDoubleOrNull() else null
                    onConfirm(cantidad, tipoPrecio, negPrecio)
                },
                enabled = cantidad <= producto.stock &&
                    (tipoPrecio != TipoPrecio.NEGOCIADO || precioNegociado.toDoubleOrNull() != null)
            ) { Text("Agregar al carrito") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

// ----- PASO 3: PAGO -----
@Composable
private fun Paso3Pago(viewModel: NuevaVentaViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Filled.Person, contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(viewModel.clienteSeleccionado?.nombre ?: "Sin cliente registrado",
                    style = MaterialTheme.typography.bodyLarge)
            }
        }

        item {
            Text("Resumen", style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
        }

        items(viewModel.items.toList()) { item ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.productoNombre, style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${item.cantidad} × ${item.precioUnitario.toDollarString()} (${item.tipoPrecio.label})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(item.subtotal.toDollarString(), style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold)
            }
            HorizontalDivider()
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(viewModel.total.toDollarString(), style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
        }

        item {
            Text("Forma de pago", style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            EstadoPago.entries.forEach { ep ->
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { viewModel.estadoPago = ep }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = viewModel.estadoPago == ep,
                        onClick = { viewModel.estadoPago = ep })
                    Spacer(Modifier.width(8.dp))
                    Text(ep.label, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        if (viewModel.estadoPago == EstadoPago.PARCIAL) {
            item {
                OutlinedTextField(
                    value = viewModel.abonoInicial,
                    onValueChange = { viewModel.abonoInicial = it },
                    label = { Text("Monto abonado ahora *") },
                    leadingIcon = { Text("$") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = { Text("El resto queda como deuda pendiente") }
                )
            }
        }

        item {
            OutlinedTextField(
                value = viewModel.notas,
                onValueChange = { viewModel.notas = it },
                label = { Text("Notas (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
        }

        if (viewModel.errorPago != null) {
            item {
                Card(colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Error, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer)
                        Text(viewModel.errorPago!!, color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                }
            }
        }

        item {
            Button(
                onClick = viewModel::guardarVenta,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !viewModel.isSaving
            ) {
                if (viewModel.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Confirmar venta", style = MaterialTheme.typography.titleSmall)
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}
