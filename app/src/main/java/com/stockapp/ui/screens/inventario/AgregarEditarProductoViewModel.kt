package com.stockapp.ui.screens.inventario

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockapp.data.local.entity.ProductoEntity
import com.stockapp.domain.model.Categoria
import com.stockapp.domain.repository.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgregarEditarProductoViewModel @Inject constructor(
    private val repository: ProductoRepository
) : ViewModel() {

    var nombre by mutableStateOf("")
    var descripcion by mutableStateOf("")
    var categoria by mutableStateOf(Categoria.ROPA.name)
    var precioNormal by mutableStateOf("")
    var precioPorMayor by mutableStateOf("")
    var color by mutableStateOf("")
    var talla by mutableStateOf("")
    var stock by mutableStateOf("0")
    var fotoUri by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    private val _productoGuardado = MutableSharedFlow<Long>()
    val productoGuardado = _productoGuardado.asSharedFlow()

    fun loadProducto(id: Long) {
        viewModelScope.launch {
            val p = repository.getProducto(id).first() ?: return@launch
            nombre = p.nombre
            descripcion = p.descripcion ?: ""
            categoria = p.categoria
            precioNormal = p.precioNormal.toString()
            precioPorMayor = p.precioPorMayor.toString()
            color = p.color ?: ""
            talla = p.talla ?: ""
            stock = p.stock.toString()
            fotoUri = p.fotoUri
        }
    }

    /** Carga datos de otro producto para duplicar (sin ID, sin stock) */
    fun loadParaDuplicar(id: Long) {
        viewModelScope.launch {
            val p = repository.getProducto(id).first() ?: return@launch
            nombre = p.nombre
            descripcion = p.descripcion ?: ""
            categoria = p.categoria
            precioNormal = p.precioNormal.toString()
            precioPorMayor = p.precioPorMayor.toString()
            color = p.color ?: ""
            talla = p.talla ?: ""
            stock = "0"
            fotoUri = p.fotoUri
        }
    }

    fun guardar(productoId: Long? = null) {
        errorMessage = null
        val normal = precioNormal.toDoubleOrNull()
        val mayor = precioPorMayor.toDoubleOrNull()
        val stockInt = stock.toIntOrNull()

        if (nombre.isBlank()) { errorMessage = "El nombre es requerido"; return }
        if (normal == null || normal <= 0) { errorMessage = "Ingresa un precio normal válido"; return }
        if (mayor == null || mayor <= 0) { errorMessage = "Ingresa un precio por mayor válido"; return }
        if (stockInt == null || stockInt < 0) { errorMessage = "El stock debe ser un número positivo"; return }

        viewModelScope.launch {
            isLoading = true
            try {
                val entity = ProductoEntity(
                    id = productoId ?: 0L,
                    nombre = nombre.trim(),
                    descripcion = descripcion.trim().ifBlank { null },
                    categoria = categoria,
                    precioNormal = normal,
                    precioPorMayor = mayor,
                    color = color.trim().ifBlank { null },
                    talla = talla.trim().ifBlank { null },
                    stock = stockInt,
                    fotoUri = fotoUri
                )
                if (productoId == null) {
                    val nuevoId = repository.insertProducto(entity)
                    _productoGuardado.emit(nuevoId)
                } else {
                    repository.updateProducto(entity)
                    _productoGuardado.emit(productoId)
                }
            } finally {
                isLoading = false
            }
        }
    }
}
