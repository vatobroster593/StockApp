package com.stockapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "compras_proveedor",
    foreignKeys = [
        ForeignKey(
            entity = ProveedorEntity::class,
            parentColumns = ["id"],
            childColumns = ["proveedorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("proveedorId")]
)
data class CompraProveedorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val proveedorId: Long,
    val fecha: Long = System.currentTimeMillis(),
    val descripcion: String,
    val total: Double,
    val estadoPago: String            // PAGADO, PENDIENTE, PARCIAL
)
