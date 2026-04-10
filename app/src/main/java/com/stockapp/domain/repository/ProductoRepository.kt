package com.stockapp.domain.repository

import com.stockapp.data.local.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    fun getProductos(): Flow<List<ProductoEntity>>
    fun getProducto(id: Long): Flow<ProductoEntity?>
    fun getProductosConStockBajo(umbral: Int = 3): Flow<List<ProductoEntity>>
    suspend fun insertProducto(producto: ProductoEntity): Long
    suspend fun updateProducto(producto: ProductoEntity)
    suspend fun deleteProducto(id: Long)
}
