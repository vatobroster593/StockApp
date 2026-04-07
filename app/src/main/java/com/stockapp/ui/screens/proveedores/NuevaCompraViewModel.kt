package com.stockapp.ui.screens.proveedores

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockapp.data.local.entity.CompraProveedorEntity
import com.stockapp.domain.model.EstadoCompra
import com.stockapp.domain.repository.ProveedorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NuevaCompraViewModel @Inject constructor(
    private val repository: ProveedorRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val proveedorId: Long = savedStateHandle.get<Long>("proveedorId") ?: 0L

    var descripcion by mutableStateOf("")
    var totalStr by mutableStateOf("")
    var estadoPago by mutableStateOf(EstadoCompra.PENDIENTE)
    var pagoInicialStr by mutableStateOf("")

    var errorDescripcion by mutableStateOf(false)
    var errorTotal by mutableStateOf(false)
    var errorPagoInicial by mutableStateOf(false)

    private val _guardado = Channel<Unit>(Channel.BUFFERED)
    val guardado = _guardado.receiveAsFlow()

    fun guardar() {
        errorDescripcion = descripcion.isBlank()
        val total = totalStr.toDoubleOrNull()
        errorTotal = total == null || total <= 0
        val pagoInicial = if (estadoPago == EstadoCompra.PARCIAL) pagoInicialStr.toDoubleOrNull() else null
        errorPagoInicial = estadoPago == EstadoCompra.PARCIAL && (pagoInicial == null || pagoInicial <= 0)

        if (errorDescripcion || errorTotal || errorPagoInicial) return

        viewModelScope.launch {
            repository.saveCompra(
                compra = CompraProveedorEntity(
                    proveedorId = proveedorId,
                    descripcion = descripcion.trim(),
                    total = total!!,
                    estadoPago = estadoPago.name
                ),
                pagoInicial = pagoInicial
            )
            _guardado.send(Unit)
        }
    }
}
