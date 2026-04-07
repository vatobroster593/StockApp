package com.stockapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clientes")
data class ClienteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val telefono: String? = null,
    val notas: String? = null,
    val fechaCreacion: Long = System.currentTimeMillis()
)
