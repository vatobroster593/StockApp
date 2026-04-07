package com.stockapp.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.stockapp.data.local.dao.ClienteDao;
import com.stockapp.data.local.dao.ClienteDao_Impl;
import com.stockapp.data.local.dao.ProductoDao;
import com.stockapp.data.local.dao.ProductoDao_Impl;
import com.stockapp.data.local.dao.ProveedorDao;
import com.stockapp.data.local.dao.ProveedorDao_Impl;
import com.stockapp.data.local.dao.VentaDao;
import com.stockapp.data.local.dao.VentaDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StockDatabase_Impl extends StockDatabase {
  private volatile ProductoDao _productoDao;

  private volatile ClienteDao _clienteDao;

  private volatile VentaDao _ventaDao;

  private volatile ProveedorDao _proveedorDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `productos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT NOT NULL, `descripcion` TEXT, `categoria` TEXT NOT NULL, `precioNormal` REAL NOT NULL, `precioPorMayor` REAL NOT NULL, `fotoUri` TEXT, `fechaCreacion` INTEGER NOT NULL, `activo` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `variantes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `productoId` INTEGER NOT NULL, `atributo` TEXT NOT NULL, `valor` TEXT NOT NULL, `stock` INTEGER NOT NULL, `activo` INTEGER NOT NULL, FOREIGN KEY(`productoId`) REFERENCES `productos`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_variantes_productoId` ON `variantes` (`productoId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `clientes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT NOT NULL, `telefono` TEXT, `notas` TEXT, `fechaCreacion` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `ventas` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `clienteId` INTEGER, `fecha` INTEGER NOT NULL, `subtotal` REAL NOT NULL, `descuento` REAL NOT NULL, `total` REAL NOT NULL, `estadoPago` TEXT NOT NULL, `notas` TEXT, FOREIGN KEY(`clienteId`) REFERENCES `clientes`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_ventas_clienteId` ON `ventas` (`clienteId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `venta_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `ventaId` INTEGER NOT NULL, `productoId` INTEGER NOT NULL, `varianteId` INTEGER, `cantidad` INTEGER NOT NULL, `tipoPrecio` TEXT NOT NULL, `precioUnitario` REAL NOT NULL, `subtotal` REAL NOT NULL, FOREIGN KEY(`ventaId`) REFERENCES `ventas`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`productoId`) REFERENCES `productos`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_venta_items_ventaId` ON `venta_items` (`ventaId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_venta_items_productoId` ON `venta_items` (`productoId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `abonos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `ventaId` INTEGER NOT NULL, `fecha` INTEGER NOT NULL, `monto` REAL NOT NULL, `notas` TEXT, FOREIGN KEY(`ventaId`) REFERENCES `ventas`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_abonos_ventaId` ON `abonos` (`ventaId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `proveedores` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT NOT NULL, `telefono` TEXT, `notas` TEXT, `fechaCreacion` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `compras_proveedor` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `proveedorId` INTEGER NOT NULL, `fecha` INTEGER NOT NULL, `descripcion` TEXT NOT NULL, `total` REAL NOT NULL, `estadoPago` TEXT NOT NULL, FOREIGN KEY(`proveedorId`) REFERENCES `proveedores`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_compras_proveedor_proveedorId` ON `compras_proveedor` (`proveedorId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `pagos_proveedor` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `compraProveedorId` INTEGER NOT NULL, `fecha` INTEGER NOT NULL, `monto` REAL NOT NULL, `notas` TEXT, FOREIGN KEY(`compraProveedorId`) REFERENCES `compras_proveedor`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_pagos_proveedor_compraProveedorId` ON `pagos_proveedor` (`compraProveedorId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '0a58cdb8a70d1905f0fd6efc0dd47e9b')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `productos`");
        db.execSQL("DROP TABLE IF EXISTS `variantes`");
        db.execSQL("DROP TABLE IF EXISTS `clientes`");
        db.execSQL("DROP TABLE IF EXISTS `ventas`");
        db.execSQL("DROP TABLE IF EXISTS `venta_items`");
        db.execSQL("DROP TABLE IF EXISTS `abonos`");
        db.execSQL("DROP TABLE IF EXISTS `proveedores`");
        db.execSQL("DROP TABLE IF EXISTS `compras_proveedor`");
        db.execSQL("DROP TABLE IF EXISTS `pagos_proveedor`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsProductos = new HashMap<String, TableInfo.Column>(9);
        _columnsProductos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("descripcion", new TableInfo.Column("descripcion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("categoria", new TableInfo.Column("categoria", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("precioNormal", new TableInfo.Column("precioNormal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("precioPorMayor", new TableInfo.Column("precioPorMayor", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("fotoUri", new TableInfo.Column("fotoUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("fechaCreacion", new TableInfo.Column("fechaCreacion", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("activo", new TableInfo.Column("activo", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProductos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProductos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProductos = new TableInfo("productos", _columnsProductos, _foreignKeysProductos, _indicesProductos);
        final TableInfo _existingProductos = TableInfo.read(db, "productos");
        if (!_infoProductos.equals(_existingProductos)) {
          return new RoomOpenHelper.ValidationResult(false, "productos(com.stockapp.data.local.entity.ProductoEntity).\n"
                  + " Expected:\n" + _infoProductos + "\n"
                  + " Found:\n" + _existingProductos);
        }
        final HashMap<String, TableInfo.Column> _columnsVariantes = new HashMap<String, TableInfo.Column>(6);
        _columnsVariantes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVariantes.put("productoId", new TableInfo.Column("productoId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVariantes.put("atributo", new TableInfo.Column("atributo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVariantes.put("valor", new TableInfo.Column("valor", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVariantes.put("stock", new TableInfo.Column("stock", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVariantes.put("activo", new TableInfo.Column("activo", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVariantes = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysVariantes.add(new TableInfo.ForeignKey("productos", "CASCADE", "NO ACTION", Arrays.asList("productoId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesVariantes = new HashSet<TableInfo.Index>(1);
        _indicesVariantes.add(new TableInfo.Index("index_variantes_productoId", false, Arrays.asList("productoId"), Arrays.asList("ASC")));
        final TableInfo _infoVariantes = new TableInfo("variantes", _columnsVariantes, _foreignKeysVariantes, _indicesVariantes);
        final TableInfo _existingVariantes = TableInfo.read(db, "variantes");
        if (!_infoVariantes.equals(_existingVariantes)) {
          return new RoomOpenHelper.ValidationResult(false, "variantes(com.stockapp.data.local.entity.VarianteEntity).\n"
                  + " Expected:\n" + _infoVariantes + "\n"
                  + " Found:\n" + _existingVariantes);
        }
        final HashMap<String, TableInfo.Column> _columnsClientes = new HashMap<String, TableInfo.Column>(5);
        _columnsClientes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClientes.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClientes.put("telefono", new TableInfo.Column("telefono", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClientes.put("notas", new TableInfo.Column("notas", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClientes.put("fechaCreacion", new TableInfo.Column("fechaCreacion", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysClientes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesClientes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoClientes = new TableInfo("clientes", _columnsClientes, _foreignKeysClientes, _indicesClientes);
        final TableInfo _existingClientes = TableInfo.read(db, "clientes");
        if (!_infoClientes.equals(_existingClientes)) {
          return new RoomOpenHelper.ValidationResult(false, "clientes(com.stockapp.data.local.entity.ClienteEntity).\n"
                  + " Expected:\n" + _infoClientes + "\n"
                  + " Found:\n" + _existingClientes);
        }
        final HashMap<String, TableInfo.Column> _columnsVentas = new HashMap<String, TableInfo.Column>(8);
        _columnsVentas.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVentas.put("clienteId", new TableInfo.Column("clienteId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVentas.put("fecha", new TableInfo.Column("fecha", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVentas.put("subtotal", new TableInfo.Column("subtotal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVentas.put("descuento", new TableInfo.Column("descuento", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVentas.put("total", new TableInfo.Column("total", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVentas.put("estadoPago", new TableInfo.Column("estadoPago", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVentas.put("notas", new TableInfo.Column("notas", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVentas = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysVentas.add(new TableInfo.ForeignKey("clientes", "SET NULL", "NO ACTION", Arrays.asList("clienteId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesVentas = new HashSet<TableInfo.Index>(1);
        _indicesVentas.add(new TableInfo.Index("index_ventas_clienteId", false, Arrays.asList("clienteId"), Arrays.asList("ASC")));
        final TableInfo _infoVentas = new TableInfo("ventas", _columnsVentas, _foreignKeysVentas, _indicesVentas);
        final TableInfo _existingVentas = TableInfo.read(db, "ventas");
        if (!_infoVentas.equals(_existingVentas)) {
          return new RoomOpenHelper.ValidationResult(false, "ventas(com.stockapp.data.local.entity.VentaEntity).\n"
                  + " Expected:\n" + _infoVentas + "\n"
                  + " Found:\n" + _existingVentas);
        }
        final HashMap<String, TableInfo.Column> _columnsVentaItems = new HashMap<String, TableInfo.Column>(8);
        _columnsVentaItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVentaItems.put("ventaId", new TableInfo.Column("ventaId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVentaItems.put("productoId", new TableInfo.Column("productoId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVentaItems.put("varianteId", new TableInfo.Column("varianteId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVentaItems.put("cantidad", new TableInfo.Column("cantidad", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVentaItems.put("tipoPrecio", new TableInfo.Column("tipoPrecio", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVentaItems.put("precioUnitario", new TableInfo.Column("precioUnitario", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVentaItems.put("subtotal", new TableInfo.Column("subtotal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVentaItems = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysVentaItems.add(new TableInfo.ForeignKey("ventas", "CASCADE", "NO ACTION", Arrays.asList("ventaId"), Arrays.asList("id")));
        _foreignKeysVentaItems.add(new TableInfo.ForeignKey("productos", "RESTRICT", "NO ACTION", Arrays.asList("productoId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesVentaItems = new HashSet<TableInfo.Index>(2);
        _indicesVentaItems.add(new TableInfo.Index("index_venta_items_ventaId", false, Arrays.asList("ventaId"), Arrays.asList("ASC")));
        _indicesVentaItems.add(new TableInfo.Index("index_venta_items_productoId", false, Arrays.asList("productoId"), Arrays.asList("ASC")));
        final TableInfo _infoVentaItems = new TableInfo("venta_items", _columnsVentaItems, _foreignKeysVentaItems, _indicesVentaItems);
        final TableInfo _existingVentaItems = TableInfo.read(db, "venta_items");
        if (!_infoVentaItems.equals(_existingVentaItems)) {
          return new RoomOpenHelper.ValidationResult(false, "venta_items(com.stockapp.data.local.entity.VentaItemEntity).\n"
                  + " Expected:\n" + _infoVentaItems + "\n"
                  + " Found:\n" + _existingVentaItems);
        }
        final HashMap<String, TableInfo.Column> _columnsAbonos = new HashMap<String, TableInfo.Column>(5);
        _columnsAbonos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAbonos.put("ventaId", new TableInfo.Column("ventaId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAbonos.put("fecha", new TableInfo.Column("fecha", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAbonos.put("monto", new TableInfo.Column("monto", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAbonos.put("notas", new TableInfo.Column("notas", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAbonos = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysAbonos.add(new TableInfo.ForeignKey("ventas", "CASCADE", "NO ACTION", Arrays.asList("ventaId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesAbonos = new HashSet<TableInfo.Index>(1);
        _indicesAbonos.add(new TableInfo.Index("index_abonos_ventaId", false, Arrays.asList("ventaId"), Arrays.asList("ASC")));
        final TableInfo _infoAbonos = new TableInfo("abonos", _columnsAbonos, _foreignKeysAbonos, _indicesAbonos);
        final TableInfo _existingAbonos = TableInfo.read(db, "abonos");
        if (!_infoAbonos.equals(_existingAbonos)) {
          return new RoomOpenHelper.ValidationResult(false, "abonos(com.stockapp.data.local.entity.AbonoEntity).\n"
                  + " Expected:\n" + _infoAbonos + "\n"
                  + " Found:\n" + _existingAbonos);
        }
        final HashMap<String, TableInfo.Column> _columnsProveedores = new HashMap<String, TableInfo.Column>(5);
        _columnsProveedores.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProveedores.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProveedores.put("telefono", new TableInfo.Column("telefono", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProveedores.put("notas", new TableInfo.Column("notas", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProveedores.put("fechaCreacion", new TableInfo.Column("fechaCreacion", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProveedores = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProveedores = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProveedores = new TableInfo("proveedores", _columnsProveedores, _foreignKeysProveedores, _indicesProveedores);
        final TableInfo _existingProveedores = TableInfo.read(db, "proveedores");
        if (!_infoProveedores.equals(_existingProveedores)) {
          return new RoomOpenHelper.ValidationResult(false, "proveedores(com.stockapp.data.local.entity.ProveedorEntity).\n"
                  + " Expected:\n" + _infoProveedores + "\n"
                  + " Found:\n" + _existingProveedores);
        }
        final HashMap<String, TableInfo.Column> _columnsComprasProveedor = new HashMap<String, TableInfo.Column>(6);
        _columnsComprasProveedor.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComprasProveedor.put("proveedorId", new TableInfo.Column("proveedorId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComprasProveedor.put("fecha", new TableInfo.Column("fecha", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComprasProveedor.put("descripcion", new TableInfo.Column("descripcion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComprasProveedor.put("total", new TableInfo.Column("total", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComprasProveedor.put("estadoPago", new TableInfo.Column("estadoPago", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysComprasProveedor = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysComprasProveedor.add(new TableInfo.ForeignKey("proveedores", "CASCADE", "NO ACTION", Arrays.asList("proveedorId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesComprasProveedor = new HashSet<TableInfo.Index>(1);
        _indicesComprasProveedor.add(new TableInfo.Index("index_compras_proveedor_proveedorId", false, Arrays.asList("proveedorId"), Arrays.asList("ASC")));
        final TableInfo _infoComprasProveedor = new TableInfo("compras_proveedor", _columnsComprasProveedor, _foreignKeysComprasProveedor, _indicesComprasProveedor);
        final TableInfo _existingComprasProveedor = TableInfo.read(db, "compras_proveedor");
        if (!_infoComprasProveedor.equals(_existingComprasProveedor)) {
          return new RoomOpenHelper.ValidationResult(false, "compras_proveedor(com.stockapp.data.local.entity.CompraProveedorEntity).\n"
                  + " Expected:\n" + _infoComprasProveedor + "\n"
                  + " Found:\n" + _existingComprasProveedor);
        }
        final HashMap<String, TableInfo.Column> _columnsPagosProveedor = new HashMap<String, TableInfo.Column>(5);
        _columnsPagosProveedor.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPagosProveedor.put("compraProveedorId", new TableInfo.Column("compraProveedorId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPagosProveedor.put("fecha", new TableInfo.Column("fecha", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPagosProveedor.put("monto", new TableInfo.Column("monto", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPagosProveedor.put("notas", new TableInfo.Column("notas", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPagosProveedor = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysPagosProveedor.add(new TableInfo.ForeignKey("compras_proveedor", "CASCADE", "NO ACTION", Arrays.asList("compraProveedorId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesPagosProveedor = new HashSet<TableInfo.Index>(1);
        _indicesPagosProveedor.add(new TableInfo.Index("index_pagos_proveedor_compraProveedorId", false, Arrays.asList("compraProveedorId"), Arrays.asList("ASC")));
        final TableInfo _infoPagosProveedor = new TableInfo("pagos_proveedor", _columnsPagosProveedor, _foreignKeysPagosProveedor, _indicesPagosProveedor);
        final TableInfo _existingPagosProveedor = TableInfo.read(db, "pagos_proveedor");
        if (!_infoPagosProveedor.equals(_existingPagosProveedor)) {
          return new RoomOpenHelper.ValidationResult(false, "pagos_proveedor(com.stockapp.data.local.entity.PagoProveedorEntity).\n"
                  + " Expected:\n" + _infoPagosProveedor + "\n"
                  + " Found:\n" + _existingPagosProveedor);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "0a58cdb8a70d1905f0fd6efc0dd47e9b", "e54d4af1d7d76ff459762e3a052c1682");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "productos","variantes","clientes","ventas","venta_items","abonos","proveedores","compras_proveedor","pagos_proveedor");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `productos`");
      _db.execSQL("DELETE FROM `variantes`");
      _db.execSQL("DELETE FROM `clientes`");
      _db.execSQL("DELETE FROM `ventas`");
      _db.execSQL("DELETE FROM `venta_items`");
      _db.execSQL("DELETE FROM `abonos`");
      _db.execSQL("DELETE FROM `proveedores`");
      _db.execSQL("DELETE FROM `compras_proveedor`");
      _db.execSQL("DELETE FROM `pagos_proveedor`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ProductoDao.class, ProductoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ClienteDao.class, ClienteDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(VentaDao.class, VentaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProveedorDao.class, ProveedorDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ProductoDao productoDao() {
    if (_productoDao != null) {
      return _productoDao;
    } else {
      synchronized(this) {
        if(_productoDao == null) {
          _productoDao = new ProductoDao_Impl(this);
        }
        return _productoDao;
      }
    }
  }

  @Override
  public ClienteDao clienteDao() {
    if (_clienteDao != null) {
      return _clienteDao;
    } else {
      synchronized(this) {
        if(_clienteDao == null) {
          _clienteDao = new ClienteDao_Impl(this);
        }
        return _clienteDao;
      }
    }
  }

  @Override
  public VentaDao ventaDao() {
    if (_ventaDao != null) {
      return _ventaDao;
    } else {
      synchronized(this) {
        if(_ventaDao == null) {
          _ventaDao = new VentaDao_Impl(this);
        }
        return _ventaDao;
      }
    }
  }

  @Override
  public ProveedorDao proveedorDao() {
    if (_proveedorDao != null) {
      return _proveedorDao;
    } else {
      synchronized(this) {
        if(_proveedorDao == null) {
          _proveedorDao = new ProveedorDao_Impl(this);
        }
        return _proveedorDao;
      }
    }
  }
}
