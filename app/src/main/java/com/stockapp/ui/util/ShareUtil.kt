package com.stockapp.ui.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun compartirProducto(
    context: Context,
    nombre: String,
    precioNormal: Double,
    precioPorMayor: Double,
    fotoUri: String?
) {
    val texto = buildString {
        append("🛍️ *$nombre*\n")
        append("💰 Precio: ${precioNormal.toDollarString()}\n")
        if (precioPorMayor < precioNormal) {
            append("📦 Por mayor: ${precioPorMayor.toDollarString()}")
        }
    }

    val intent = if (!fotoUri.isNullOrBlank()) {
        val uri = Uri.parse(fotoUri)
        Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TEXT, texto)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    } else {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, texto)
        }
    }

    context.startActivity(Intent.createChooser(intent, "Compartir producto"))
}
