package com.stockapp.ui.screens.inventario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockapp.data.local.relation.ProductoConVariantes
import com.stockapp.domain.repository.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InventarioUiState(
    val productos: List<ProductoConVariantes> = emptyList(),
    val searchQuery: String = "",
    val categoriaFiltro: String? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class InventarioViewModel @Inject constructor(
    private val repository: ProductoRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _categoriaFiltro = MutableStateFlow<String?>(null)

    val uiState: StateFlow<InventarioUiState> = combine(
        repository.getProductosConVariantes(),
        _searchQuery,
        _categoriaFiltro
    ) { productos, query, categoria ->
        val filtrados = productos.filter { pv ->
            val matchCategoria = categoria == null || pv.producto.categoria == categoria
            val matchQuery = query.isBlank() ||
                pv.producto.nombre.contains(query, ignoreCase = true)
            matchCategoria && matchQuery
        }
        InventarioUiState(
            productos = filtrados,
            searchQuery = query,
            categoriaFiltro = categoria,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = InventarioUiState()
    )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategoriaFiltro(categoria: String?) {
        _categoriaFiltro.value = categoria
    }

    fun deleteProducto(id: Long) {
        viewModelScope.launch { repository.deleteProducto(id) }
    }
}
