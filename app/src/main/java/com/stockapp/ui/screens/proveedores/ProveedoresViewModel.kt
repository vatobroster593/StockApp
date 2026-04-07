package com.stockapp.ui.screens.proveedores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockapp.data.local.relation.ProveedorConDeuda
import com.stockapp.domain.repository.ProveedorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProveedoresUiState(
    val proveedores: List<ProveedorConDeuda> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ProveedoresViewModel @Inject constructor(
    private val repository: ProveedorRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: MutableStateFlow<String> = _searchQuery

    val uiState = combine(
        repository.getProveedoresConDeuda(),
        _searchQuery
    ) { proveedores, query ->
        val filtrados = if (query.isBlank()) proveedores
        else proveedores.filter { it.proveedor.nombre.contains(query, ignoreCase = true) }
        ProveedoresUiState(proveedores = filtrados, isLoading = false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProveedoresUiState())

    fun onSearchChange(query: String) { _searchQuery.value = query }
}
