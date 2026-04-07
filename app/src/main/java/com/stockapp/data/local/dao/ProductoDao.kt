package com.stockapp.data.local.dao

import androidx.room.*
import com.stockapp.data.local.entity.ProductoEntity
import com.stockapp.data.local.entity.VarianteEntity
import com.stockapp.data.local.relation.ProductoConVariantes
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Transaction
    @Query("SELECT * FROM productos WHERE activo = 1 ORDER BY nombre ASC")
    fun getProductosConVariantes(): Flow<List<ProductoConVariantes>>

    @Transaction
    @Query("SELECT * FROM productos WHERE activo = 1 AND categoria = :categoria ORDER BY nombre ASC")
    fun getProductosPorCategoria(categoria: String): Flow<List<ProductoConVariantes>>

    @Transaction
    @Query("SELECT * FROM productos WHERE activo = 1 AND nombre LIKE '%' || :query || '%' ORDER BY nombre ASC")
    fun buscarProductos(query: String): Flow<List<ProductoConVariantes>>

    @Transaction
    @Query("SELECT * FROM productos WHERE id = :id")
    fun getProductoConVariantes(id: Long): Flow<ProductoConVariantes?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducto(producto: ProductoEntity): Long

    @Update
    suspend fun updateProducto(producto: ProductoEntity)

    // Soft delete: marca como inactivo
    @Query("UPDATE productos SET activo = 0 WHERE id = :id")
    suspend fun deleteProducto(id: Long)

    // Variantes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariante(variante: VarianteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariantes(variantes: List<VarianteEntity>)

    @Update
    suspend fun updateVariante(variante: VarianteEntity)

    @Query("UPDATE variantes SET activo = 0 WHERE id = :id")
    suspend fun deleteVariante(id: Long)

    @Query("UPDATE variantes SET stock = stock - :cantidad WHERE id = :varianteId AND stock >= :cantidad")
    suspend fun decrementarStock(varianteId: Long, cantidad: Int): Int

    @Query("SELECT * FROM variantes WHERE productoId = :productoId AND activo = 1")
    fun getVariantesByProducto(productoId: Long): Flow<List<VarianteEntity>>

    // Stock bajo — para alertas del dashboard
    @Transaction
    @Query("""
        SELECT p.* FROM productos p
        INNER JOIN variantes v ON v.productoId = p.id
        WHERE p.activo = 1 AND v.activo = 1
        GROUP BY p.id
        HAVING SUM(v.stock) <= :umbral
        ORDER BY SUM(v.stock) ASC
    """)
    fun getProductosConStockBajo(umbral: Int = 3): Flow<List<ProductoConVariantes>>
}
