package com.stockapp.ui.screens.reportes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.stockapp.ui.util.ExcelExporter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportesScreen(
    navController: NavController,
    viewModel: ReportesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Lanzar share sheet cuando se genera el archivo
    LaunchedEffect(uiState.uriExportado) {
        uiState.uriExportado?.let { uri ->
            val nombre = uiState.tipoSeleccionado.label
            ExcelExporter.compartirExcel(context, uri, nombre)
            viewModel.limpiarUri()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reportes y Export") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Tipo de reporte
            Text("Tipo de reporte",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ReporteOption(
                    icon = Icons.Filled.Inventory2,
                    title = TipoReporte.INVENTARIO.label,
                    subtitle = "Lista completa de productos, stock y precios",
                    selected = uiState.tipoSeleccionado == TipoReporte.INVENTARIO,
                    onClick = { viewModel.seleccionarTipo(TipoReporte.INVENTARIO) }
                )
                ReporteOption(
                    icon = Icons.Filled.ReceiptLong,
                    title = TipoReporte.VENTAS.label,
                    subtitle = "Historial de ventas en el período seleccionado",
                    selected = uiState.tipoSeleccionado == TipoReporte.VENTAS,
                    onClick = { viewModel.seleccionarTipo(TipoReporte.VENTAS) }
                )
                ReporteOption(
                    icon = Icons.Filled.Person,
                    title = TipoReporte.CXC.label,
                    subtitle = "Clientes con saldo pendiente (fiado/parcial)",
                    selected = uiState.tipoSeleccionado == TipoReporte.CXC,
                    onClick = { viewModel.seleccionarTipo(TipoReporte.CXC) }
                )
                ReporteOption(
                    icon = Icons.Filled.Store,
                    title = TipoReporte.CXP.label,
                    subtitle = "Proveedores con deuda pendiente",
                    selected = uiState.tipoSeleccionado == TipoReporte.CXP,
                    onClick = { viewModel.seleccionarTipo(TipoReporte.CXP) }
                )
            }

            // Período (solo si aplica)
            if (uiState.tipoSeleccionado == TipoReporte.VENTAS) {
                HorizontalDivider()
                Text("Período",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RangoPeriodo.entries.forEach { rango ->
                        FilterChip(
                            selected = uiState.rangoSeleccionado == rango,
                            onClick = { viewModel.seleccionarRango(rango) },
                            label = { Text(rango.label, style = MaterialTheme.typography.labelMedium) }
                        )
                    }
                }
            }

            HorizontalDivider()

            // Info del reporte seleccionado
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.TableChart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Column {
                        Text(
                            "Se exportará: ${uiState.tipoSeleccionado.label}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        if (uiState.tipoSeleccionado == TipoReporte.VENTAS) {
                            Text(
                                "Período: ${uiState.rangoSeleccionado.label}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }
                        Text(
                            "Formato: Excel (.xlsx)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Error
            if (uiState.error != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Error, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer)
                        Text(
                            "Error al exportar: ${uiState.error}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            // Botón exportar
            Button(
                onClick = { viewModel.exportar(context) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.exportando
            ) {
                if (uiState.exportando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Generando...")
                } else {
                    Icon(Icons.Filled.FileDownload, contentDescription = null,
                        modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Exportar a Excel")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReporteOption(
    icon: ImageVector,
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.surface
        ),
        border = if (selected) CardDefaults.outlinedCardBorder() else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                icon, contentDescription = null,
                tint = if (selected) MaterialTheme.colorScheme.onSecondaryContainer
                else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (selected) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (selected) MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (selected) {
                Icon(
                    Icons.Filled.CheckCircle, contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
