package com.stockapp.ui.screens.inventario

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockapp.data.local.entity.ProductoEntity
import com.stockapp.data.local.entity.VarianteEntity
import com.stockapp.domain.model.Categoria
import com.stockapp.domain.repository.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VarianteForm(
    val id: Long? = null,
    val atributo: String = "Talla",
    val valor: String = "",
    val stock: String = "0"
)

@HiltViewModel
class AgregarEditarProductoViewModel @Inject constructor(
    private val repository: ProductoRepository
) : ViewModel() {

    var nombre by mutableStateOf("")
    var descripcion by mutableStateOf("")
    var categoria by mutableStateOf(Categoria.ROPA.name)
    var precioNormal by mutableStateOf("")
    var precioPorMayor by mutableStateOf("")
    var fotoUri by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    val variantes = mutableStateListOf(VarianteForm())

    private val _productoGuardado = MutableSharedFlow<Long>()
    val productoGuardado = _productoGuardado.asSharedFlow()

    fun loadProducto(id: Long) {
        viewModelScope.launch {
            val pv = repository.getProductoConVariantes(id).first() ?: return@launch
            nombre = pv.producto.nombre
            descripcion = pv.producto.descripcion ?: ""
            categoria = pv.producto.categoria
            precioNormal = pv.producto.precioNormal.toString()
            precioPorMayor = pv.producto.precioPorMayor.toString()
            fotoUri = pv.producto.fotoUri
            variantes.clear()
            pv.variantes.filter { it.activo }.forEach { v ->
                variantes.add(VarianteForm(id = v.id, atributo = v.atributo, valor = v.valor, stock = v.stock.toString()))
            }
            if (variantes.isEmpty()) variantes.add(VarianteForm())
        }
    }

    fun addVariante() {
        val ultimoAtributo = variantes.lastOrNull()?.atributo ?: "Talla"
        variantes.add(VarianteForm(atributo = ultimoAtributo))
    }

    fun removeVariante(index: Int) {
        if (variantes.size > 1) variantes.removeAt(index)
    }

    fun updateVariante(index: Int, form: VarianteForm) {
        if (index in variantes.indices) variantes[index] = form
    }

    fun guardar(productoId: Long? = null) {
        errorMessage = null
        val normal = precioNormal.toDoubleOrNull()
        val mayor = precioPorMayor.toDoubleOrNull()

        if (nombre.isBlank()) { errorMessage = "El nombre es requerido"; return }
        if (normal == null || normal <= 0) { errorMessage = "Ingresa un precio normal válido"; return }
        if (mayor == null || mayor <= 0) { errorMessage = "Ingresa un precio por mayor válido"; return }
        if (variantes.any { it.valor.isBlank() }) { errorMessage = "Completa el valor de todas las variantes"; return }

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
                    fotoUri = fotoUri
                )

                if (productoId == null) {
                    // Nuevo producto
                    val nuevoId = repository.insertProducto(entity)
                    repository.insertVariantes(variantes.map { v ->
                        VarianteEntity(
                            productoId = nuevoId,
                            atributo = v.atributo,
                            valor = v.valor,
                            stock = v.stock.toIntOrNull() ?: 0
                        )
                    })
                    _productoGuardado.emit(nuevoId)
                } else {
                    // Editar producto
                    repository.updateProducto(entity)
                    variantes.forEach { v ->
                        if (v.id != null) {
                            repository.updateVariante(
                                VarianteEntity(
                                    id = v.id, productoId = productoId,
                                    atributo = v.atributo, valor = v.valor,
                                    stock = v.stock.toIntOrNull() ?: 0
                                )
                            )
                        } else {
                            repository.insertVariante(
                                VarianteEntity(
                                    productoId = productoId,
                                    atributo = v.atributo, valor = v.valor,
                                    stock = v.stock.toIntOrNull() ?: 0
                                )
                            )
                        }
                    }
                    _productoGuardado.emit(productoId)
                }
            } finally {
                isLoading = false
            }
        }
    }
}
