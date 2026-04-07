package com.stockapp.data.local.dao

import androidx.room.*
import com.stockapp.data.local.entity.AbonoEntity
import com.stockapp.data.local.entity.VentaEntity
import com.stockapp.data.local.entity.VentaItemEntity
import com.stockapp.data.local.relation.VentaConDetalle
import kotlinx.coroutines.flow.Flow

@Dao
interface VentaDao {

    @Transaction
    @Query("SELECT * FROM ventas ORDER BY fecha DESC")
    fun getVentasConDetalle(): Flow<List<VentaConDetalle>>

    @Transaction
    @Query("SELECT * FROM ventas WHERE estadoPago = :estado ORDER BY fecha DESC")
    fun getVentasPorEstado(estado: String): Flow<List<VentaConDetalle>>

    @Transaction
    @Query("SELECT * FROM ventas WHERE clienteId = :clienteId ORDER BY fecha DESC")
    fun getVentasPorCliente(clienteId: Long): Flow<List<VentaConDetalle>>

    @Transaction
    @Query("SELECT * FROM ventas WHERE fecha BETWEEN :desde AND :hasta ORDER BY fecha DESC")
    fun getVentasPorFecha(desde: Long, hasta: Long): Flow<List<VentaConDetalle>>

    @Transaction
    @Query("SELECT * FROM ventas WHERE id = :id")
    fun getVentaConDetalle(id: Long): Flow<VentaConDetalle?>

    @Insert
    suspend fun insertVenta(venta: VentaEntity): Long

    @Insert
    suspend fun insertVentaItems(items: List<VentaItemEntity>)

    @Update
    suspend fun updateVenta(venta: VentaEntity)

    // Abonos
    @Insert
    suspend fun insertAbono(abono: AbonoEntity): Long

    @Query("SELECT * FROM abonos WHERE ventaId = :ventaId ORDER BY fecha DESC")
    fun getAbonosByVenta(ventaId: Long): Flow<List<AbonoEntity>>

    @Query("SELECT COALESCE(SUM(monto), 0) FROM abonos WHERE ventaId = :ventaId")
    suspend fun getTotalAbonadoByVenta(ventaId: Long): Double

    // Dashboard: total CxC pendiente
    @Query("""
        SELECT COALESCE(SUM(v.total), 0) - COALESCE(SUM(a.monto), 0)
        FROM ventas v
        LEFT JOIN abonos a ON a.ventaId = v.id
        WHERE v.estadoPago IN ('FIADO', 'PARCIAL')
    """)
    fun getTotalCxCPendiente(): Flow<Double>

    // Dashboard: ventas del dia
    @Query("SELECT COALESCE(SUM(total), 0) FROM ventas WHERE fecha BETWEEN :desde AND :hasta")
    fun getTotalVentasPorPeriodo(desde: Long, hasta: Long): Flow<Double>

    // Dashboard: count ventas del dia
    @Query("SELECT COUNT(*) FROM ventas WHERE fecha BETWEEN :desde AND :hasta")
    fun getCountVentasPorPeriodo(desde: Long, hasta: Long): Flow<Int>

    @Query("SELECT * FROM ventas WHERE id = :id")
    suspend fun getVentaById(id: Long): VentaEntity?
}
