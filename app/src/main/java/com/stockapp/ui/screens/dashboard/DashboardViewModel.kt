package com.stockapp.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockapp.data.local.relation.ProductoConVariantes
import com.stockapp.data.preferences.AppPreferences
import com.stockapp.domain.repository.ProductoRepository
import com.stockapp.domain.repository.ProveedorRepository
import com.stockapp.domain.repository.VentaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import javax.inject.Inject

data class DashboardUiState(
    val ventasHoy: Double = 0.0,
    val countVentasHoy: Int = 0,
    val ventasMes: Double = 0.0,
    val countVentasMes: Int = 0,
    val totalCxC: Double = 0.0,
    val totalCxP: Double = 0.0,
    val productosStockBajo: List<ProductoConVariantes> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    ventaRepository: VentaRepository,
    proveedorRepository: ProveedorRepository,
    productoRepository: ProductoRepository,
    prefs: AppPreferences
) : ViewModel() {

    private fun inicioHoy(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun finHoy(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59); cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }

    private fun inicioMes(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun finMes(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59); cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }

    // Combinar flows de ventas (hoy + mes + CxC)
    private val ventasFlow = combine(
        ventaRepository.getTotalVentasPorPeriodo(inicioHoy(), finHoy()),
        ventaRepository.getCountVentasPorPeriodo(inicioHoy(), finHoy()),
        ventaRepository.getTotalVentasPorPeriodo(inicioMes(), finMes()),
        ventaRepository.getCountVentasPorPeriodo(inicioMes(), finMes()),
        ventaRepository.getTotalCxCPendiente()
    ) { ventasHoy, countHoy, ventasMes, countMes, cxc ->
        listOf(ventasHoy, countHoy.toDouble(), ventasMes, countMes.toDouble(), cxc)
    }

    val uiState: StateFlow<DashboardUiState> = combine(
        ventasFlow,
        proveedorRepository.getTotalCxPPendiente(),
        productoRepository.getProductosConStockBajo(prefs.umbralStockBajo)
    ) { ventas, cxp, stockBajo ->
        DashboardUiState(
            ventasHoy = ventas[0],
            countVentasHoy = ventas[1].toInt(),
            ventasMes = ventas[2],
            countVentasMes = ventas[3].toInt(),
            totalCxC = ventas[4],
            totalCxP = cxp,
            productosStockBajo = stockBajo,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardUiState())
}
