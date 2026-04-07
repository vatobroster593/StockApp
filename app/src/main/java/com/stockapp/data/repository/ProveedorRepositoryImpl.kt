package com.stockapp.data.repository

import androidx.room.withTransaction
import com.stockapp.data.local.StockDatabase
import com.stockapp.data.local.dao.ProveedorDao
import com.stockapp.data.local.entity.CompraProveedorEntity
import com.stockapp.data.local.entity.PagoProveedorEntity
import com.stockapp.data.local.entity.ProveedorEntity
import com.stockapp.data.local.relation.CompraConPagos
import com.stockapp.data.local.relation.ProveedorConDeuda
import com.stockapp.domain.model.EstadoCompra
import com.stockapp.domain.repository.ProveedorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProveedorRepositoryImpl @Inject constructor(
    private val database: StockDatabase,
    private val proveedorDao: ProveedorDao
) : ProveedorRepository {

    override fun getProveedoresConDeuda(): Flow<List<ProveedorConDeuda>> =
        proveedorDao.getProveedoresConDeuda()

    override suspend fun getProveedorById(id: Long): ProveedorEntity? =
        proveedorDao.getProveedorById(id)

    override fun getComprasConPagosPorProveedor(proveedorId: Long): Flow<List<CompraConPagos>> =
        proveedorDao.getComprasConPagosPorProveedor(proveedorId)

    override fun getTotalCxPPendiente(): Flow<Double> =
        proveedorDao.getTotalCxPPendiente()

    override suspend fun saveProveedor(proveedor: ProveedorEntity): Long =
        proveedorDao.insertProveedor(proveedor)

    override suspend fun updateProveedor(proveedor: ProveedorEntity) =
        proveedorDao.updateProveedor(proveedor)

    override suspend fun deleteProveedor(proveedor: ProveedorEntity) =
        proveedorDao.deleteProveedor(proveedor)

    override suspend fun saveCompra(compra: CompraProveedorEntity, pagoInicial: Double?): Long =
        database.withTransaction {
            val compraId = proveedorDao.insertCompra(compra)
            if (pagoInicial != null && pagoInicial > 0) {
                proveedorDao.insertPago(
                    PagoProveedorEntity(compraProveedorId = compraId, monto = pagoInicial)
                )
            }
            compraId
        }

    override suspend fun registrarPago(
        compraId: Long,
        compraTotal: Double,
        monto: Double,
        notas: String?
    ) = database.withTransaction {
        proveedorDao.insertPago(
            PagoProveedorEntity(compraProveedorId = compraId, monto = monto, notas = notas)
        )
        val totalPagado = proveedorDao.getTotalPagadoPorCompra(compraId)
        val compra = proveedorDao.getCompraById(compraId) ?: return@withTransaction
        val nuevoEstado = when {
            totalPagado >= compraTotal -> EstadoCompra.PAGADO.name
            totalPagado > 0           -> EstadoCompra.PARCIAL.name
            else                      -> EstadoCompra.PENDIENTE.name
        }
        proveedorDao.updateCompra(compra.copy(estadoPago = nuevoEstado))
    }
}
