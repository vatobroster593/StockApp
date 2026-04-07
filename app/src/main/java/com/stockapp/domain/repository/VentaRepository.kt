package com.stockapp.domain.repository

import com.stockapp.data.local.entity.VentaEntity
import com.stockapp.data.local.entity.VentaItemEntity
import com.stockapp.data.local.relation.VentaConDetalle
import kotlinx.coroutines.flow.Flow

interface VentaRepository {
    fun getVentasConDetalle(): Flow<List<VentaConDetalle>>
    fun getVentasPorEstado(estado: String): Flow<List<VentaConDetalle>>
    fun getVentasPorCliente(clienteId: Long): Flow<List<VentaConDetalle>>
    fun getVentaConDetalle(id: Long): Flow<VentaConDetalle?>
    fun getTotalCxCPendiente(): Flow<Double>
    fun getTotalVentasPorPeriodo(desde: Long, hasta: Long): Flow<Double>
    fun getCountVentasPorPeriodo(desde: Long, hasta: Long): Flow<Int>
    suspend fun saveVenta(
        venta: VentaEntity,
        items: List<VentaItemEntity>,
        abonoInicial: Double?
    ): Long
    suspend fun registrarAbono(ventaId: Long, ventaTotal: Double, monto: Double, notas: String?)
    suspend fun updateVenta(venta: VentaEntity)
}
