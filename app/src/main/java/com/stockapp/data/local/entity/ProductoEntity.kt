package com.stockapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val descripcion: String? = null,
    val categoria: String,           // ROPA, ZAPATOS, BOLSOS, BELLEZA, OTRO
    val precioNormal: Double,
    val precioPorMayor: Double,
    val fotoUri: String? = null,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val activo: Boolean = true
)
