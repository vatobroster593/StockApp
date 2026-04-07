# StockApp — Plan de Desarrollo

## Descripcion
Aplicacion Android nativa para gestion de un emprendimiento de moda y belleza.
Permite controlar inventario con variantes, registrar ventas con multiples tipos de precio,
gestionar clientes y sus deudas, y llevar control de proveedores y compras.
App de un solo dispositivo, sin internet requerido, con exportacion a Excel y
capacidad de compartir productos directamente a redes sociales y mensajeria.

**Repositorio:** https://github.com/vatobroster593/StockApp
**Estado actual:** Fase 3 completada — ver CHANGELOG.md

---

## Stack Tecnico

| Tecnologia | Uso |
|---|---|
| Kotlin | Lenguaje principal |
| Jetpack Compose | UI declarativa nativa |
| Room 2.6.1 | Base de datos local (SQLite) |
| Hilt 2.54 | Inyeccion de dependencias |
| Navigation Compose 2.8.5 | Navegacion entre pantallas |
| Coil 2.7.0 | Carga y cache de imagenes |
| Apache POI 5.2.5 | Exportacion a Excel (.xlsx) |
| FileProvider | Acceso a camara y galeria |
| Android ShareSheet | Compartir productos por WhatsApp/redes |
| KSP 2.1.0-1.0.29 | Procesador de anotaciones (Room, Hilt) |

---

## Arquitectura

MVVM — ViewModel + StateFlow + Jetpack Compose

```
com.stockapp/
├── data/
│   ├── local/
│   │   ├── entity/         -- 9 entidades Room
│   │   ├── dao/            -- 4 DAOs con queries reactivos (Flow)
│   │   ├── relation/       -- data classes para queries con JOIN
│   │   └── StockDatabase.kt
│   └── repository/         -- implementaciones de repositorios
├── domain/
│   ├── model/              -- Enums (Categoria, EstadoPago, TipoPrecio, EstadoCompra)
│   └── repository/         -- interfaces de repositorios
├── ui/
│   ├── screens/
│   │   ├── dashboard/
│   │   ├── inventario/
│   │   ├── ventas/
│   │   ├── clientes/
│   │   ├── proveedores/
│   │   ├── reportes/
│   │   ├── ajustes/
│   │   └── mas/
│   ├── navigation/         -- Screen.kt (rutas), StockNavGraph.kt
│   ├── theme/              -- Color.kt, Theme.kt, Type.kt (paleta Purple/Rose M3)
│   └── util/               -- FormatUtil.kt, ShareUtil.kt
└── di/                     -- DatabaseModule.kt, RepositoryModule.kt
```

---

## Modelo de Datos

### Producto
```
id: Long (PK, autoGenerate)
nombre: String
descripcion: String?
categoria: String          -- ROPA | ZAPATOS | BOLSOS | BELLEZA | OTRO
precioNormal: Double
precioPorMayor: Double
fotoUri: String?           -- URI local del dispositivo (camara o galeria)
fechaCreacion: Long
activo: Boolean            -- soft delete
```

### Variante (de Producto)
```
id: Long (PK)
productoId: Long (FK -> Producto, CASCADE DELETE)
atributo: String           -- ej: "Talla", "Color"
valor: String              -- ej: "36", "Rojo"
stock: Int
activo: Boolean
```
*Relacion: ProductoConVariantes (Room @Relation). stockTotal = sum de variantes activas.*

### Cliente
```
id: Long (PK)
nombre: String
telefono: String?
notas: String?
fechaCreacion: Long
```

### Venta
```
id: Long (PK)
clienteId: Long?           -- nullable: venta sin cliente registrado (SET_NULL al eliminar cliente)
fecha: Long
subtotal: Double
descuento: Double          -- diferencia respecto al precio normal
total: Double
estadoPago: String         -- PAGADO | FIADO | PARCIAL
notas: String?
```

### VentaItem
```
id: Long (PK)
ventaId: Long (FK -> Venta, CASCADE DELETE)
productoId: Long (FK -> Producto, RESTRICT)
varianteId: Long?          -- null si el producto no tiene variantes
cantidad: Int
tipoPrecio: String         -- NORMAL | MAYOR | NEGOCIADO
precioUnitario: Double     -- precio aplicado (puede ser negociado/regateado)
subtotal: Double
```

### Abono (Cuentas por Cobrar)
```
id: Long (PK)
ventaId: Long (FK -> Venta, CASCADE DELETE)
fecha: Long
monto: Double
notas: String?
```
*Al registrar abono: se recalcula totalAbonado, si >= total → estadoPago = PAGADO automaticamente.*

### Proveedor
```
id: Long (PK)
nombre: String
telefono: String?
notas: String?
fechaCreacion: Long
```

### CompraProveedor
```
id: Long (PK)
proveedorId: Long (FK -> Proveedor, CASCADE DELETE)
fecha: Long
descripcion: String
total: Double
estadoPago: String         -- PAGADO | PENDIENTE | PARCIAL
```

### PagoProveedor
```
id: Long (PK)
compraProveedorId: Long (FK -> CompraProveedor, CASCADE DELETE)
fecha: Long
monto: Double
notas: String?
```

---

## Relaciones Room implementadas

| Clase | Descripcion |
|---|---|
| `ProductoConVariantes` | @Embedded Producto + @Relation List<Variante>. Computed: stockTotal |
| `VentaConDetalle` | @Embedded Venta + @Relation Cliente? + List<VentaItem> + List<Abono>. Computed: totalAbonado, saldoPendiente |
| `ClienteConDeuda` | @Embedded Cliente + @ColumnInfo saldoPendiente (SQL agregado) |
| `ProveedorConDeuda` | @Embedded Proveedor + @ColumnInfo deudaPendiente (SQL agregado) |

---

## Navegacion

**Bottom Navigation Bar:** Dashboard · Inventario · Nueva Venta · Clientes · Mas

**Rutas implementadas:**
```
dashboard
inventario
  └─ agregar_producto
  └─ detalle_producto/{productoId}
  └─ editar_producto/{productoId}
nueva_venta  (flujo 3 pasos en un solo Composable)
clientes
  └─ agregar_cliente
  └─ detalle_cliente/{clienteId}
  └─ editar_cliente/{clienteId}
mas
  └─ ventas  (historial)
  │   └─ detalle_venta/{ventaId}
  └─ proveedores
  │   └─ detalle_proveedor/{proveedorId}
  │   └─ agregar_proveedor
  │   └─ nueva_compra/{proveedorId}
  └─ reportes
  └─ ajustes
```

---

## Pantallas y estado de implementacion

### Bottom Nav
| Pantalla | Estado |
|---|---|
| DashboardScreen | Placeholder — Fase 6 |
| InventarioScreen | ✅ Completo |
| NuevaVentaScreen | ✅ Completo (3 pasos) |
| ClientesScreen | ✅ Completo |
| MasScreen | ✅ Completo |

### Inventario
| Pantalla | Estado |
|---|---|
| InventarioScreen | ✅ Grid 2 col, busqueda, chips, empty state, stock chips |
| AgregarEditarProductoScreen | ✅ Foto camara/galeria, precios, variantes CRUD |
| DetalleProductoScreen | ✅ Detalle, variantes, compartir, eliminar con confirmacion |

### Ventas
| Pantalla | Estado |
|---|---|
| NuevaVentaScreen | ✅ Paso 1 (cliente), Paso 2 (productos+dialog variante/precio), Paso 3 (pago) |
| VentasScreen | ✅ Lista con filtros PAGADO/FIADO/PARCIAL |
| DetalleVentaScreen | ✅ Items, total, saldo, abonos, FAB registrar abono |

### Clientes
| Pantalla | Estado |
|---|---|
| ClientesScreen | ✅ Lista con deuda en rojo, busqueda, avatar inicial |
| AgregarEditarClienteScreen | ✅ Form nombre/telefono/notas |
| DetalleClienteScreen | ✅ Info, tarjeta deuda, historial de compras |

### Proveedores
| Pantalla | Estado |
|---|---|
| ProveedoresScreen | Placeholder — Fase 5 |
| DetalleProveedorScreen | Placeholder — Fase 5 |
| AgregarProveedorScreen | Placeholder — Fase 5 |
| NuevaCompraScreen | Placeholder — Fase 5 |

### Reportes / Ajustes
| Pantalla | Estado |
|---|---|
| ReportesScreen | Placeholder — Fase 7 |
| AjustesScreen | Placeholder — Fase 8 |

---

## Fases de Desarrollo

### ✅ Fase 1 — Setup del Proyecto (COMPLETADA)
- [x] Configurar `libs.versions.toml` con todas las dependencias
- [x] Estructura de paquetes: data / domain / ui / di
- [x] Room: 9 entidades, 4 DAOs, 4 relaciones, StockDatabase
- [x] Hilt: DatabaseModule + RepositoryModule
- [x] Navigation Compose: Screen.kt + StockNavGraph.kt + BottomNav
- [x] Tema visual: paleta Purple/Rose Material 3, Typography

### ✅ Fase 2 — Modulo Inventario (COMPLETADA)
- [x] CRUD completo de Productos con soft delete
- [x] CRUD de Variantes (por producto, con stock individual)
- [x] Fotos desde camara (FileProvider + TakePicture contract + permiso runtime)
- [x] Fotos desde galeria (PickVisualMedia + persistableUriPermission)
- [x] Lista en grid 2 columnas con busqueda reactiva y chips de categoria
- [x] StockChip: verde/naranja/rojo segun nivel de stock
- [x] Compartir producto via ShareSheet (foto + nombre + precios)
- [x] ProductoRepository (interface + impl + Hilt binding)

### ✅ Fase 3 — Modulo Ventas y Clientes (COMPLETADA)
- [x] CRUD de Clientes con busqueda
- [x] Detalle de cliente con deuda total y historial
- [x] Flujo Nueva Venta en 3 pasos con StepIndicator
- [x] Seleccion de cliente o venta sin cliente
- [x] Dialog de producto: variante + cantidad + tipo de precio (Normal/Mayor/Negociado)
- [x] Precio negociado: campo libre para regateado (puede ser menor al normal)
- [x] Actualizacion atomica de stock al confirmar venta (Room withTransaction)
- [x] Estados de pago: PAGADO / FIADO / PARCIAL (con abono inicial)
- [x] Lista de ventas con filtros por estado
- [x] Detalle de venta: items, total, saldo pendiente, historial de abonos
- [x] Registrar abono con actualizacion automatica de estado (PARCIAL → PAGADO)
- [x] ClienteRepository + VentaRepository (interfaces + impl + Hilt)

### ✅ Fase 4 — Cuentas por Cobrar (COMPLETADA junto con Fase 3)
- [x] Ventas con estado FIADO y PARCIAL
- [x] Registro de abonos con dialog
- [x] Actualizacion automatica de estado segun suma de abonos
- [x] Vista de deuda total por cliente en DetalleClienteScreen
- [x] `ClienteConDeuda` query SQL con suma de pendientes

### 🔲 Fase 5 — Proveedores y Cuentas por Pagar (PENDIENTE)
- [ ] CRUD de Proveedores
- [ ] Registro de compras a proveedores (PAGADO / PENDIENTE / PARCIAL)
- [ ] Registro de pagos parciales a proveedores
- [ ] Actualizacion automatica de estado de compra al pagar
- [ ] Vista de deuda por proveedor con historial
- [ ] ProveedorRepository (interface + impl + Hilt)

### 🔲 Fase 6 — Dashboard (PENDIENTE)
- [ ] Resumen ventas del DIA: total $ y cantidad
- [ ] Resumen ventas del MES: total $
- [ ] Total CxC pendiente (deudas de clientes)
- [ ] Total CxP pendiente (deudas a proveedores)
- [ ] Cards de productos con stock bajo (< umbral configurable)
- [ ] DashboardViewModel con queries reactivos

### 🔲 Fase 7 — Reportes y Export Excel (PENDIENTE)
- [ ] Exportar inventario a .xlsx (productos + variantes + stock + precios)
- [ ] Exportar ventas a .xlsx con filtro de fechas
- [ ] Exportar CxC (clientes con deuda pendiente)
- [ ] Exportar CxP (proveedores con deuda pendiente)
- [ ] Guardar en carpeta Descargas o compartir directamente
- [ ] Apache POI ya incluido en build.gradle.kts

### 🔲 Fase 8 — Polish y UX Final (PENDIENTE)
- [ ] Animaciones y transiciones entre pantallas
- [ ] Empty states consistentes en todas las pantallas
- [ ] Confirmaciones antes de eliminar (donde falta)
- [ ] Validaciones robustas en todos los formularios
- [ ] Icono y nombre de la app (ic_launcher)
- [ ] Ajustes: categorias personalizadas + umbral stock bajo
- [ ] Historial de ventas por producto en DetalleProducto
- [ ] Nombre del producto en VentaItemRow (actualmente muestra #id)

---

## Moneda
USD unicamente

## Dispositivos
Un solo dispositivo (sin sincronizacion en la nube)

## Notas tecnicas
- Room `withTransaction` usado en `saveVenta` y `registrarAbono` para atomicidad
- Stock se decrementa automaticamente al confirmar venta
- Soft delete en productos (`activo = false`) para preservar historial de ventas
- FileProvider configurado en `res/xml/file_provider_paths.xml`
- `takePersistableUriPermission` usado al seleccionar imagen de galeria
- `PickVisualMedia` (Android Photo Picker) — sin necesidad de permiso READ_MEDIA en Android 13+
