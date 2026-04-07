package com.stockapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "venta_items",
    foreignKeys = [
        ForeignKey(
            entity = VentaEntity::class,
            parentColumns = ["id"],
            childColumns = ["ventaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductoEntity::class,
            parentColumns = ["id"],
            childColumns = ["productoId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("ventaId"), Index("productoId")]
)
data class VentaItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ventaId: Long,
    val productoId: Long,
    val productoNombre: String = "",   // guardado al momento de la venta
    val varianteId: Long? = null,      // null si el producto no tiene variantes
    val varianteLabel: String? = null, // ej: "Talla: M" guardado al vender
    val cantidad: Int,
    val tipoPrecio: String,            // NORMAL, MAYOR, NEGOCIADO
    val precioUnitario: Double,
    val subtotal: Double
)
