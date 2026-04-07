package com.stockapp.domain.repository

import com.stockapp.data.local.entity.ProductoEntity
import com.stockapp.data.local.entity.VarianteEntity
import com.stockapp.data.local.relation.ProductoConVariantes
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    fun getProductosConVariantes(): Flow<List<ProductoConVariantes>>
    fun getProductoConVariantes(id: Long): Flow<ProductoConVariantes?>
    fun getProductosConStockBajo(umbral: Int = 3): Flow<List<ProductoConVariantes>>
    suspend fun insertProducto(producto: ProductoEntity): Long
    suspend fun updateProducto(producto: ProductoEntity)
    suspend fun deleteProducto(id: Long)
    suspend fun insertVariante(variante: VarianteEntity): Long
    suspend fun insertVariantes(variantes: List<VarianteEntity>)
    suspend fun updateVariante(variante: VarianteEntity)
    suspend fun deleteVariante(id: Long)
}
