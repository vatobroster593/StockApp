package com.stockapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pagos_proveedor",
    foreignKeys = [
        ForeignKey(
            entity = CompraProveedorEntity::class,
            parentColumns = ["id"],
            childColumns = ["compraProveedorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("compraProveedorId")]
)
data class PagoProveedorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val compraProveedorId: Long,
    val fecha: Long = System.currentTimeMillis(),
    val monto: Double,
    val notas: String? = null
)
