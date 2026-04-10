package com.stockapp.ui.screens.inventario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.stockapp.domain.model.Categoria
import com.stockapp.ui.navigation.Screen
import com.stockapp.ui.util.compartirProducto
import com.stockapp.ui.util.toDollarString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    navController: NavController,
    viewModel: InventarioViewModel = hiltViewModel()
) {
    val productoId = navController.currentBackStackEntry
        ?.arguments?.getLong("productoId") ?: return

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val producto = uiState.productos.find { it.id == productoId }

    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(producto?.nombre ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (producto != null) {
                        IconButton(onClick = {
                            navController.navigate(Screen.DuplicarProducto.createRoute(productoId))
                        }) {
                            Icon(Icons.Filled.ContentCopy, contentDescription = "Duplicar")
                        }
                        IconButton(onClick = {
                            navController.navigate(Screen.EditarProducto.createRoute(productoId))
                        }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar",
                                tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (producto != null) {
                FloatingActionButton(
                    onClick = {
                        compartirProducto(
                            context = context,
                            nombre = producto.nombre,
                            precioNormal = producto.precioNormal,
                            precioPorMayor = producto.precioPorMayor,
                            fotoUri = producto.fotoUri
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Filled.Share, contentDescription = "Compartir",
                        tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    ) { innerPadding ->
        if (producto == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            // Foto
            item {
                if (producto.fotoUri != null) {
                    AsyncImage(
                        model = producto.fotoUri,
                        contentDescription = producto.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(280.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Inventory2, contentDescription = null,
                            modifier = Modifier.size(72.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = producto.nombre,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(Categoria.entries.find { it.name == producto.categoria }?.label
                                    ?: producto.categoria)
                            }
                        )
                    }

                    if (!producto.descripcion.isNullOrBlank()) {
                        Text(
                            text = producto.descripcion,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Color y Talla
                    val tieneVariacion = !producto.color.isNullOrBlank() || !producto.talla.isNullOrBlank()
                    if (tieneVariacion) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (!producto.color.isNullOrBlank()) {
                                AssistChip(onClick = {}, label = { Text("Color: ${producto.color}") })
                            }
                            if (!producto.talla.isNullOrBlank()) {
                                AssistChip(onClick = {}, label = { Text("Talla: ${producto.talla}") })
                            }
                        }
                    }

                    HorizontalDivider()

                    Text("Precios", style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PrecioCard("Precio normal", producto.precioNormal,
                            Modifier.weight(1f), isPrimary = true)
                        PrecioCard("Por mayor", producto.precioPorMayor,
                            Modifier.weight(1f), isPrimary = false)
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Stock", style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                        StockChip(stock = producto.stock)
                    }

                    HorizontalDivider()

                    // Boton duplicar (acceso rapido desde el detalle)
                    OutlinedButton(
                        onClick = {
                            navController.navigate(Screen.DuplicarProducto.createRoute(productoId))
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.ContentCopy, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Duplicar producto")
                    }
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = { Icon(Icons.Filled.Warning, contentDescription = null,
                tint = MaterialTheme.colorScheme.error) },
            title = { Text("Eliminar producto") },
            text = { Text("¿Estás seguro de que deseas eliminar \"${producto?.nombre}\"? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteProducto(productoId)
                        showDeleteDialog = false
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) { Text("Eliminar") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun PrecioCard(label: String, precio: Double, modifier: Modifier, isPrimary: Boolean) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPrimary) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, style = MaterialTheme.typography.labelSmall,
                color = if (isPrimary) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(4.dp))
            Text(precio.toDollarString(), style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (isPrimary) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
