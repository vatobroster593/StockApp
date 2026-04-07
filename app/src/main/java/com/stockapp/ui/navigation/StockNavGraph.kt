package com.stockapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.stockapp.ui.screens.ajustes.AjustesScreen
import com.stockapp.ui.screens.clientes.ClientesScreen
import com.stockapp.ui.screens.dashboard.DashboardScreen
import com.stockapp.ui.screens.inventario.AgregarEditarProductoScreen
import com.stockapp.ui.screens.inventario.DetalleProductoScreen
import com.stockapp.ui.screens.inventario.InventarioScreen
import com.stockapp.ui.screens.mas.MasScreen
import com.stockapp.ui.screens.proveedores.AgregarProveedorScreen
import com.stockapp.ui.screens.proveedores.DetalleProveedorScreen
import com.stockapp.ui.screens.proveedores.NuevaCompraScreen
import com.stockapp.ui.screens.proveedores.ProveedoresScreen
import com.stockapp.ui.screens.reportes.ReportesScreen
import com.stockapp.ui.screens.ventas.DetalleVentaScreen
import com.stockapp.ui.screens.ventas.NuevaVentaScreen
import com.stockapp.ui.screens.ventas.VentasScreen

@Composable
fun StockNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        // Bottom nav principal
        composable(Screen.Dashboard.route)  { DashboardScreen(navController) }
        composable(Screen.Inventario.route) { InventarioScreen(navController) }
        composable(Screen.NuevaVenta.route) { NuevaVentaScreen(navController) }
        composable(Screen.Clientes.route)   { ClientesScreen(navController) }
        composable(Screen.Mas.route)        { MasScreen(navController) }

        // Inventario — Agregar producto (sin productoId)
        composable(Screen.AgregarProducto.route) {
            AgregarEditarProductoScreen(navController = navController, productoId = null)
        }

        // Inventario — Detalle de producto
        composable(
            route = Screen.DetalleProducto.route,
            arguments = listOf(navArgument("productoId") { type = NavType.LongType })
        ) {
            DetalleProductoScreen(navController)
        }

        // Inventario — Editar producto
        composable(
            route = Screen.EditarProducto.route,
            arguments = listOf(navArgument("productoId") { type = NavType.LongType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getLong("productoId")
            AgregarEditarProductoScreen(navController = navController, productoId = productoId)
        }

        // Ventas
        composable(Screen.Ventas.route) { VentasScreen(navController) }
        composable(
            route = Screen.DetalleVenta.route,
            arguments = listOf(navArgument("ventaId") { type = NavType.LongType })
        ) { DetalleVentaScreen(navController) }

        // Clientes
        composable(Screen.AgregarCliente.route) { /* Fase 3 */ }
        composable(
            route = Screen.DetalleCliente.route,
            arguments = listOf(navArgument("clienteId") { type = NavType.LongType })
        ) { /* Fase 3 */ }

        // Proveedores
        composable(Screen.Proveedores.route)      { ProveedoresScreen(navController) }
        composable(Screen.AgregarProveedor.route) { AgregarProveedorScreen(navController) }
        composable(
            route = Screen.DetalleProveedor.route,
            arguments = listOf(navArgument("proveedorId") { type = NavType.LongType })
        ) { DetalleProveedorScreen(navController) }
        composable(
            route = Screen.NuevaCompra.route,
            arguments = listOf(navArgument("proveedorId") { type = NavType.LongType })
        ) { NuevaCompraScreen(navController) }

        // Reportes y Ajustes
        composable(Screen.Reportes.route) { ReportesScreen(navController) }
        composable(Screen.Ajustes.route)  { AjustesScreen(navController) }
    }
}
