package com.stockapp.ui.screens.ajustes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.stockapp.data.preferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AjustesViewModel @Inject constructor(
    private val prefs: AppPreferences
) : ViewModel() {

    var umbralStock by mutableIntStateOf(prefs.umbralStockBajo)
        private set

    fun setUmbral(value: Int) {
        umbralStock = value
        prefs.umbralStockBajo = value
    }
}
