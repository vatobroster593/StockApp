package com.stockapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stockapp.data.local.dao.ClienteDao
import com.stockapp.data.local.dao.ProductoDao
import com.stockapp.data.local.dao.ProveedorDao
import com.stockapp.data.local.dao.VentaDao
import com.stockapp.data.local.entity.*

@Database(
    entities = [
        ProductoEntity::class,
        VarianteEntity::class,
        ClienteEntity::class,
        VentaEntity::class,
        VentaItemEntity::class,
        AbonoEntity::class,
        ProveedorEntity::class,
        CompraProveedorEntity::class,
        PagoProveedorEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class StockDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun clienteDao(): ClienteDao
    abstract fun ventaDao(): VentaDao
    abstract fun proveedorDao(): ProveedorDao
}
