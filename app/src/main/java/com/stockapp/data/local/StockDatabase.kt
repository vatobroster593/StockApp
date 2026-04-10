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
        ClienteEntity::class,
        VentaEntity::class,
        VentaItemEntity::class,
        AbonoEntity::class,
        ProveedorEntity::class,
        CompraProveedorEntity::class,
        PagoProveedorEntity::class
    ],
    version = 3,
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

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Agregar stock directo al producto (suma del stock de sus variantes)
                db.execSQL("ALTER TABLE productos ADD COLUMN stock INTEGER NOT NULL DEFAULT 0")
                db.execSQL("""
                    UPDATE productos SET stock = (
                        SELECT COALESCE(SUM(v.stock), 0)
                        FROM variantes v
                        WHERE v.productoId = productos.id AND v.activo = 1
                    )
                """.trimIndent())
                // Eliminar tabla variantes (ya no se usa)
                db.execSQL("DROP TABLE IF EXISTS variantes")
            }
        }
    }
}
