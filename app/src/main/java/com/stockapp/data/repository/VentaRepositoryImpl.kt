package com.stockapp.data.repository

import androidx.room.withTransaction
import com.stockapp.data.local.StockDatabase
import com.stockapp.data.local.dao.ProductoDao
import com.stockapp.data.local.dao.VentaDao
import com.stockapp.data.local.entity.AbonoEntity
import com.stockapp.data.local.entity.VentaEntity
import com.stockapp.data.local.entity.VentaItemEntity
import com.stockapp.data.local.relation.VentaConDetalle
import com.stockapp.domain.model.EstadoPago
import com.stockapp.domain.repository.VentaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class VentaRepositoryImpl @Inject constructor(
    private val database: StockDatabase,
    private val ventaDao: VentaDao,
    private val productoDao: ProductoDao
) : VentaRepository {

    override fun getVentasConDetalle(): Flow<List<VentaConDetalle>> =
        ventaDao.getVentasConDetalle()

    override fun getVentasPorEstado(estado: String): Flow<List<VentaConDetalle>> =
        ventaDao.getVentasPorEstado(estado)

    override fun getVentasPorCliente(clienteId: Long): Flow<List<VentaConDetalle>> =
        ventaDao.getVentasPorCliente(clienteId)

    override fun getVentaConDetalle(id: Long): Flow<VentaConDetalle?> =
        ventaDao.getVentaConDetalle(id)

    override fun getTotalCxCPendiente(): Flow<Double> =
        ventaDao.getTotalCxCPendiente()

    override fun getTotalVentasPorPeriodo(desde: Long, hasta: Long): Flow<Double> =
        ventaDao.getTotalVentasPorPeriodo(desde, hasta)

    override fun getCountVentasPorPeriodo(desde: Long, hasta: Long): Flow<Int> =
        ventaDao.getCountVentasPorPeriodo(desde, hasta)

    override suspend fun saveVenta(
        venta: VentaEntity,
        items: List<VentaItemEntity>,
        abonoInicial: Double?
    ): Long = database.withTransaction {
        val ventaId = ventaDao.insertVenta(venta)
        ventaDao.insertVentaItems(items.map { it.copy(ventaId = ventaId) })
        // Decrementar stock de cada producto vendido
        items.forEach { item ->
            productoDao.decrementarStock(item.productoId, item.cantidad)
        }
        // Abono inicial si pago PARCIAL
        if (abonoInicial != null && abonoInicial > 0) {
            ventaDao.insertAbono(AbonoEntity(ventaId = ventaId, monto = abonoInicial))
        }
        ventaId
    }

    override suspend fun registrarAbono(
        ventaId: Long,
        ventaTotal: Double,
        monto: Double,
        notas: String?
    ) = database.withTransaction {
        ventaDao.insertAbono(AbonoEntity(ventaId = ventaId, monto = monto, notas = notas))
        val totalAbonado = ventaDao.getTotalAbonadoByVenta(ventaId)
        val venta = ventaDao.getVentaById(ventaId) ?: return@withTransaction
        val nuevoEstado = when {
            totalAbonado >= ventaTotal -> EstadoPago.PAGADO.name
            totalAbonado > 0          -> EstadoPago.PARCIAL.name
            else                      -> EstadoPago.FIADO.name
        }
        ventaDao.updateVenta(venta.copy(estadoPago = nuevoEstado))
    }

    override suspend fun updateVenta(venta: VentaEntity) =
        ventaDao.updateVenta(venta)

    override suspend fun getVentasPorFechaSnapshot(desde: Long, hasta: Long): List<VentaConDetalle> =
        ventaDao.getVentasPorFecha(desde, hasta).first()
}
