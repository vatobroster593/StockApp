package com.stockapp.data.local.dao

import androidx.room.*
import com.stockapp.data.local.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos WHERE activo = 1 ORDER BY nombre ASC")
    fun getProductos(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE id = :id")
    fun getProducto(id: Long): Flow<ProductoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducto(producto: ProductoEntity): Long

    @Update
    suspend fun updateProducto(producto: ProductoEntity)

    @Query("UPDATE productos SET activo = 0 WHERE id = :id")
    suspend fun deleteProducto(id: Long)

    @Query("UPDATE productos SET stock = stock - :cantidad WHERE id = :productoId AND stock >= :cantidad")
    suspend fun decrementarStock(productoId: Long, cantidad: Int): Int

    @Query("SELECT * FROM productos WHERE activo = 1 AND stock <= :umbral ORDER BY stock ASC")
    fun getProductosConStockBajo(umbral: Int = 3): Flow<List<ProductoEntity>>
}
