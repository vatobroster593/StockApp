package com.stockapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ventas",
    foreignKeys = [
        ForeignKey(
            entity = ClienteEntity::class,
            parentColumns = ["id"],
            childColumns = ["clienteId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("clienteId")]
)
data class VentaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val clienteId: Long? = null,     // null = venta sin cliente registrado
    val fecha: Long = System.currentTimeMillis(),
    val subtotal: Double,
    val descuento: Double = 0.0,
    val total: Double,
    val estadoPago: String,          // PAGADO, FIADO, PARCIAL
    val notas: String? = null
)
