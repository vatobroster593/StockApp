package com.stockapp.ui.screens.inventario

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.stockapp.domain.model.Categoria
import kotlinx.coroutines.flow.collectLatest
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarEditarProductoScreen(
    navController: NavController,
    productoId: Long? = null,
    duplicarDeId: Long? = null,
    viewModel: AgregarEditarProductoViewModel = hiltViewModel()
) {
    val esEdicion = productoId != null
    val context = LocalContext.current

    LaunchedEffect(productoId, duplicarDeId) {
        when {
            productoId != null -> viewModel.loadProducto(productoId)
            duplicarDeId != null -> viewModel.loadParaDuplicar(duplicarDeId)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.productoGuardado.collectLatest {
            navController.popBackStack()
        }
    }

    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) cameraUri?.let { viewModel.fotoUri = it.toString() }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {}
            viewModel.fotoUri = it.toString()
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val imageDir = File(context.filesDir, "product_images").also { it.mkdirs() }
            val file = File(imageDir, "product_${System.currentTimeMillis()}.jpg")
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            cameraUri = uri
            cameraLauncher.launch(uri)
        }
    }

    var showFotoDialog by remember { mutableStateOf(false) }
    var categoriaExpanded by remember { mutableStateOf(false) }

    val titulo = when {
        esEdicion -> "Editar Producto"
        duplicarDeId != null -> "Duplicar Producto"
        else -> "Agregar Producto"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titulo) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.guardar(productoId) },
                        enabled = !viewModel.isLoading
                    ) {
                        Text("Guardar", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                FotoSection(fotoUri = viewModel.fotoUri, onClick = { showFotoDialog = true })
            }

            if (viewModel.errorMessage != null) {
                item {
                    Card(colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer)) {
                        Row(Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Error, contentDescription = null,
                                tint = MaterialTheme.colorScheme.onErrorContainer)
                            Text(viewModel.errorMessage!!,
                                color = MaterialTheme.colorScheme.onErrorContainer)
                        }
                    }
                }
            }

            item {
                SectionTitle("Información del producto")
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = viewModel.nombre,
                    onValueChange = { viewModel.nombre = it },
                    label = { Text("Nombre *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = viewModel.descripcion,
                    onValueChange = { viewModel.descripcion = it },
                    label = { Text("Descripción (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }

            item {
                ExposedDropdownMenuBox(
                    expanded = categoriaExpanded,
                    onExpandedChange = { categoriaExpanded = it }
                ) {
                    OutlinedTextField(
                        value = Categoria.entries.find { it.name == viewModel.categoria }?.label
                            ?: viewModel.categoria,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoriaExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = categoriaExpanded,
                        onDismissRequest = { categoriaExpanded = false }
                    ) {
                        Categoria.entries.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.label) },
                                onClick = {
                                    viewModel.categoria = cat.name
                                    categoriaExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                SectionTitle("Precios")
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = viewModel.precioNormal,
                        onValueChange = { viewModel.precioNormal = it },
                        label = { Text("Precio normal *") },
                        leadingIcon = { Text("$", style = MaterialTheme.typography.bodyLarge) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = viewModel.precioPorMayor,
                        onValueChange = { viewModel.precioPorMayor = it },
                        label = { Text("Por mayor *") },
                        leadingIcon = { Text("$", style = MaterialTheme.typography.bodyLarge) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }

            item {
                SectionTitle("Stock")
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = viewModel.stock,
                    onValueChange = { viewModel.stock = it },
                    label = { Text("Cantidad en inventario *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = { Text("Unidades disponibles actualmente") }
                )
            }

            item {
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.guardar(productoId) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    enabled = !viewModel.isLoading
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(Icons.Filled.Save, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (esEdicion) "Guardar cambios" else "Guardar producto",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }

    if (showFotoDialog) {
        AlertDialog(
            onDismissRequest = { showFotoDialog = false },
            title = { Text("Seleccionar foto") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = {
                            showFotoDialog = false
                            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.CameraAlt, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Tomar foto")
                    }
                    OutlinedButton(
                        onClick = {
                            showFotoDialog = false
                            galleryLauncher.launch(PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            ))
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.PhotoLibrary, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Elegir de galería")
                    }
                    if (viewModel.fotoUri != null) {
                        OutlinedButton(
                            onClick = { showFotoDialog = false; viewModel.fotoUri = null },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(Icons.Filled.DeleteOutline, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Quitar foto")
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showFotoDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun FotoSection(fotoUri: String?, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (fotoUri != null) {
            AsyncImage(
                model = fotoUri,
                contentDescription = "Foto del producto",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp).size(40.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Edit, contentDescription = "Cambiar foto",
                    tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Filled.AddPhotoAlternate,
                    contentDescription = "Agregar foto",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
                Spacer(Modifier.height(8.dp))
                Text("Toca para agregar foto",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary
    )
}
