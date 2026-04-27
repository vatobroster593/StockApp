package com.stockapp.ui.screens.inventario

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.stockapp.data.local.entity.ProductoEntity
import com.stockapp.domain.model.Categoria
import com.stockapp.ui.navigation.Screen
import com.stockapp.ui.util.compartirProducto
import com.stockapp.ui.util.toDollarString

private enum class VistaInventario { CUADRICULA, LISTA }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventarioScreen(
    navController: NavController,
    viewModel: InventarioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var vista by remember { mutableStateOf(VistaInventario.CUADRICULA) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventario") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                actions = {
                    IconButton(onClick = {
                        vista = if (vista == VistaInventario.CUADRICULA)
                            VistaInventario.LISTA else VistaInventario.CUADRICULA
                    }) {
                        Icon(
                            imageVector = if (vista == VistaInventario.CUADRICULA)
                                Icons.Filled.ViewList else Icons.Filled.GridView,
                            contentDescription = "Cambiar vista"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Screen.AgregarProducto.route) },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Agregar") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::setSearchQuery,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
            )
            CategoriaChips(
                categoriaSeleccionada = uiState.categoriaFiltro,
                onCategoriaSelected = viewModel::setCategoriaFiltro
            )

            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                AnimatedVisibility(visible = uiState.productos.isEmpty(), enter = fadeIn(), exit = fadeOut()) {
                    EmptyInventario(
                        tienesFiltro = uiState.searchQuery.isNotBlank() || uiState.categoriaFiltro != null,
                        onAgregar = { navController.navigate(Screen.AgregarProducto.route) }
                    )
                }
                AnimatedVisibility(
                    visible = uiState.productos.isNotEmpty(),
                    enter = fadeIn() + slideInVertically { it / 8 },
                    exit = fadeOut()
                ) {
                    val context = LocalContext.current
                    val onShare: (ProductoEntity) -> Unit = { producto ->
                        compartirProducto(
                            context = context,
                            nombre = producto.nombre,
                            descripcion = producto.descripcion,
                            precioNormal = producto.precioNormal,
                            fotoUri = producto.fotoUri
                        )
                    }
                    val onProductoClick: (Long) -> Unit = {
                        navController.navigate(Screen.DetalleProducto.createRoute(it))
                    }

                    if (vista == VistaInventario.CUADRICULA) {
                        ProductosGrid(
                            productos = uiState.productos,
                            onProductoClick = onProductoClick,
                            onShare = onShare
                        )
                    } else {
                        ProductosLista(
                            productos = uiState.productos,
                            onProductoClick = onProductoClick,
                            onShare = onShare
                        )
                    }
                }
            }
        }
    }
}

// ─── Vista cuadrícula ────────────────────────────────────────────────────────

@Composable
private fun ProductosGrid(
    productos: List<ProductoEntity>,
    onProductoClick: (Long) -> Unit,
    onShare: (ProductoEntity) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(productos, key = { it.id }) { producto ->
            ProductoCard(
                producto = producto,
                onClick = { onProductoClick(producto.id) },
                onShare = { onShare(producto) }
            )
        }
    }
}

// ─── Vista lista ─────────────────────────────────────────────────────────────

@Composable
private fun ProductosLista(
    productos: List<ProductoEntity>,
    onProductoClick: (Long) -> Unit,
    onShare: (ProductoEntity) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(bottom = 88.dp)) {
        items(productos, key = { it.id }) { producto ->
            ProductoListItem(
                producto = producto,
                onClick = { onProductoClick(producto.id) },
                onShare = { onShare(producto) }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Composable
private fun ProductoListItem(
    producto: ProductoEntity,
    onClick: () -> Unit,
    onShare: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Foto pequeña
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            if (producto.fotoUri != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(producto.fotoUri).crossfade(true).build(),
                    contentDescription = producto.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    Icons.Filled.Inventory2, contentDescription = null,
                    modifier = Modifier.align(Alignment.Center).size(28.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )
            }
        }

        // Info
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            val variacion = listOfNotNull(producto.color, producto.talla).joinToString(" · ")
            Text(
                text = variacion.ifBlank {
                    Categoria.entries.find { it.name == producto.categoria }?.label ?: producto.categoria
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = producto.precioNormal.toDollarString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        // Stock chip
        StockChip(stock = producto.stock)

        // Compartir
        IconButton(onClick = onShare, modifier = Modifier.size(36.dp)) {
            Icon(
                Icons.Filled.Share, contentDescription = "Compartir",
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ─── Tile (cuadrícula) ───────────────────────────────────────────────────────

@Composable
fun ProductoCard(
    producto: ProductoEntity,
    onClick: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
                if (producto.fotoUri != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(producto.fotoUri).crossfade(true).build(),
                        contentDescription = producto.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Inventory2, contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
                    }
                }
                StockChip(stock = producto.stock, modifier = Modifier.align(Alignment.TopEnd).padding(6.dp))
                Box(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp).size(36.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                        .clickable(onClick = onShare),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Share, contentDescription = "Compartir",
                        modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                }
            }
            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(producto.nombre, style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(producto.precioNormal.toDollarString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                val variacion = listOfNotNull(producto.color, producto.talla).joinToString(" · ")
                Text(
                    text = variacion.ifBlank {
                        Categoria.entries.find { it.name == producto.categoria }?.label ?: producto.categoria
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ─── Componentes compartidos ─────────────────────────────────────────────────

@Composable
fun StockChip(stock: Int, modifier: Modifier = Modifier) {
    val (bgColor, textColor, label) = when {
        stock == 0 -> Triple(Color(0xFFB3261E), Color.White, "Sin stock")
        stock <= 3 -> Triple(Color(0xFFF59E0B), Color.White, "Stock: $stock")
        else       -> Triple(Color(0xFF2E7D32), Color.White, "Stock: $stock")
    }
    Box(
        modifier = modifier.clip(RoundedCornerShape(6.dp)).background(bgColor)
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall,
            color = textColor, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = query, onValueChange = onQueryChange, modifier = modifier,
        placeholder = { Text("Buscar productos...") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Filled.Close, contentDescription = "Limpiar")
                }
            }
        },
        shape = RoundedCornerShape(28.dp), singleLine = true
    )
}

@Composable
private fun CategoriaChips(categoriaSeleccionada: String?, onCategoriaSelected: (String?) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(selected = categoriaSeleccionada == null,
                onClick = { onCategoriaSelected(null) }, label = { Text("Todos") })
        }
        items(Categoria.entries) { cat ->
            FilterChip(selected = categoriaSeleccionada == cat.name,
                onClick = { onCategoriaSelected(cat.name) }, label = { Text(cat.label) })
        }
    }
}

@Composable
private fun EmptyInventario(tienesFiltro: Boolean, onAgregar: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Icon(Icons.Filled.Inventory2, contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
        Spacer(Modifier.height(16.dp))
        Text(
            text = if (tienesFiltro) "Sin resultados" else "Aún no tienes productos",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (!tienesFiltro) {
            Spacer(Modifier.height(12.dp))
            Button(onClick = onAgregar) {
                Icon(Icons.Filled.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Agregar primer producto")
            }
        }
    }
}
