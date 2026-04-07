package com.stockapp.ui.screens.clientes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockapp.data.local.entity.ClienteEntity
import com.stockapp.data.local.relation.ClienteConDeuda
import com.stockapp.domain.repository.ClienteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClientesUiState(
    val clientes: List<ClienteConDeuda> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true
)

@HiltViewModel
class ClientesViewModel @Inject constructor(
    private val repository: ClienteRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ClientesUiState> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) repository.getClientesConDeuda()
            else repository.buscarClientes(query).map { lista ->
                lista.map { ClienteConDeuda(it, 0.0) }
            }
        }
        .map { lista -> ClientesUiState(clientes = lista, searchQuery = _searchQuery.value, isLoading = false) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ClientesUiState())

    fun setSearchQuery(q: String) { _searchQuery.value = q }

    fun deleteCliente(cliente: ClienteEntity) {
        viewModelScope.launch { repository.deleteCliente(cliente) }
    }
}

// --- ViewModel para Agregar/Editar ---
@HiltViewModel
class AgregarEditarClienteViewModel @Inject constructor(
    private val repository: ClienteRepository
) : ViewModel() {

    var nombre by mutableStateOf("")
    var telefono by mutableStateOf("")
    var notas by mutableStateOf("")
    var isSaving by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    private val _guardado = MutableSharedFlow<Unit>()
    val guardado = _guardado.asSharedFlow()

    fun loadCliente(id: Long) {
        viewModelScope.launch {
            val c = repository.getClienteById(id) ?: return@launch
            nombre = c.nombre
            telefono = c.telefono ?: ""
            notas = c.notas ?: ""
        }
    }

    fun guardar(clienteId: Long? = null) {
        errorMessage = null
        if (nombre.isBlank()) { errorMessage = "El nombre es requerido"; return }
        viewModelScope.launch {
            isSaving = true
            val entity = ClienteEntity(
                id = clienteId ?: 0L,
                nombre = nombre.trim(),
                telefono = telefono.trim().ifBlank { null },
                notas = notas.trim().ifBlank { null }
            )
            if (clienteId == null) repository.insertCliente(entity)
            else repository.updateCliente(entity)
            _guardado.emit(Unit)
            isSaving = false
        }
    }
}
