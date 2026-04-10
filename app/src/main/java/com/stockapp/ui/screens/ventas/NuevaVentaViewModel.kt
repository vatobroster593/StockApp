package com.stockapp.ui.screens.ventas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockapp.data.local.entity.ClienteEntity
import com.stockapp.data.local.entity.ProductoEntity
import com.stockapp.data.local.entity.VentaEntity
import com.stockapp.data.local.entity.VentaItemEntity
import com.stockapp.domain.model.EstadoPago
import com.stockapp.domain.model.TipoPrecio
import com.stockapp.domain.repository.ClienteRepository
import com.stockapp.domain.repository.ProductoRepository
import com.stockapp.domain.repository.VentaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ItemVentaUi(
    val productoId: Long,
    val productoNombre: String,
    val fotoUri: String?,
    val cantidad: Int,
    val tipoPrecio: TipoPrecio,
    val precioUnitario: Double
) {
    val subtotal: Double get() = cantidad * precioUnitario
}

@HiltViewModel
class NuevaVentaViewModel @Inject constructor(
    private val ventaRepository: VentaRepository,
    private val clienteRepository: ClienteRepository,
    private val productoRepository: ProductoRepository
) : ViewModel() {

    var paso by mutableStateOf(1)

    private val _busquedaCliente = MutableStateFlow("")
    var busquedaCliente: String
        get() = _busquedaCliente.value
        set(v) { _busquedaCliente.value = v }

    var clienteSeleccionado by mutableStateOf<ClienteEntity?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val clientes: StateFlow<List<ClienteEntity>> = _busquedaCliente
        .flatMapLatest { q ->
            if (q.isBlank()) clienteRepository.getClientes()
            else clienteRepository.buscarClientes(q)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _busquedaProducto = MutableStateFlow("")
    var busquedaProducto: String
        get() = _busquedaProducto.value
        set(v) { _busquedaProducto.value = v }

    val productos: StateFlow<List<ProductoEntity>> = combine(
        productoRepository.getProductos(),
        _busquedaProducto
    ) { lista, q ->
        if (q.isBlank()) lista
        else lista.filter { it.nombre.contains(q, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val items = mutableStateListOf<ItemVentaUi>()

    val subtotal: Double get() = items.sumOf { it.subtotal }
    val total: Double get() = subtotal

    var estadoPago by mutableStateOf(EstadoPago.PAGADO)
    var abonoInicial by mutableStateOf("")
    var notas by mutableStateOf("")
    var isSaving by mutableStateOf(false)
    var errorPago by mutableStateOf<String?>(null)

    private val _ventaGuardada = MutableSharedFlow<Long>()
    val ventaGuardada = _ventaGuardada.asSharedFlow()

    fun seleccionarCliente(cliente: ClienteEntity?) {
        clienteSeleccionado = cliente
        paso = 2
    }

    fun agregarItem(
        producto: ProductoEntity,
        cantidad: Int,
        tipoPrecio: TipoPrecio,
        precioNegociado: Double?
    ) {
        val precio = when (tipoPrecio) {
            TipoPrecio.NORMAL    -> producto.precioNormal
            TipoPrecio.MAYOR     -> producto.precioPorMayor
            TipoPrecio.NEGOCIADO -> precioNegociado ?: producto.precioNormal
        }
        val idx = items.indexOfFirst { it.productoId == producto.id }
        if (idx >= 0) {
            items[idx] = items[idx].copy(
                cantidad = items[idx].cantidad + cantidad,
                tipoPrecio = tipoPrecio,
                precioUnitario = precio
            )
        } else {
            items.add(
                ItemVentaUi(
                    productoId = producto.id,
                    productoNombre = producto.nombre,
                    fotoUri = producto.fotoUri,
                    cantidad = cantidad,
                    tipoPrecio = tipoPrecio,
                    precioUnitario = precio
                )
            )
        }
    }

    fun removeItem(index: Int) = items.removeAt(index)

    fun guardarVenta() {
        errorPago = null
        if (items.isEmpty()) { errorPago = "Agrega al menos un producto"; return }
        if (estadoPago == EstadoPago.PARCIAL) {
            val ab = abonoInicial.toDoubleOrNull()
            if (ab == null || ab <= 0) { errorPago = "Ingresa el monto del abono inicial"; return }
            if (ab >= total) { errorPago = "El abono no puede cubrir el total (usa PAGADO)"; return }
        }

        viewModelScope.launch {
            isSaving = true
            try {
                val venta = VentaEntity(
                    clienteId = clienteSeleccionado?.id,
                    subtotal = subtotal,
                    descuento = 0.0,
                    total = total,
                    estadoPago = estadoPago.name,
                    notas = notas.trim().ifBlank { null }
                )
                val ventaItems = items.map {
                    VentaItemEntity(
                        ventaId = 0,
                        productoId = it.productoId,
                        productoNombre = it.productoNombre,
                        cantidad = it.cantidad,
                        tipoPrecio = it.tipoPrecio.name,
                        precioUnitario = it.precioUnitario,
                        subtotal = it.subtotal
                    )
                }
                val abono = if (estadoPago == EstadoPago.PARCIAL)
                    abonoInicial.toDoubleOrNull()?.takeIf { it > 0 } else null

                val id = ventaRepository.saveVenta(venta, ventaItems, abono)
                _ventaGuardada.emit(id)
            } finally {
                isSaving = false
            }
        }
    }

    fun reset() {
        paso = 1
        clienteSeleccionado = null
        busquedaCliente = ""
        busquedaProducto = ""
        items.clear()
        estadoPago = EstadoPago.PAGADO
        abonoInicial = ""
        notas = ""
        errorPago = null
    }
}
