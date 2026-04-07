package com.stockapp.data.preferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("stockapp_prefs", Context.MODE_PRIVATE)

    var umbralStockBajo: Int
        get() = prefs.getInt(KEY_UMBRAL_STOCK, 3)
        set(value) = prefs.edit().putInt(KEY_UMBRAL_STOCK, value).apply()

    companion object {
        private const val KEY_UMBRAL_STOCK = "umbral_stock_bajo"
    }
}
