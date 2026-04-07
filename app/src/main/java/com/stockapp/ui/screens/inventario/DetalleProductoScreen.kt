package com.stockapp.ui.screens.inventario

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.stockapp.data.local.entity.VarianteEntity
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

    // Obtenemos el producto de la lista en el ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pv = uiState.productos.find { it.producto.id == productoId }

    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pv?.producto?.nombre ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (pv != null) {
                        // Boton editar
                        IconButton(onClick = {
                            navController.navigate(Screen.EditarProducto.createRoute(productoId))
                        }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar")
                        }
                        // Boton eliminar
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Eliminar",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (pv != null) {
                FloatingActionButton(
                    onClick = {
                        compartirProducto(
                            context = context,
                            nombre = pv.producto.nombre,
                            precioNormal = pv.producto.precioNormal,
                            precioPorMayor = pv.producto.precioPorMayor,
                            fotoUri = pv.producto.fotoUri
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
        if (pv == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val producto = pv.producto

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            // Foto
            item {
                if (producto.fotoUri != null) {
                    AsyncImage(
                        model = producto.fotoUri,
                        contentDescription = producto.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Inventory2, contentDescription = null,
                            modifier = Modifier.size(72.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                    }
                }
            }

            // Info principal
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Nombre y categoria
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

                    // Descripcion
                    if (!producto.descripcion.isNullOrBlank()) {
                        Text(
                            text = producto.descripcion,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    HorizontalDivider()

                    // Precios
                    Text(
                        "Precios",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PrecioCard(
                            label = "Precio normal",
                            precio = producto.precioNormal,
                            modifier = Modifier.weight(1f),
                            isPrimary = true
                        )
                        PrecioCard(
                            label = "Por mayor",
                            precio = producto.precioPorMayor,
                            modifier = Modifier.weight(1f),
                            isPrimary = false
                        )
                    }

                    HorizontalDivider()

                    // Variantes y stock
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Variantes",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Stock total: ${pv.stockTotal}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Lista de variantes
            val variantesActivas = pv.variantes.filter { it.activo }
            if (variantesActivas.isEmpty()) {
                item {
                    Text(
                        "Sin variantes registradas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            } else {
                items(variantesActivas) { variante ->
                    VarianteItem(variante = variante)
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }

    // Dialogo de confirmacion de eliminacion
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = { Icon(Icons.Filled.Warning, contentDescription = null,
                tint = MaterialTheme.colorScheme.error) },
            title = { Text("Eliminar producto") },
            text = { Text("¿Estás seguro de que deseas eliminar \"${pv?.producto?.nombre}\"? Esta acción no se puede deshacer.") },
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
private fun PrecioCard(
    label: String,
    precio: Double,
    modifier: Modifier = Modifier,
    isPrimary: Boolean
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPrimary)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isPrimary) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = precio.toDollarString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (isPrimary) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun VarianteItem(variante: VarianteEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "${variante.atributo}: ${variante.valor}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
        StockChip(stock = variante.stock)
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
}
