package com.stockapp.di

import android.content.Context
import androidx.room.Room
import com.stockapp.data.local.StockDatabase
import com.stockapp.data.local.dao.ClienteDao
import com.stockapp.data.local.dao.ProductoDao
import com.stockapp.data.local.dao.ProveedorDao
import com.stockapp.data.local.dao.VentaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideStockDatabase(@ApplicationContext context: Context): StockDatabase =
        Room.databaseBuilder(
            context,
            StockDatabase::class.java,
            "stock_database"
        )
        .addMigrations(StockDatabase.MIGRATION_1_2, StockDatabase.MIGRATION_2_3)
        .build()

    @Provides
    fun provideProductoDao(db: StockDatabase): ProductoDao = db.productoDao()

    @Provides
    fun provideClienteDao(db: StockDatabase): ClienteDao = db.clienteDao()

    @Provides
    fun provideVentaDao(db: StockDatabase): VentaDao = db.ventaDao()

    @Provides
    fun provideProveedorDao(db: StockDatabase): ProveedorDao = db.proveedorDao()
}
