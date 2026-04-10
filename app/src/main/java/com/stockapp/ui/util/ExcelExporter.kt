package com.stockapp.ui.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.stockapp.data.local.entity.ProductoEntity
import com.stockapp.data.local.relation.ClienteConDeuda
import com.stockapp.data.local.relation.ProveedorConDeuda
import com.stockapp.data.local.relation.VentaConDetalle
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

object ExcelExporter {

    fun exportarInventario(
        context: Context,
        productos: List<ProductoEntity>
    ): Uri {
        val wb = XSSFWorkbook()
        val sheet = wb.createSheet("Inventario")

        val headerStyle = crearEstiloHeader(wb)
        val moneyStyle = crearEstiloDinero(wb)

        val header = sheet.createRow(0)
        listOf("Nombre", "Categoría", "Precio Normal", "Precio Mayor", "Stock").forEachIndexed { i, title ->
            header.createCell(i).apply { setCellValue(title); cellStyle = headerStyle }
        }

        var rowIdx = 1
        productos.forEach { item ->
            val row = sheet.createRow(rowIdx++)
            row.createCell(0).setCellValue(item.nombre)
            row.createCell(1).setCellValue(item.categoria)
            row.createCell(2).apply { setCellValue(item.precioNormal); cellStyle = moneyStyle }
            row.createCell(3).apply { setCellValue(item.precioPorMayor); cellStyle = moneyStyle }
            row.createCell(4).setCellValue(item.stock.toDouble())
        }

        autoSizeColumns(sheet, 5)
        return guardarYCompartir(context, wb, "inventario")
    }

    fun exportarVentas(
        context: Context,
        ventas: List<VentaConDetalle>,
        desde: Long,
        hasta: Long
    ): Uri {
        val wb = XSSFWorkbook()
        val sheet = wb.createSheet("Ventas")

        val headerStyle = crearEstiloHeader(wb)
        val moneyStyle = crearEstiloDinero(wb)

        val header = sheet.createRow(0)
        listOf("Fecha", "Cliente", "Total", "Abonado", "Saldo", "Estado", "Notas").forEachIndexed { i, title ->
            header.createCell(i).apply { setCellValue(title); cellStyle = headerStyle }
        }

        var rowIdx = 1
        ventas.forEach { venta ->
            val row = sheet.createRow(rowIdx++)
            row.createCell(0).setCellValue(venta.venta.fecha.toDateTimeString())
            row.createCell(1).setCellValue(venta.cliente?.nombre ?: "Sin cliente")
            row.createCell(2).apply { setCellValue(venta.venta.total); cellStyle = moneyStyle }
            row.createCell(3).apply { setCellValue(venta.totalAbonado); cellStyle = moneyStyle }
            row.createCell(4).apply { setCellValue(venta.saldoPendiente); cellStyle = moneyStyle }
            row.createCell(5).setCellValue(when (venta.venta.estadoPago) {
                "PAGADO" -> "Pagado"
                "FIADO"  -> "Fiado"
                else     -> "Parcial"
            })
            row.createCell(6).setCellValue(venta.venta.notas ?: "")
        }

        autoSizeColumns(sheet, 7)

        // Totales al final
        val totalRow = sheet.createRow(rowIdx + 1)
        totalRow.createCell(0).setCellValue("TOTAL")
        totalRow.createCell(2).apply {
            setCellValue(ventas.sumOf { it.venta.total }); cellStyle = moneyStyle
        }
        totalRow.createCell(3).apply {
            setCellValue(ventas.sumOf { it.totalAbonado }); cellStyle = moneyStyle
        }
        totalRow.createCell(4).apply {
            setCellValue(ventas.sumOf { it.saldoPendiente }); cellStyle = moneyStyle
        }

        return guardarYCompartir(context, wb, "ventas_${desde.toDateString()}_${hasta.toDateString()}")
    }

    fun exportarCxC(
        context: Context,
        clientes: List<ClienteConDeuda>
    ): Uri {
        val wb = XSSFWorkbook()
        val sheet = wb.createSheet("Cuentas por Cobrar")

        val headerStyle = crearEstiloHeader(wb)
        val moneyStyle = crearEstiloDinero(wb)

        val header = sheet.createRow(0)
        listOf("Cliente", "Teléfono", "Saldo Pendiente").forEachIndexed { i, title ->
            header.createCell(i).apply { setCellValue(title); cellStyle = headerStyle }
        }

        var rowIdx = 1
        clientes.filter { it.saldoPendiente > 0 }.forEach { item ->
            val row = sheet.createRow(rowIdx++)
            row.createCell(0).setCellValue(item.cliente.nombre)
            row.createCell(1).setCellValue(item.cliente.telefono ?: "")
            row.createCell(2).apply { setCellValue(item.saldoPendiente); cellStyle = moneyStyle }
        }

        autoSizeColumns(sheet, 3)

        val totalRow = sheet.createRow(rowIdx + 1)
        totalRow.createCell(0).setCellValue("TOTAL")
        totalRow.createCell(2).apply {
            setCellValue(clientes.sumOf { it.saldoPendiente }); cellStyle = moneyStyle
        }

        return guardarYCompartir(context, wb, "cxc")
    }

    fun exportarCxP(
        context: Context,
        proveedores: List<ProveedorConDeuda>
    ): Uri {
        val wb = XSSFWorkbook()
        val sheet = wb.createSheet("Cuentas por Pagar")

        val headerStyle = crearEstiloHeader(wb)
        val moneyStyle = crearEstiloDinero(wb)

        val header = sheet.createRow(0)
        listOf("Proveedor", "Teléfono", "Deuda Pendiente").forEachIndexed { i, title ->
            header.createCell(i).apply { setCellValue(title); cellStyle = headerStyle }
        }

        var rowIdx = 1
        proveedores.filter { it.deudaPendiente > 0 }.forEach { item ->
            val row = sheet.createRow(rowIdx++)
            row.createCell(0).setCellValue(item.proveedor.nombre)
            row.createCell(1).setCellValue(item.proveedor.telefono ?: "")
            row.createCell(2).apply { setCellValue(item.deudaPendiente); cellStyle = moneyStyle }
        }

        autoSizeColumns(sheet, 3)

        val totalRow = sheet.createRow(rowIdx + 1)
        totalRow.createCell(0).setCellValue("TOTAL")
        totalRow.createCell(2).apply {
            setCellValue(proveedores.sumOf { it.deudaPendiente }); cellStyle = moneyStyle
        }

        return guardarYCompartir(context, wb, "cxp")
    }

    private fun crearEstiloHeader(wb: XSSFWorkbook) = wb.createCellStyle().apply {
        fillForegroundColor = IndexedColors.DARK_BLUE.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
        alignment = HorizontalAlignment.CENTER
        setFont(wb.createFont().apply {
            bold = true
            color = IndexedColors.WHITE.index
        })
        setBorderBottom(BorderStyle.THIN)
        setBorderTop(BorderStyle.THIN)
        setBorderLeft(BorderStyle.THIN)
        setBorderRight(BorderStyle.THIN)
    }

    private fun crearEstiloDinero(wb: XSSFWorkbook) = wb.createCellStyle().apply {
        dataFormat = wb.createDataFormat().getFormat("\"$\"#,##0.00")
    }

    private fun autoSizeColumns(sheet: org.apache.poi.ss.usermodel.Sheet, count: Int) {
        repeat(count) { sheet.autoSizeColumn(it) }
    }

    private fun guardarYCompartir(context: Context, wb: XSSFWorkbook, nombre: String): Uri {
        val dir = File(context.cacheDir, "reports").also { it.mkdirs() }
        val file = File(dir, "stockapp_${nombre}.xlsx")
        FileOutputStream(file).use { wb.write(it) }
        wb.close()
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    fun compartirExcel(context: Context, uri: Uri, nombre: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "StockApp — $nombre")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Exportar $nombre"))
    }
}
