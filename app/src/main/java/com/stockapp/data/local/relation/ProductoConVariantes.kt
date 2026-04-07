package com.stockapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.stockapp.data.local.entity.ProductoEntity
import com.stockapp.data.local.entity.VarianteEntity

data class ProductoConVariantes(
    @Embedded val producto: ProductoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "productoId"
    )
    val variantes: List<VarianteEntity>
) {
    val stockTotal: Int get() = variantes.filter { it.activo }.sumOf { it.stock }
}
