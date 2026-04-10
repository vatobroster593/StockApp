package com.stockapp.data.repository

import com.stockapp.data.local.dao.ProductoDao
import com.stockapp.data.local.entity.ProductoEntity
import com.stockapp.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductoRepositoryImpl @Inject constructor(
    private val dao: ProductoDao
) : ProductoRepository {

    override fun getProductos(): Flow<List<ProductoEntity>> = dao.getProductos()

    override fun getProducto(id: Long): Flow<ProductoEntity?> = dao.getProducto(id)

    override fun getProductosConStockBajo(umbral: Int): Flow<List<ProductoEntity>> =
        dao.getProductosConStockBajo(umbral)

    override suspend fun insertProducto(producto: ProductoEntity): Long =
        dao.insertProducto(producto)

    override suspend fun updateProducto(producto: ProductoEntity) =
        dao.updateProducto(producto)

    override suspend fun deleteProducto(id: Long) =
        dao.deleteProducto(id)
}
