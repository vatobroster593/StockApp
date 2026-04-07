# StockApp — Changelog

Todos los cambios notables de este proyecto se documentan aqui.

---

## [0.3.0] — Fase 3 + Fase 4: Ventas, Clientes y Cuentas por Cobrar

### Nuevas pantallas
- **ClientesScreen**: Lista de clientes con busqueda reactiva, avatar con inicial del nombre, saldo pendiente en rojo (CxC)
- **AgregarEditarClienteScreen**: Formulario con nombre, telefono y notas; valida campo nombre
- **DetalleClienteScreen**: Info del cliente, tarjeta de deuda total, historial de compras del cliente con navegacion a cada venta
- **NuevaVentaScreen**: Flujo de 3 pasos con `StepIndicator` visual
  - Paso 1: Seleccion de cliente (busqueda reactiva) o "venta sin cliente registrado"
  - Paso 2: Busqueda y seleccion de productos; `AgregarItemDialog` con variante, cantidad (+/-), tipo de precio (Normal / Mayor / Negociado con campo libre)
  - Paso 3: Resumen del carrito, seleccion de estado de pago (PAGADO / FIADO / PARCIAL), campo de abono inicial si PARCIAL, notas, boton confirmar
- **VentasScreen**: Historial de ventas con `FilterChip` por estado (PAGADO / FIADO / PARCIAL)
- **DetalleVentaScreen**: Items de la venta, total, saldo pendiente, historial de abonos, FAB "Registrar abono" (visible si no esta PAGADO)

### Nuevas entidades y relaciones Room
- `ClienteEntity`: nombre, telefono?, notas?, fechaCreacion
- `VentaEntity`: clienteId? (SET_NULL al eliminar cliente), fecha, subtotal, descuento, total, estadoPago, notas?
- `VentaItemEntity`: ventaId (CASCADE), productoId (RESTRICT), varianteId?, cantidad, tipoPrecio, precioUnitario, subtotal
- `AbonoEntity`: ventaId (CASCADE), fecha, monto, notas?
- `VentaConDetalle`: @Relation con Cliente?, List<VentaItem>, List<Abono>; computed `totalAbonado` y `saldoPendiente`
- `ClienteConDeuda`: query SQL con JOIN y SUM para calcular saldo pendiente por cliente

### Repositorios
- `ClienteRepository` + `ClienteRepositoryImpl`: getClientes, getClientesConDeuda, buscarClientes, getClienteById, saveCliente, deleteCliente
- `VentaRepository` + `VentaRepositoryImpl`: getVentas, getVentaConDetalle, getVentasByCliente, saveVenta, registrarAbono

### Logica de negocio
- `saveVenta`: usa `database.withTransaction` — inserta Venta + VentaItems + decrementa stock (por varianteId) + abono inicial (si PARCIAL) en una sola transaccion atomica
- `registrarAbono`: usa `database.withTransaction` — inserta abono, recalcula totalAbonado, actualiza estadoPago automaticamente (PARCIAL → PAGADO cuando totalAbonado >= total)
- Precio negociado: campo libre sin minimo, puede ser menor al precio normal (soporte para regateo)

### ViewModels
- `ClientesViewModel`: StateFlow<ClientesUiState> con busqueda reactiva via `combine`
- `NuevaVentaViewModel`: manejo de 3 pasos, busqueda de clientes via `flatMapLatest`, busqueda de productos via `combine`, lista de items como `mutableStateListOf<ItemVentaUi>`, merge automatico si se agrega el mismo producto+variante
- `VentasViewModel`: lista reactiva de ventas con filtro por estado
- `DetalleVentaViewModel`: obtiene VentaConDetalle, expone `abonoGuardado: SharedFlow` para cerrar dialog al guardar

### Correcciones
- Agregado `getVentaById(id: Long): VentaEntity?` a `VentaDao` (necesario para `registrarAbono`)
- `MasScreen` actualizado para incluir "Historial de ventas" con navegacion a `Screen.Ventas`

---

## [0.2.0] — Fase 2: Modulo Inventario

### Nuevas pantallas
- **InventarioScreen**: Grid de 2 columnas (`LazyVerticalGrid`), busqueda reactiva, chips de categoria (`FilterChip` en `LazyRow`), empty state, `StockChip` superpuesto en cada card (rojo=0, naranja=1-3, verde>3), boton compartir por producto
- **AgregarEditarProductoScreen**: Foto desde camara (`TakePicture` + `REQUEST_PERMISSION` + `FileProvider`) o galeria (`PickVisualMedia(ImageOnly)` + `takePersistableUriPermission`); `ExposedDropdownMenuBox` para categoria; precio normal y por mayor; CRUD de variantes inline (atributo, valor, stock por variante); valida campos obligatorios
- **DetalleProductoScreen**: Muestra foto (AsyncImage), info del producto, lista de variantes con stock individual, stock total; FAB compartir via `compartirProducto()`; boton eliminar con `AlertDialog` de confirmacion; soft delete (`activo = false`)

### Nuevas entidades y relaciones Room
- `ProductoEntity`: nombre, descripcion?, categoria, precioNormal, precioPorMayor, fotoUri?, fechaCreacion, activo (soft delete)
- `VarianteEntity`: productoId (FK → Producto, CASCADE DELETE), atributo, valor, stock, activo
- `ProductoConVariantes`: @Embedded ProductoEntity + @Relation List<VarianteEntity>; computed `stockTotal` = suma de variantes activas

### Repositorios
- `ProductoRepository` (interface) + `ProductoRepositoryImpl` (impl con @Binds Hilt):
  - `getProductosConVariantes()`: Flow<List<ProductoConVariantes>> — solo activos
  - `getProductoConVariantesById()`, `saveProducto()`, `deleteProducto()`, `decrementarStock()`
- DAOs: `ProductoDao` con queries para producto+variantes, `@Transaction @Query` para relaciones

### Utilidades
- `ShareUtil.kt`: `compartirProducto(context, nombre, precioNormal, precioPorMayor, fotoUri?)` — Intent ACTION_SEND con imagen (si tiene foto) o texto plano; usa Android ShareSheet
- `FormatUtil.kt`: extensiones `Double.toDollarString()` (→ "$1,234.56"), `Long.toDateString()`, `Long.toDateTimeString()`

### Configuracion
- `AndroidManifest.xml`: permiso CAMERA, READ_EXTERNAL_STORAGE (maxSdk 32), READ_MEDIA_IMAGES, FileProvider con authority `${applicationId}.fileprovider`
- `res/xml/file_provider_paths.xml`: rutas cache y external para fotos

### Correcciones
- Solucionado `innerPadding` no pasado al `NavHost` → contenido tapado por BottomNav; corregido en `StockAppContent.kt`

---

## [0.1.0] — Fase 1: Setup del Proyecto

### Estructura creada
- Proyecto Android (Kotlin + Jetpack Compose) en `C:\Proyectos\StockApp`
- Paquete base: `com.stockapp`
- Estructura de paquetes: `data/`, `domain/`, `ui/`, `di/`

### Dependencias configuradas (`libs.versions.toml`)
| Libreria | Version |
|---|---|
| AGP | 8.7.3 |
| Kotlin | 2.1.0 |
| Compose BOM | 2024.12.01 |
| Room | 2.6.1 |
| Hilt | 2.54 |
| Navigation Compose | 2.8.5 |
| Coil | 2.7.0 |
| Apache POI | 5.2.5 |
| KSP | 2.1.0-1.0.29 |

- `app/build.gradle.kts`: packaging excludes para `META-INF/DEPENDENCIES` y archivos POI conflictivos en Android

### Room — Base de datos
- **`StockDatabase`**: version 1, exportSchema true, 9 entidades, 4 DAOs abstractos
- **9 entidades**: ProductoEntity, VarianteEntity, ClienteEntity, VentaEntity, VentaItemEntity, AbonoEntity, ProveedorEntity, CompraProveedorEntity, PagoProveedorEntity
- **4 DAOs**: ProductoDao, ClienteDao, VentaDao, ProveedorDao

### Hilt — Inyeccion de dependencias
- `@HiltAndroidApp` en `StockApp : Application`
- `DatabaseModule`: provee `StockDatabase` (@Singleton) y DAOs individuales
- `RepositoryModule`: @Binds para interfaces de repositorios

### Dominio — Enums
- `Categoria`: ROPA, ZAPATOS, BOLSOS, BELLEZA, OTRO
- `EstadoPago`: PAGADO, FIADO, PARCIAL
- `TipoPrecio`: NORMAL (label: "Normal"), MAYOR (label: "Por mayor"), NEGOCIADO (label: "Negociado")
- `EstadoCompra`: PAGADO, PENDIENTE, PARCIAL

### Navegacion
- `Screen.kt`: sealed class con todas las rutas; `createRoute(id)` para rutas con argumentos
- `StockNavGraph.kt`: NavHost con todas las rutas registradas (composables placeholders donde aun no hay implementacion)
- `StockAppContent.kt`: Scaffold exterior con BottomNavigationBar; `showBottomBar` oculta nav en pantallas de detalle
- `bottomNavItems`: Inicio (Dashboard), Inventario, Vender (NuevaVenta), Clientes, Mas

### Tema visual
- Paleta Material 3: Purple (#6650A4) primario, Rose secundario
- `Color.kt`, `Theme.kt` (light/dark), `Type.kt` (tipografia M3)
- Soporte dark mode via `isSystemInDarkTheme()`

### Pantallas placeholder
- `DashboardScreen`, `ProveedoresScreen`, `DetalleProveedorScreen`, `AgregarProveedorScreen`, `NuevaCompraScreen`, `ReportesScreen`, `AjustesScreen` — placeholders con `Box { Text("...") }`

### GitHub
- Repositorio publico creado: https://github.com/vatobroster593/StockApp
- Commit inicial pusheado con todo el codigo de Fase 1

---

## Proximas versiones planeadas

| Version | Fase | Descripcion |
|---|---|---|
| 0.4.0 | Fase 5 | Proveedores: CRUD, compras, pagos parciales, CxP |
| 0.5.0 | Fase 6 | Dashboard: resumen dia/mes, alertas stock bajo |
| 0.6.0 | Fase 7 | Reportes: exportar inventario, ventas, CxC, CxP a Excel |
| 1.0.0 | Fase 8 | Polish: animaciones, empty states, icono, ajustes, nombre en VentaItem |
