package com.stockapp.domain.repository

import com.stockapp.data.local.entity.CompraProveedorEntity
import com.stockapp.data.local.entity.ProveedorEntity
import com.stockapp.data.local.relation.CompraConPagos
import com.stockapp.data.local.relation.ProveedorConDeuda
import kotlinx.coroutines.flow.Flow

interface ProveedorRepository {
    fun getProveedoresConDeuda(): Flow<List<ProveedorConDeuda>>
    suspend fun getProveedorById(id: Long): ProveedorEntity?
    fun getComprasConPagosPorProveedor(proveedorId: Long): Flow<List<CompraConPagos>>
    fun getTotalCxPPendiente(): Flow<Double>
    suspend fun saveProveedor(proveedor: ProveedorEntity): Long
    suspend fun updateProveedor(proveedor: ProveedorEntity)
    suspend fun deleteProveedor(proveedor: ProveedorEntity)
    suspend fun saveCompra(compra: CompraProveedorEntity, pagoInicial: Double?): Long
    suspend fun registrarPago(compraId: Long, compraTotal: Double, monto: Double, notas: String?)
}
