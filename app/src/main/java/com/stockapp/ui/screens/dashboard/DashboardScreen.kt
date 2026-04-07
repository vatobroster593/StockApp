package com.stockapp.ui.screens.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.stockapp.data.local.relation.ProductoConVariantes
import com.stockapp.ui.navigation.Screen
import com.stockapp.ui.util.toDollarString
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val hoy = SimpleDateFormat("EEEE d 'de' MMMM", Locale("es")).format(Date())
        .replaceFirstChar { it.uppercaseChar() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("StockApp", style = MaterialTheme.typography.titleLarge)
                        Text(hoy,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        AnimatedVisibility(
            visible = !uiState.isLoading,
            enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 10 }
        ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ventas de hoy
            item {
                Text("Hoy",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.AttachMoney,
                        label = "Ventas hoy",
                        value = uiState.ventasHoy.toDollarString(),
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.ShoppingBag,
                        label = "Pedidos hoy",
                        value = uiState.countVentasHoy.toString(),
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            // Ventas del mes
            item {
                Text("Este mes",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.TrendingUp,
                        label = "Ventas del mes",
                        value = uiState.ventasMes.toDollarString(),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Receipt,
                        label = "Pedidos del mes",
                        value = uiState.countVentasMes.toString(),
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // CxC y CxP
            item {
                Text("Cuentas pendientes",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DebtCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Person,
                        label = "Por cobrar",
                        sublabel = "Clientes",
                        value = uiState.totalCxC,
                        onClick = { navController.navigate(Screen.Clientes.route) }
                    )
                    DebtCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Store,
                        label = "Por pagar",
                        sublabel = "Proveedores",
                        value = uiState.totalCxP,
                        onClick = { navController.navigate(Screen.Proveedores.route) }
                    )
                }
            }

            // Stock bajo
            if (uiState.productosStockBajo.isNotEmpty()) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Filled.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                        Text("Stock bajo",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.error)
                    }
                }
                items(uiState.productosStockBajo, key = { it.producto.id }) { item ->
                    StockBajoRow(
                        item = item,
                        onClick = {
                            navController.navigate(Screen.DetalleProducto.createRoute(item.producto.id))
                        }
                    )
                }
            }

            // Accesos rápidos
            item {
                Text("Accesos rápidos",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuickActionChip(
                        icon = Icons.Filled.Add,
                        label = "Nueva venta",
                        onClick = { navController.navigate(Screen.NuevaVenta.route) }
                    )
                    QuickActionChip(
                        icon = Icons.Filled.Inventory2,
                        label = "Inventario",
                        onClick = { navController.navigate(Screen.Inventario.route) }
                    )
                    QuickActionChip(
                        icon = Icons.Filled.Assessment,
                        label = "Reportes",
                        onClick = { navController.navigate(Screen.Reportes.route) }
                    )
                }
            }

            item { Spacer(Modifier.height(8.dp)) }
        }
        } // AnimatedVisibility
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    containerColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(24.dp))
            Text(value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = contentColor)
            Text(label,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor.copy(alpha = 0.8f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DebtCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    sublabel: String,
    value: Double,
    onClick: () -> Unit
) {
    val hasDebt = value > 0
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (hasDebt) MaterialTheme.colorScheme.errorContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    icon, contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = if (hasDebt) MaterialTheme.colorScheme.onErrorContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(sublabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (hasDebt) MaterialTheme.colorScheme.onErrorContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(value.toDollarString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (hasDebt) MaterialTheme.colorScheme.onErrorContainer
                else MaterialTheme.colorScheme.onSurfaceVariant)
            Text(label,
                style = MaterialTheme.typography.labelSmall,
                color = if (hasDebt) MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StockBajoRow(item: ProductoConVariantes, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Filled.Inventory2, contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Text(
                item.producto.nombre,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            val stock = item.stockTotal
            Surface(
                color = if (stock == 0) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.errorContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = if (stock == 0) "Sin stock" else "$stock uds.",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (stock == 0) MaterialTheme.colorScheme.onError
                    else MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuickActionChip(icon: ImageVector, label: String, onClick: () -> Unit) {
    SuggestionChip(
        onClick = onClick,
        label = { Text(label, style = MaterialTheme.typography.labelMedium) },
        icon = { Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp)) }
    )
}
