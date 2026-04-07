package com.stockapp.data.local.relation

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.stockapp.data.local.entity.ClienteEntity

data class ClienteConDeuda(
    @Embedded val cliente: ClienteEntity,
    @ColumnInfo(name = "saldoPendiente")
    val saldoPendiente: Double = 0.0
)
