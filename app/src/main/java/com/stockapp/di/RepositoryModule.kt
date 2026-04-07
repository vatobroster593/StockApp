package com.stockapp.di

import com.stockapp.data.repository.ClienteRepositoryImpl
import com.stockapp.data.repository.ProductoRepositoryImpl
import com.stockapp.data.repository.VentaRepositoryImpl
import com.stockapp.domain.repository.ClienteRepository
import com.stockapp.domain.repository.ProductoRepository
import com.stockapp.domain.repository.VentaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindProductoRepository(impl: ProductoRepositoryImpl): ProductoRepository

    @Binds @Singleton
    abstract fun bindClienteRepository(impl: ClienteRepositoryImpl): ClienteRepository

    @Binds @Singleton
    abstract fun bindVentaRepository(impl: VentaRepositoryImpl): VentaRepository
}
