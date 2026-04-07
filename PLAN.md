# StockApp — Plan de Desarrollo

## Descripcion
Aplicacion Android nativa para gestion de un emprendimiento de moda y belleza.
Permite controlar inventario con variantes, registrar ventas con multiples tipos de precio,
gestionar clientes y sus deudas, y llevar control de proveedores y compras.
App de un solo dispositivo, sin internet requerido, con exportacion a Excel y
capacidad de compartir productos directamente a redes sociales y mensajeria.

---

## Stack Tecnico

| Tecnologia | Uso |
|---|---|
| Kotlin | Lenguaje principal |
| Jetpack Compose | UI declarativa nativa |
| Room | Base de datos local (SQLite) |
| Hilt | Inyeccion de dependencias |
| Navigation Compose | Navegacion entre pantallas |
| Coil | Carga y cache de imagenes |
| Apache POI | Exportacion a Excel (.xlsx) |
| FileProvider | Acceso a camara y galeria |
| Android ShareSheet | Compartir productos por WhatsApp/redes |

---

## Arquitectura

MVVM — ViewModel + StateFlow + Jetpack Compose

```
com.stockapp/
├── data/
│   ├── local/
│   │   ├── entities/
│   │   ├── dao/
│   │   └── StockDatabase.kt
│   └── repository/
├── domain/
│   ├── model/
│   ├── repository/   (interfaces)
│   └── usecase/
├── ui/
│   ├── screens/
│   │   ├── dashboard/
│   │   ├── inventario/
│   │   ├── ventas/
│   │   ├── clientes/
│   │   ├── proveedores/
│   │   └── reportes/
│   ├── components/   (componentes reutilizables)
│   ├── navigation/
│   └── theme/
└── di/               (Hilt modules)
```

---

## Modelo de Datos

### Producto
```
id: Long (PK)
nombre: String
descripcion: String?
categoria: String          -- ROPA | ZAPATOS | BOLSOS | BELLEZA | OTRO
precioNormal: Double
precioPorMayor: Double
fotoUri: String?           -- URI local del dispositivo
fechaCreacion: Long
activo: Boolean
```

### Variante (de Producto)
```
id: Long (PK)
productoId: Long (FK -> Producto)
atributo: String           -- ej: "Talla", "Color"
valor: String              -- ej: "36", "Rojo"
stock: Int
activo: Boolean
```

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
clienteId: Long?           -- nullable: venta sin cliente registrado
fecha: Long
subtotal: Double
descuento: Double          -- diferencia respecto al precio normal
total: Double
estadoPago: Enum           -- PAGADO | FIADO | PARCIAL
notas: String?
```

### VentaItem
```
id: Long (PK)
ventaId: Long (FK -> Venta)
productoId: Long (FK -> Producto)
varianteId: Long?          -- nullable si el producto no tiene variantes
cantidad: Int
tipoPrecio: Enum           -- NORMAL | MAYOR | NEGOCIADO
precioUnitario: Double     -- precio aplicado (puede ser menor al normal)
subtotal: Double
```

### Abono (Cuentas por Cobrar)
```
id: Long (PK)
ventaId: Long (FK -> Venta)
fecha: Long
monto: Double
notas: String?
```

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
proveedorId: Long (FK -> Proveedor)
fecha: Long
descripcion: String
total: Double
estadoPago: Enum           -- PAGADO | PENDIENTE | PARCIAL
```

### PagoProveedor
```
id: Long (PK)
compraProveedorId: Long (FK -> CompraProveedor)
fecha: Long
monto: Double
notas: String?
```

---

## Modulos y Pantallas

### Navegacion Principal (Bottom Navigation Bar)
```
[ Dashboard ]  [ Inventario ]  [ Nueva Venta ]  [ Clientes ]  [ Mas ]
```

---

### 1. Dashboard (Inicio)
**Objetivo:** Vista rapida del estado del negocio

**Secciones:**
- Resumen del DIA: ventas realizadas, ingresos cobrados
- Resumen del MES: total ventas, total cobrado vs. total fiado
- Cuentas por Cobrar: monto total pendiente de clientes
- Cuentas por Pagar: monto total pendiente a proveedores
- Alertas: tarjetas de productos con stock bajo (< umbral configurable)

---

### 2. Inventario

#### 2.1 Lista de Productos
- Grid o lista con foto, nombre, categoria, precio normal, stock total
- Chip filters por categoria (Todos / Ropa / Zapatos / Bolsos / Belleza / Otro)
- Barra de busqueda por nombre
- FAB "+" para agregar producto
- Cada tarjeta: boton compartir (ShareSheet con foto + nombre + precio)
- Indicador visual de stock bajo o sin stock

#### 2.2 Detalle de Producto
- Foto grande
- Nombre, descripcion, categoria
- Precio normal y precio por mayor
- Lista de variantes con stock por variante
- Botones: Editar, Compartir, Eliminar
- Historial de ventas del producto (opcional Fase 8)

#### 2.3 Agregar / Editar Producto
- Selector de foto: camara o galeria del dispositivo
- Campos: nombre*, descripcion, categoria*
- Precio normal* y precio por mayor*
- Seccion de variantes:
  - Boton "Agregar variante"
  - Por variante: atributo (ej. Talla), valor (ej. 38), stock inicial
  - Swipe para eliminar variante

#### 2.4 Compartir Producto
- Se genera una imagen o texto con: foto del producto, nombre, precio normal
- Se lanza el ShareSheet de Android (WhatsApp, Instagram, Telegram, etc.)

---

### 3. Ventas

#### 3.1 Lista de Ventas
- Lista con: fecha, nombre del cliente (o "Cliente general"), total, estado (PAGADO / FIADO / PARCIAL)
- Filtros: por estado de pago, por rango de fechas
- Busqueda por cliente
- FAB "+" para nueva venta
- Al tocar: ir al Detalle de Venta

#### 3.2 Nueva Venta (flujo de 3 pasos)

**Paso 1 — Cliente:**
- Seleccionar cliente existente (busqueda) o continuar sin cliente

**Paso 2 — Productos:**
- Buscar producto por nombre
- Al seleccionar: elegir variante (si aplica), cantidad, tipo de precio:
  - Normal ($XX)
  - Por mayor ($XX)
  - Negociado (campo libre — puede ser menor al normal)
- Lista de items agregados con posibilidad de editar/quitar
- Subtotal en tiempo real

**Paso 3 — Pago:**
- Total calculado
- Estado de pago:
  - PAGADO: confirmar
  - FIADO: queda como deuda total
  - PARCIAL: ingresar monto abonado, el resto queda como deuda
- Notas opcionales
- Boton "Confirmar Venta" — actualiza stock automaticamente

#### 3.3 Detalle de Venta
- Info completa: cliente, fecha, items con precios, total
- Estado de pago con indicador de color
- Saldo pendiente (si FIADO o PARCIAL)
- Historial de abonos realizados
- Boton "Registrar abono" (si deuda pendiente)
- Boton "Marcar como pagado" (si resta saldo)

---

### 4. Clientes

#### 4.1 Lista de Clientes
- Lista con: nombre, telefono, saldo deudor total
- Indicador visual si tiene deudas pendientes
- Busqueda por nombre
- FAB "+" para agregar cliente

#### 4.2 Detalle de Cliente
- Nombre, telefono, notas
- Saldo total adeudado (destacado)
- Lista de compras activas con deuda (FIADO / PARCIAL) con saldo de cada una
- Historial completo de compras (todas)
- Botones: Editar, Eliminar (solo si no tiene deudas pendientes)

#### 4.3 Agregar / Editar Cliente
- Nombre*, telefono, notas
- Validacion: nombre requerido

---

### 5. Mas (menu secundario)

Acceso a:
- Proveedores
- Reportes / Exportar
- Ajustes

---

### 6. Proveedores

#### 6.1 Lista de Proveedores
- Lista con: nombre, telefono, deuda pendiente total
- FAB "+" para agregar proveedor

#### 6.2 Detalle de Proveedor
- Info del proveedor
- Deuda total pendiente
- Lista de compras con estado
- Historial de pagos realizados
- Boton "Registrar compra"

#### 6.3 Agregar / Editar Proveedor
- Nombre*, telefono, notas

#### 6.4 Nueva Compra a Proveedor
- Proveedor (pre-seleccionado si viene del detalle)
- Fecha, descripcion*, total*
- Estado de pago: PAGADO / PENDIENTE / PARCIAL
- Si PARCIAL: monto pagado inicial

#### 6.5 Registrar Pago a Proveedor
- Monto*, fecha, notas
- Actualiza automaticamente el estado de la compra

---

### 7. Reportes / Exportar

- **Exportar Inventario:** todos los productos con variantes, stock y precios (.xlsx)
- **Exportar Ventas:** con filtro de rango de fechas (.xlsx)
- **Exportar Cuentas por Cobrar:** clientes con deuda pendiente (.xlsx)
- **Exportar Cuentas por Pagar:** proveedores con deuda pendiente (.xlsx)
- Archivo se guarda en Descargas o se comparte directamente

---

### 8. Ajustes

- **Categorias:** agregar / editar / eliminar categorias de productos
- **Umbral de stock bajo:** numero a partir del cual se muestra alerta (default: 3)
- **Sobre la app:** version

---

## Fases de Desarrollo

### Fase 1 — Setup del Proyecto
- [ ] Crear proyecto en Android Studio (min SDK 26, target SDK 35)
- [ ] Configurar `libs.versions.toml` con todas las dependencias
- [ ] Estructura de paquetes: data / domain / ui / di
- [ ] Room: definir todas las entidades, DAOs y StockDatabase
- [ ] Hilt: configurar modulos
- [ ] Navigation Compose: grafo de navegacion basico
- [ ] Tema visual: colores, tipografia, dark/light mode

### Fase 2 — Modulo Inventario
- [ ] CRUD completo de Productos
- [ ] CRUD de Variantes (dentro del producto)
- [ ] Fotos desde camara (FileProvider + ActivityResultContracts)
- [ ] Fotos desde galeria (PickVisualMedia contract)
- [ ] Lista con busqueda y filtros por categoria
- [ ] Compartir producto (Intent.ACTION_SEND con imagen)

### Fase 3 — Modulo Ventas y Clientes
- [ ] CRUD de Clientes
- [ ] Flujo completo de Nueva Venta (3 pasos)
- [ ] Seleccion de tipo de precio (normal / mayor / negociado)
- [ ] Actualizacion automatica de stock al confirmar venta
- [ ] Lista de ventas con filtros
- [ ] Detalle de venta

### Fase 4 — Cuentas por Cobrar
- [ ] Ventas con estado FIADO y PARCIAL
- [ ] Registro de abonos
- [ ] Actualizacion automatica de estado segun abonos
- [ ] Vista de deuda total por cliente en detalle de cliente

### Fase 5 — Proveedores y Cuentas por Pagar
- [ ] CRUD de Proveedores
- [ ] Registro de compras a proveedores
- [ ] Registro de pagos parciales a proveedores
- [ ] Vista de deuda por proveedor

### Fase 6 — Dashboard
- [ ] Calculos: ventas del dia, ventas del mes
- [ ] Total CxC pendiente
- [ ] Total CxP pendiente
- [ ] Alertas de stock bajo (con umbral configurable)

### Fase 7 — Reportes y Export Excel
- [ ] Integrar Apache POI
- [ ] Export inventario, ventas, CxC, CxP
- [ ] Guardar en Descargas o compartir via ShareSheet

### Fase 8 — Polish y UX Final
- [ ] Animaciones y transiciones
- [ ] Empty states (pantallas sin datos)
- [ ] Confirmaciones antes de eliminar
- [ ] Validaciones robustas en formularios
- [ ] Icono y nombre de la app
- [ ] Historial de ventas por producto

---

## Moneda
USD unicamente

## Dispositivos
Un solo dispositivo (sin sincronizacion en la nube)
