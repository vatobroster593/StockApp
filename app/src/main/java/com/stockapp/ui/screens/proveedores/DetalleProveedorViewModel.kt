package com.stockapp.ui.screens.proveedores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockapp.data.local.entity.ProveedorEntity
import com.stockapp.data.local.relation.CompraConPagos
import com.stockapp.domain.repository.ProveedorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetalleProveedorUiState(
    val proveedor: ProveedorEntity? = null,
    val compras: List<CompraConPagos> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class DetalleProveedorViewModel @Inject constructor(
    private val repository: ProveedorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleProveedorUiState())
    val uiState: StateFlow<DetalleProveedorUiState> = _uiState.asStateFlow()

    private val _pagoGuardado = Channel<Unit>(Channel.BUFFERED)
    val pagoGuardado = _pagoGuardado.receiveAsFlow()

    private val _eliminado = Channel<Unit>(Channel.BUFFERED)
    val eliminado = _eliminado.receiveAsFlow()

    fun cargar(proveedorId: Long) {
        viewModelScope.launch {
            val proveedor = repository.getProveedorById(proveedorId)
            _uiState.value = _uiState.value.copy(proveedor = proveedor)
        }
        viewModelScope.launch {
            repository.getComprasConPagosPorProveedor(proveedorId)
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
                .collect { compras ->
                    _uiState.value = _uiState.value.copy(compras = compras, isLoading = false)
                }
        }
    }

    fun registrarPago(compraId: Long, compraTotal: Double, monto: Double, notas: String?) {
        viewModelScope.launch {
            repository.registrarPago(compraId, compraTotal, monto, notas)
            _pagoGuardado.send(Unit)
        }
    }

    fun eliminarProveedor() {
        val proveedor = _uiState.value.proveedor ?: return
        viewModelScope.launch {
            repository.deleteProveedor(proveedor)
            _eliminado.send(Unit)
        }
    }
}
