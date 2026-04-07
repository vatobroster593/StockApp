package com.stockapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.stockapp.data.local.entity.AbonoEntity
import com.stockapp.data.local.entity.ClienteEntity
import com.stockapp.data.local.entity.VentaEntity
import com.stockapp.data.local.entity.VentaItemEntity

data class VentaConDetalle(
    @Embedded val venta: VentaEntity,
    @Relation(
        parentColumn = "clienteId",
        entityColumn = "id"
    )
    val cliente: ClienteEntity?,
    @Relation(
        parentColumn = "id",
        entityColumn = "ventaId"
    )
    val items: List<VentaItemEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "ventaId"
    )
    val abonos: List<AbonoEntity>
) {
    val totalAbonado: Double get() = abonos.sumOf { it.monto }
    val saldoPendiente: Double get() = venta.total - totalAbonado
}
