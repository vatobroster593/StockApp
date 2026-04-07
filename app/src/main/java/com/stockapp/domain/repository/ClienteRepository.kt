package com.stockapp.domain.repository

import com.stockapp.data.local.entity.ClienteEntity
import com.stockapp.data.local.relation.ClienteConDeuda
import kotlinx.coroutines.flow.Flow

interface ClienteRepository {
    fun getClientes(): Flow<List<ClienteEntity>>
    fun buscarClientes(query: String): Flow<List<ClienteEntity>>
    fun getClientesConDeuda(): Flow<List<ClienteConDeuda>>
    suspend fun getClienteById(id: Long): ClienteEntity?
    suspend fun insertCliente(cliente: ClienteEntity): Long
    suspend fun updateCliente(cliente: ClienteEntity)
    suspend fun deleteCliente(cliente: ClienteEntity)
}
