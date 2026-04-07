package com.stockapp.data.local.relation

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.stockapp.data.local.entity.ProveedorEntity

data class ProveedorConDeuda(
    @Embedded val proveedor: ProveedorEntity,
    @ColumnInfo(name = "deudaPendiente")
    val deudaPendiente: Double = 0.0
)
