package com.stockapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    version = 2,
    exportSchema = true
)
abstract class StockDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun clienteDao(): ClienteDao
    abstract fun ventaDao(): VentaDao
    abstract fun proveedorDao(): ProveedorDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE venta_items ADD COLUMN productoNombre TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE venta_items ADD COLUMN varianteLabel TEXT")
            }
        }
    }
}
