package com.stockapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {

    // Bottom Nav
    object Dashboard    : Screen("dashboard")
    object Inventario   : Screen("inventario")
    object NuevaVenta   : Screen("nueva_venta")
    object Clientes     : Screen("clientes")
    object Mas          : Screen("mas")

    // Inventario
    object DetalleProducto  : Screen("detalle_producto/{productoId}") {
        fun createRoute(productoId: Long) = "detalle_producto/$productoId"
    }
    object AgregarProducto  : Screen("agregar_producto")
    object EditarProducto   : Screen("editar_producto/{productoId}") {
        fun createRoute(productoId: Long) = "editar_producto/$productoId"
    }

    // Ventas
    object Ventas           : Screen("ventas")
    object DetalleVenta     : Screen("detalle_venta/{ventaId}") {
        fun createRoute(ventaId: Long) = "detalle_venta/$ventaId"
    }

    // Clientes
    object DetalleCliente   : Screen("detalle_cliente/{clienteId}") {
        fun createRoute(clienteId: Long) = "detalle_cliente/$clienteId"
    }
    object AgregarCliente   : Screen("agregar_cliente")
    object EditarCliente    : Screen("editar_cliente/{clienteId}") {
        fun createRoute(clienteId: Long) = "editar_cliente/$clienteId"
    }

    // Proveedores
    object Proveedores      : Screen("proveedores")
    object DetalleProveedor : Screen("detalle_proveedor/{proveedorId}") {
        fun createRoute(proveedorId: Long) = "detalle_proveedor/$proveedorId"
    }
    object AgregarProveedor : Screen("agregar_proveedor")
    object EditarProveedor  : Screen("editar_proveedor/{proveedorId}") {
        fun createRoute(proveedorId: Long) = "editar_proveedor/$proveedorId"
    }
    object NuevaCompra      : Screen("nueva_compra/{proveedorId}") {
        fun createRoute(proveedorId: Long) = "nueva_compra/$proveedorId"
    }

    // Reportes
    object Reportes         : Screen("reportes")

    // Ajustes
    object Ajustes          : Screen("ajustes")
}

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Dashboard,  "Inicio",      Icons.Filled.Dashboard),
    BottomNavItem(Screen.Inventario, "Inventario",  Icons.Filled.Inventory2),
    BottomNavItem(Screen.NuevaVenta, "Vender",      Icons.Filled.ShoppingCart),
    BottomNavItem(Screen.Clientes,   "Clientes",    Icons.Filled.People),
    BottomNavItem(Screen.Mas,        "Más",         Icons.Filled.MoreHoriz)
)
