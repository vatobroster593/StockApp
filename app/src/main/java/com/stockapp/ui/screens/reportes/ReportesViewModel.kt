package com.stockapp.ui.screens.reportes

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockapp.domain.repository.ClienteRepository
import com.stockapp.domain.repository.ProductoRepository
import com.stockapp.domain.repository.ProveedorRepository
import com.stockapp.domain.repository.VentaRepository
import com.stockapp.ui.util.ExcelExporter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

enum class TipoReporte(val label: String) {
    INVENTARIO("Inventario"),
    VENTAS("Ventas por período"),
    CXC("Cuentas por cobrar"),
    CXP("Cuentas por pagar")
}

enum class RangoPeriodo(val label: String) {
    HOY("Hoy"),
    SEMANA("Esta semana"),
    MES("Este mes"),
    MES_PASADO("Mes pasado")
}

data class ReportesUiState(
    val tipoSeleccionado: TipoReporte = TipoReporte.INVENTARIO,
    val rangoSeleccionado: RangoPeriodo = RangoPeriodo.MES,
    val exportando: Boolean = false,
    val uriExportado: Uri? = null,
    val error: String? = null
)

@HiltViewModel
class ReportesViewModel @Inject constructor(
    private val ventaRepository: VentaRepository,
    private val productoRepository: ProductoRepository,
    private val clienteRepository: ClienteRepository,
    private val proveedorRepository: ProveedorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportesUiState())
    val uiState: StateFlow<ReportesUiState> = _uiState.asStateFlow()

    fun seleccionarTipo(tipo: TipoReporte) {
        _uiState.value = _uiState.value.copy(tipoSeleccionado = tipo, uriExportado = null)
    }

    fun seleccionarRango(rango: RangoPeriodo) {
        _uiState.value = _uiState.value.copy(rangoSeleccionado = rango, uriExportado = null)
    }

    fun exportar(context: Context) {
        _uiState.value = _uiState.value.copy(exportando = true, error = null, uriExportado = null)
        viewModelScope.launch {
            try {
                val uri = when (_uiState.value.tipoSeleccionado) {
                    TipoReporte.INVENTARIO -> {
                        val productos = productoRepository.getProductosConVariantes().first()
                        ExcelExporter.exportarInventario(context, productos)
                    }
                    TipoReporte.VENTAS -> {
                        val (desde, hasta) = rangoATimestamps(_uiState.value.rangoSeleccionado)
                        val ventas = ventaRepository.getVentasPorFechaSnapshot(desde, hasta)
                        ExcelExporter.exportarVentas(context, ventas, desde, hasta)
                    }
                    TipoReporte.CXC -> {
                        val clientes = clienteRepository.getClientesConDeuda().first()
                        ExcelExporter.exportarCxC(context, clientes)
                    }
                    TipoReporte.CXP -> {
                        val proveedores = proveedorRepository.getProveedoresConDeuda().first()
                        ExcelExporter.exportarCxP(context, proveedores)
                    }
                }
                _uiState.value = _uiState.value.copy(exportando = false, uriExportado = uri)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(exportando = false, error = e.message)
            }
        }
    }

    fun limpiarUri() {
        _uiState.value = _uiState.value.copy(uriExportado = null)
    }

    private fun rangoATimestamps(rango: RangoPeriodo): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        return when (rango) {
            RangoPeriodo.HOY -> {
                cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
                val inicio = cal.timeInMillis
                cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE, 59)
                cal.set(Calendar.SECOND, 59); cal.set(Calendar.MILLISECOND, 999)
                inicio to cal.timeInMillis
            }
            RangoPeriodo.SEMANA -> {
                cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
                cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
                val inicio = cal.timeInMillis
                cal.add(Calendar.DAY_OF_WEEK, 6)
                cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE, 59)
                cal.set(Calendar.SECOND, 59)
                inicio to cal.timeInMillis
            }
            RangoPeriodo.MES -> {
                cal.set(Calendar.DAY_OF_MONTH, 1)
                cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
                val inicio = cal.timeInMillis
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
                cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE, 59)
                cal.set(Calendar.SECOND, 59)
                inicio to cal.timeInMillis
            }
            RangoPeriodo.MES_PASADO -> {
                cal.add(Calendar.MONTH, -1)
                cal.set(Calendar.DAY_OF_MONTH, 1)
                cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
                val inicio = cal.timeInMillis
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
                cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE, 59)
                cal.set(Calendar.SECOND, 59)
                inicio to cal.timeInMillis
            }
        }
    }
}
