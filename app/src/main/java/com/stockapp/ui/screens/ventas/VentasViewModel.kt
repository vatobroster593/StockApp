package com.stockapp.ui.screens.ventas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockapp.data.local.entity.AbonoEntity
import com.stockapp.data.local.relation.VentaConDetalle
import com.stockapp.domain.repository.VentaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VentasUiState(
    val ventas: List<VentaConDetalle> = emptyList(),
    val filtroEstado: String? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class VentasViewModel @Inject constructor(
    private val repository: VentaRepository
) : ViewModel() {

    private val _filtroEstado = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<VentasUiState> = _filtroEstado
        .flatMapLatest { estado ->
            if (estado == null) repository.getVentasConDetalle()
            else repository.getVentasPorEstado(estado)
        }
        .map { ventas -> VentasUiState(ventas = ventas, filtroEstado = _filtroEstado.value, isLoading = false) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), VentasUiState())

    fun setFiltroEstado(estado: String?) { _filtroEstado.value = estado }
}

// --- ViewModel para el detalle de una venta ---
@HiltViewModel
class DetalleVentaViewModel @Inject constructor(
    private val repository: VentaRepository
) : ViewModel() {

    fun getVenta(id: Long): Flow<VentaConDetalle?> = repository.getVentaConDetalle(id)

    var montoAbono by mutableStateOf("")
    var notasAbono by mutableStateOf("")
    var isSavingAbono by mutableStateOf(false)
    var errorAbono by mutableStateOf<String?>(null)

    private val _abonoGuardado = MutableSharedFlow<Unit>()
    val abonoGuardado = _abonoGuardado.asSharedFlow()

    fun registrarAbono(ventaId: Long, ventaTotal: Double) {
        errorAbono = null
        val monto = montoAbono.toDoubleOrNull()
        if (monto == null || monto <= 0) { errorAbono = "Ingresa un monto válido"; return }

        viewModelScope.launch {
            isSavingAbono = true
            repository.registrarAbono(
                ventaId = ventaId,
                ventaTotal = ventaTotal,
                monto = monto,
                notas = notasAbono.trim().ifBlank { null }
            )
            montoAbono = ""
            notasAbono = ""
            _abonoGuardado.emit(Unit)
            isSavingAbono = false
        }
    }
}
