package com.stockapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.stockapp.data.local.entity.CompraProveedorEntity
import com.stockapp.data.local.entity.PagoProveedorEntity

data class CompraConPagos(
    @Embedded val compra: CompraProveedorEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "compraProveedorId"
    )
    val pagos: List<PagoProveedorEntity>
) {
    val totalPagado: Double get() = pagos.sumOf { it.monto }
    val saldoPendiente: Double get() = (compra.total - totalPagado).coerceAtLeast(0.0)
}
