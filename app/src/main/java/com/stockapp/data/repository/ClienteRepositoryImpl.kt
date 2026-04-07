package com.stockapp.data.repository

import com.stockapp.data.local.dao.ClienteDao
import com.stockapp.data.local.entity.ClienteEntity
import com.stockapp.data.local.relation.ClienteConDeuda
import com.stockapp.domain.repository.ClienteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClienteRepositoryImpl @Inject constructor(
    private val dao: ClienteDao
) : ClienteRepository {

    override fun getClientes(): Flow<List<ClienteEntity>> = dao.getClientes()

    override fun buscarClientes(query: String): Flow<List<ClienteEntity>> =
        dao.buscarClientes(query)

    override fun getClientesConDeuda(): Flow<List<ClienteConDeuda>> =
        dao.getClientesConDeuda()

    override suspend fun getClienteById(id: Long): ClienteEntity? =
        dao.getClienteById(id)

    override suspend fun insertCliente(cliente: ClienteEntity): Long =
        dao.insertCliente(cliente)

    override suspend fun updateCliente(cliente: ClienteEntity) =
        dao.updateCliente(cliente)

    override suspend fun deleteCliente(cliente: ClienteEntity) =
        dao.deleteCliente(cliente)
}
