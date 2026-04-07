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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarEditarProveedorScreen(
    navController: NavController,
    proveedorId: Long?,
    viewModel: AgregarEditarProveedorViewModel = hiltViewModel()
) {
    val isEdit = proveedorId != null

    LaunchedEffect(proveedorId) {
        if (proveedorId != null) viewModel.cargar(proveedorId)
    }

    LaunchedEffect(Unit) {
        viewModel.guardado.collect { navController.popBackStack() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) "Editar proveedor" else "Nuevo proveedor") },
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
                value = viewModel.nombre,
                onValueChange = { viewModel.nombre = it; viewModel.errorNombre = false },
                label = { Text("Nombre *") },
                isError = viewModel.errorNombre,
                supportingText = if (viewModel.errorNombre) ({ Text("El nombre es requerido") }) else null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = viewModel.telefono,
                onValueChange = { viewModel.telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = viewModel.notas,
                onValueChange = { viewModel.notas = it },
                label = { Text("Notas") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                )
            )

            Button(
                onClick = { viewModel.guardar(proveedorId) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEdit) "Guardar cambios" else "Agregar proveedor")
            }
        }
    }
}
