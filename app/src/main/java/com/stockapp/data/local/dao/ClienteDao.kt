package com.stockapp.data.local.dao

import androidx.room.*
import com.stockapp.data.local.entity.ClienteEntity
import com.stockapp.data.local.relation.ClienteConDeuda
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {

    @Query("SELECT * FROM clientes ORDER BY nombre ASC")
    fun getClientes(): Flow<List<ClienteEntity>>

    @Query("SELECT * FROM clientes WHERE nombre LIKE '%' || :query || '%' ORDER BY nombre ASC")
    fun buscarClientes(query: String): Flow<List<ClienteEntity>>

    @Query("SELECT * FROM clientes WHERE id = :id")
    suspend fun getClienteById(id: Long): ClienteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCliente(cliente: ClienteEntity): Long

    @Update
    suspend fun updateCliente(cliente: ClienteEntity)

    @Delete
    suspend fun deleteCliente(cliente: ClienteEntity)

    // Saldo deudor por cliente (ventas FIADO + PARCIAL menos abonos)
    @Query("""
        SELECT c.*,
               COALESCE(SUM(v.total), 0) - COALESCE(SUM(a.monto), 0) AS saldoPendiente
        FROM clientes c
        LEFT JOIN ventas v ON v.clienteId = c.id AND v.estadoPago IN ('FIADO', 'PARCIAL')
        LEFT JOIN abonos a ON a.ventaId = v.id
        GROUP BY c.id
        ORDER BY c.nombre ASC
    """)
    fun getClientesConDeuda(): Flow<List<ClienteConDeuda>>
}
