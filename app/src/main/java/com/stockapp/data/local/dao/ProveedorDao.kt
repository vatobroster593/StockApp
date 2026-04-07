package com.stockapp.data.local.dao

import androidx.room.*
import com.stockapp.data.local.entity.CompraProveedorEntity
import com.stockapp.data.local.entity.PagoProveedorEntity
import com.stockapp.data.local.entity.ProveedorEntity
import com.stockapp.data.local.relation.ProveedorConDeuda
import kotlinx.coroutines.flow.Flow

@Dao
interface ProveedorDao {

    @Query("SELECT * FROM proveedores ORDER BY nombre ASC")
    fun getProveedores(): Flow<List<ProveedorEntity>>

    @Query("SELECT * FROM proveedores WHERE id = :id")
    suspend fun getProveedorById(id: Long): ProveedorEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProveedor(proveedor: ProveedorEntity): Long

    @Update
    suspend fun updateProveedor(proveedor: ProveedorEntity)

    @Delete
    suspend fun deleteProveedor(proveedor: ProveedorEntity)

    // Compras
    @Query("SELECT * FROM compras_proveedor WHERE proveedorId = :proveedorId ORDER BY fecha DESC")
    fun getComprasPorProveedor(proveedorId: Long): Flow<List<CompraProveedorEntity>>

    @Insert
    suspend fun insertCompra(compra: CompraProveedorEntity): Long

    @Update
    suspend fun updateCompra(compra: CompraProveedorEntity)

    // Pagos
    @Insert
    suspend fun insertPago(pago: PagoProveedorEntity): Long

    @Query("SELECT * FROM pagos_proveedor WHERE compraProveedorId = :compraId ORDER BY fecha DESC")
    fun getPagosPorCompra(compraId: Long): Flow<List<PagoProveedorEntity>>

    @Query("SELECT COALESCE(SUM(monto), 0) FROM pagos_proveedor WHERE compraProveedorId = :compraId")
    suspend fun getTotalPagadoPorCompra(compraId: Long): Double

    // Dashboard: total CxP pendiente
    @Query("""
        SELECT COALESCE(SUM(c.total), 0) - COALESCE(SUM(p.monto), 0)
        FROM compras_proveedor c
        LEFT JOIN pagos_proveedor p ON p.compraProveedorId = c.id
        WHERE c.estadoPago IN ('PENDIENTE', 'PARCIAL')
    """)
    fun getTotalCxPPendiente(): Flow<Double>

    // Proveedores con deuda
    @Query("""
        SELECT pv.*,
               COALESCE(SUM(c.total), 0) - COALESCE(SUM(p.monto), 0) AS deudaPendiente
        FROM proveedores pv
        LEFT JOIN compras_proveedor c ON c.proveedorId = pv.id AND c.estadoPago IN ('PENDIENTE', 'PARCIAL')
        LEFT JOIN pagos_proveedor p ON p.compraProveedorId = c.id
        GROUP BY pv.id
        ORDER BY pv.nombre ASC
    """)
    fun getProveedoresConDeuda(): Flow<List<ProveedorConDeuda>>
}
