package com.stockapp.data.repository

import com.stockapp.data.local.dao.ProductoDao
import com.stockapp.data.local.entity.ProductoEntity
import com.stockapp.data.local.entity.VarianteEntity
import com.stockapp.data.local.relation.ProductoConVariantes
import com.stockapp.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductoRepositoryImpl @Inject constructor(
    private val dao: ProductoDao
) : ProductoRepository {

    override fun getProductosConVariantes(): Flow<List<ProductoConVariantes>> =
        dao.getProductosConVariantes()

    override fun getProductoConVariantes(id: Long): Flow<ProductoConVariantes?> =
        dao.getProductoConVariantes(id)

    override fun getProductosConStockBajo(umbral: Int): Flow<List<ProductoConVariantes>> =
        dao.getProductosConStockBajo(umbral)

    override suspend fun insertProducto(producto: ProductoEntity): Long =
        dao.insertProducto(producto)

    override suspend fun updateProducto(producto: ProductoEntity) =
        dao.updateProducto(producto)

    override suspend fun deleteProducto(id: Long) =
        dao.deleteProducto(id)

    override suspend fun insertVariante(variante: VarianteEntity): Long =
        dao.insertVariante(variante)

    override suspend fun insertVariantes(variantes: List<VarianteEntity>) =
        dao.insertVariantes(variantes)

    override suspend fun updateVariante(variante: VarianteEntity) =
        dao.updateVariante(variante)

    override suspend fun deleteVariante(id: Long) =
        dao.deleteVariante(id)
}
