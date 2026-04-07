package com.stockapp.domain.model

enum class Categoria(val label: String) {
    ROPA("Ropa"),
    ZAPATOS("Zapatos"),
    BOLSOS("Bolsos"),
    BELLEZA("Belleza"),
    OTRO("Otro")
}

enum class EstadoPago(val label: String) {
    PAGADO("Pagado"),
    FIADO("Fiado"),
    PARCIAL("Parcial")
}

enum class TipoPrecio(val label: String) {
    NORMAL("Normal"),
    MAYOR("Por mayor"),
    NEGOCIADO("Negociado")
}

enum class EstadoCompra(val label: String) {
    PAGADO("Pagado"),
    PENDIENTE("Pendiente"),
    PARCIAL("Parcial")
}
