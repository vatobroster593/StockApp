package com.stockapp.ui.screens.proveedores

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockapp.data.local.entity.ProveedorEntity
import com.stockapp.domain.repository.ProveedorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgregarEditarProveedorViewModel @Inject constructor(
    private val repository: ProveedorRepository
) : ViewModel() {

    var nombre by mutableStateOf("")
    var telefono by mutableStateOf("")
    var notas by mutableStateOf("")
    var errorNombre by mutableStateOf(false)

    private val _guardado = Channel<Unit>(Channel.BUFFERED)
    val guardado = _guardado.receiveAsFlow()

    private var proveedorOriginal: ProveedorEntity? = null

    fun cargar(proveedorId: Long) {
        viewModelScope.launch {
            repository.getProveedorById(proveedorId)?.also { p ->
                proveedorOriginal = p
                nombre = p.nombre
                telefono = p.telefono ?: ""
                notas = p.notas ?: ""
            }
        }
    }

    fun guardar(proveedorId: Long?) {
        if (nombre.isBlank()) { errorNombre = true; return }
        errorNombre = false
        viewModelScope.launch {
            if (proveedorId == null) {
                repository.saveProveedor(
                    ProveedorEntity(
                        nombre = nombre.trim(),
                        telefono = telefono.trim().ifBlank { null },
                        notas = notas.trim().ifBlank { null }
                    )
                )
            } else {
                val original = proveedorOriginal ?: return@launch
                repository.updateProveedor(
                    original.copy(
                        nombre = nombre.trim(),
                        telefono = telefono.trim().ifBlank { null },
                        notas = notas.trim().ifBlank { null }
                    )
                )
            }
            _guardado.send(Unit)
        }
    }
}
