package com.stockapp.ui.screens.proveedores

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.stockapp.domain.model.EstadoCompra

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaCompraScreen(
    navController: NavController,
    viewModel: NuevaCompraViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.guardado.collect { navController.popBackStack() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva compra") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.descripcion,
                onValueChange = { viewModel.descripcion = it; viewModel.errorDescripcion = false },
                label = { Text("Descripción *") },
                isError = viewModel.errorDescripcion,
                supportingText = if (viewModel.errorDescripcion) ({ Text("La descripción es requerida") }) else null,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                maxLines = 3
            )

            OutlinedTextField(
                value = viewModel.totalStr,
                onValueChange = { viewModel.totalStr = it; viewModel.errorTotal = false },
                label = { Text("Total *") },
                prefix = { Text("$") },
                isError = viewModel.errorTotal,
                supportingText = if (viewModel.errorTotal) ({ Text("Ingresa un total válido") }) else null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                )
            )

            // Estado de pago
            Text("Estado de pago", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(EstadoCompra.PENDIENTE, EstadoCompra.PARCIAL, EstadoCompra.PAGADO).forEach { estado ->
                    FilterChip(
                        selected = viewModel.estadoPago == estado,
                        onClick = { viewModel.estadoPago = estado },
                        label = {
                            Text(when (estado) {
                                EstadoCompra.PAGADO -> "Pagado"
                                EstadoCompra.PENDIENTE -> "Pendiente"
                                EstadoCompra.PARCIAL -> "Parcial"
                            })
                        }
                    )
                }
            }

            // Pago inicial solo si PARCIAL
            if (viewModel.estadoPago == EstadoCompra.PARCIAL) {
                OutlinedTextField(
                    value = viewModel.pagoInicialStr,
                    onValueChange = { viewModel.pagoInicialStr = it; viewModel.errorPagoInicial = false },
                    label = { Text("Pago inicial *") },
                    prefix = { Text("$") },
                    isError = viewModel.errorPagoInicial,
                    supportingText = if (viewModel.errorPagoInicial) ({ Text("Ingresa un monto válido") }) else null,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    )
                )
            }

            Button(
                onClick = { viewModel.guardar() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar compra")
            }
        }
    }
}
