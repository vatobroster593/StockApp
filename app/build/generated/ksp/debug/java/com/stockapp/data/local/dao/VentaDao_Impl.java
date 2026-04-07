package com.stockapp.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.stockapp.data.local.entity.AbonoEntity;
import com.stockapp.data.local.entity.ClienteEntity;
import com.stockapp.data.local.entity.VentaEntity;
import com.stockapp.data.local.entity.VentaItemEntity;
import com.stockapp.data.local.relation.VentaConDetalle;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class VentaDao_Impl implements VentaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<VentaEntity> __insertionAdapterOfVentaEntity;

  private final EntityInsertionAdapter<VentaItemEntity> __insertionAdapterOfVentaItemEntity;

  private final EntityInsertionAdapter<AbonoEntity> __insertionAdapterOfAbonoEntity;

  private final EntityDeletionOrUpdateAdapter<VentaEntity> __updateAdapterOfVentaEntity;

  public VentaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfVentaEntity = new EntityInsertionAdapter<VentaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `ventas` (`id`,`clienteId`,`fecha`,`subtotal`,`descuento`,`total`,`estadoPago`,`notas`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VentaEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getClienteId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getClienteId());
        }
        statement.bindLong(3, entity.getFecha());
        statement.bindDouble(4, entity.getSubtotal());
        statement.bindDouble(5, entity.getDescuento());
        statement.bindDouble(6, entity.getTotal());
        statement.bindString(7, entity.getEstadoPago());
        if (entity.getNotas() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getNotas());
        }
      }
    };
    this.__insertionAdapterOfVentaItemEntity = new EntityInsertionAdapter<VentaItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `venta_items` (`id`,`ventaId`,`productoId`,`productoNombre`,`varianteId`,`varianteLabel`,`cantidad`,`tipoPrecio`,`precioUnitario`,`subtotal`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VentaItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getVentaId());
        statement.bindLong(3, entity.getProductoId());
        statement.bindString(4, entity.getProductoNombre());
        if (entity.getVarianteId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getVarianteId());
        }
        if (entity.getVarianteLabel() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getVarianteLabel());
        }
        statement.bindLong(7, entity.getCantidad());
        statement.bindString(8, entity.getTipoPrecio());
        statement.bindDouble(9, entity.getPrecioUnitario());
        statement.bindDouble(10, entity.getSubtotal());
      }
    };
    this.__insertionAdapterOfAbonoEntity = new EntityInsertionAdapter<AbonoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `abonos` (`id`,`ventaId`,`fecha`,`monto`,`notas`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AbonoEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getVentaId());
        statement.bindLong(3, entity.getFecha());
        statement.bindDouble(4, entity.getMonto());
        if (entity.getNotas() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getNotas());
        }
      }
    };
    this.__updateAdapterOfVentaEntity = new EntityDeletionOrUpdateAdapter<VentaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `ventas` SET `id` = ?,`clienteId` = ?,`fecha` = ?,`subtotal` = ?,`descuento` = ?,`total` = ?,`estadoPago` = ?,`notas` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VentaEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getClienteId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getClienteId());
        }
        statement.bindLong(3, entity.getFecha());
        statement.bindDouble(4, entity.getSubtotal());
        statement.bindDouble(5, entity.getDescuento());
        statement.bindDouble(6, entity.getTotal());
        statement.bindString(7, entity.getEstadoPago());
        if (entity.getNotas() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getNotas());
        }
        statement.bindLong(9, entity.getId());
      }
    };
  }

  @Override
  public Object insertVenta(final VentaEntity venta, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfVentaEntity.insertAndReturnId(venta);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertVentaItems(final List<VentaItemEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfVentaItemEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAbono(final AbonoEntity abono, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAbonoEntity.insertAndReturnId(abono);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateVenta(final VentaEntity venta, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfVentaEntity.handle(venta);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<VentaConDetalle>> getVentasConDetalle() {
    final String _sql = "SELECT * FROM ventas ORDER BY fecha DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"clientes", "venta_items", "abonos",
        "ventas"}, new Callable<List<VentaConDetalle>>() {
      @Override
      @NonNull
      public List<VentaConDetalle> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfClienteId = CursorUtil.getColumnIndexOrThrow(_cursor, "clienteId");
            final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
            final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
            final int _cursorIndexOfDescuento = CursorUtil.getColumnIndexOrThrow(_cursor, "descuento");
            final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
            final int _cursorIndexOfEstadoPago = CursorUtil.getColumnIndexOrThrow(_cursor, "estadoPago");
            final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
            final LongSparseArray<ClienteEntity> _collectionCliente = new LongSparseArray<ClienteEntity>();
            final LongSparseArray<ArrayList<VentaItemEntity>> _collectionItems = new LongSparseArray<ArrayList<VentaItemEntity>>();
            final LongSparseArray<ArrayList<AbonoEntity>> _collectionAbonos = new LongSparseArray<ArrayList<AbonoEntity>>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfClienteId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfClienteId);
              }
              if (_tmpKey != null) {
                _collectionCliente.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionItems.containsKey(_tmpKey_1)) {
                _collectionItems.put(_tmpKey_1, new ArrayList<VentaItemEntity>());
              }
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionAbonos.containsKey(_tmpKey_2)) {
                _collectionAbonos.put(_tmpKey_2, new ArrayList<AbonoEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipclientesAscomStockappDataLocalEntityClienteEntity(_collectionCliente);
            __fetchRelationshipventaItemsAscomStockappDataLocalEntityVentaItemEntity(_collectionItems);
            __fetchRelationshipabonosAscomStockappDataLocalEntityAbonoEntity(_collectionAbonos);
            final List<VentaConDetalle> _result = new ArrayList<VentaConDetalle>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final VentaConDetalle _item;
              final VentaEntity _tmpVenta;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final Long _tmpClienteId;
              if (_cursor.isNull(_cursorIndexOfClienteId)) {
                _tmpClienteId = null;
              } else {
                _tmpClienteId = _cursor.getLong(_cursorIndexOfClienteId);
              }
              final long _tmpFecha;
              _tmpFecha = _cursor.getLong(_cursorIndexOfFecha);
              final double _tmpSubtotal;
              _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
              final double _tmpDescuento;
              _tmpDescuento = _cursor.getDouble(_cursorIndexOfDescuento);
              final double _tmpTotal;
              _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
              final String _tmpEstadoPago;
              _tmpEstadoPago = _cursor.getString(_cursorIndexOfEstadoPago);
              final String _tmpNotas;
              if (_cursor.isNull(_cursorIndexOfNotas)) {
                _tmpNotas = null;
              } else {
                _tmpNotas = _cursor.getString(_cursorIndexOfNotas);
              }
              _tmpVenta = new VentaEntity(_tmpId,_tmpClienteId,_tmpFecha,_tmpSubtotal,_tmpDescuento,_tmpTotal,_tmpEstadoPago,_tmpNotas);
              final ClienteEntity _tmpCliente;
              final Long _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfClienteId)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getLong(_cursorIndexOfClienteId);
              }
              if (_tmpKey_3 != null) {
                _tmpCliente = _collectionCliente.get(_tmpKey_3);
              } else {
                _tmpCliente = null;
              }
              final ArrayList<VentaItemEntity> _tmpItemsCollection;
              final long _tmpKey_4;
              _tmpKey_4 = _cursor.getLong(_cursorIndexOfId);
              _tmpItemsCollection = _collectionItems.get(_tmpKey_4);
              final ArrayList<AbonoEntity> _tmpAbonosCollection;
              final long _tmpKey_5;
              _tmpKey_5 = _cursor.getLong(_cursorIndexOfId);
              _tmpAbonosCollection = _collectionAbonos.get(_tmpKey_5);
              _item = new VentaConDetalle(_tmpVenta,_tmpCliente,_tmpItemsCollection,_tmpAbonosCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<VentaConDetalle>> getVentasPorEstado(final String estado) {
    final String _sql = "SELECT * FROM ventas WHERE estadoPago = ? ORDER BY fecha DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, estado);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"clientes", "venta_items", "abonos",
        "ventas"}, new Callable<List<VentaConDetalle>>() {
      @Override
      @NonNull
      public List<VentaConDetalle> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfClienteId = CursorUtil.getColumnIndexOrThrow(_cursor, "clienteId");
            final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
            final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
            final int _cursorIndexOfDescuento = CursorUtil.getColumnIndexOrThrow(_cursor, "descuento");
            final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
            final int _cursorIndexOfEstadoPago = CursorUtil.getColumnIndexOrThrow(_cursor, "estadoPago");
            final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
            final LongSparseArray<ClienteEntity> _collectionCliente = new LongSparseArray<ClienteEntity>();
            final LongSparseArray<ArrayList<VentaItemEntity>> _collectionItems = new LongSparseArray<ArrayList<VentaItemEntity>>();
            final LongSparseArray<ArrayList<AbonoEntity>> _collectionAbonos = new LongSparseArray<ArrayList<AbonoEntity>>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfClienteId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfClienteId);
              }
              if (_tmpKey != null) {
                _collectionCliente.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionItems.containsKey(_tmpKey_1)) {
                _collectionItems.put(_tmpKey_1, new ArrayList<VentaItemEntity>());
              }
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionAbonos.containsKey(_tmpKey_2)) {
                _collectionAbonos.put(_tmpKey_2, new ArrayList<AbonoEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipclientesAscomStockappDataLocalEntityClienteEntity(_collectionCliente);
            __fetchRelationshipventaItemsAscomStockappDataLocalEntityVentaItemEntity(_collectionItems);
            __fetchRelationshipabonosAscomStockappDataLocalEntityAbonoEntity(_collectionAbonos);
            final List<VentaConDetalle> _result = new ArrayList<VentaConDetalle>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final VentaConDetalle _item;
              final VentaEntity _tmpVenta;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final Long _tmpClienteId;
              if (_cursor.isNull(_cursorIndexOfClienteId)) {
                _tmpClienteId = null;
              } else {
                _tmpClienteId = _cursor.getLong(_cursorIndexOfClienteId);
              }
              final long _tmpFecha;
              _tmpFecha = _cursor.getLong(_cursorIndexOfFecha);
              final double _tmpSubtotal;
              _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
              final double _tmpDescuento;
              _tmpDescuento = _cursor.getDouble(_cursorIndexOfDescuento);
              final double _tmpTotal;
              _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
              final String _tmpEstadoPago;
              _tmpEstadoPago = _cursor.getString(_cursorIndexOfEstadoPago);
              final String _tmpNotas;
              if (_cursor.isNull(_cursorIndexOfNotas)) {
                _tmpNotas = null;
              } else {
                _tmpNotas = _cursor.getString(_cursorIndexOfNotas);
              }
              _tmpVenta = new VentaEntity(_tmpId,_tmpClienteId,_tmpFecha,_tmpSubtotal,_tmpDescuento,_tmpTotal,_tmpEstadoPago,_tmpNotas);
              final ClienteEntity _tmpCliente;
              final Long _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfClienteId)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getLong(_cursorIndexOfClienteId);
              }
              if (_tmpKey_3 != null) {
                _tmpCliente = _collectionCliente.get(_tmpKey_3);
              } else {
                _tmpCliente = null;
              }
              final ArrayList<VentaItemEntity> _tmpItemsCollection;
              final long _tmpKey_4;
              _tmpKey_4 = _cursor.getLong(_cursorIndexOfId);
              _tmpItemsCollection = _collectionItems.get(_tmpKey_4);
              final ArrayList<AbonoEntity> _tmpAbonosCollection;
              final long _tmpKey_5;
              _tmpKey_5 = _cursor.getLong(_cursorIndexOfId);
              _tmpAbonosCollection = _collectionAbonos.get(_tmpKey_5);
              _item = new VentaConDetalle(_tmpVenta,_tmpCliente,_tmpItemsCollection,_tmpAbonosCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<VentaConDetalle>> getVentasPorCliente(final long clienteId) {
    final String _sql = "SELECT * FROM ventas WHERE clienteId = ? ORDER BY fecha DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, clienteId);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"clientes", "venta_items", "abonos",
        "ventas"}, new Callable<List<VentaConDetalle>>() {
      @Override
      @NonNull
      public List<VentaConDetalle> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfClienteId = CursorUtil.getColumnIndexOrThrow(_cursor, "clienteId");
            final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
            final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
            final int _cursorIndexOfDescuento = CursorUtil.getColumnIndexOrThrow(_cursor, "descuento");
            final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
            final int _cursorIndexOfEstadoPago = CursorUtil.getColumnIndexOrThrow(_cursor, "estadoPago");
            final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
            final LongSparseArray<ClienteEntity> _collectionCliente = new LongSparseArray<ClienteEntity>();
            final LongSparseArray<ArrayList<VentaItemEntity>> _collectionItems = new LongSparseArray<ArrayList<VentaItemEntity>>();
            final LongSparseArray<ArrayList<AbonoEntity>> _collectionAbonos = new LongSparseArray<ArrayList<AbonoEntity>>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfClienteId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfClienteId);
              }
              if (_tmpKey != null) {
                _collectionCliente.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionItems.containsKey(_tmpKey_1)) {
                _collectionItems.put(_tmpKey_1, new ArrayList<VentaItemEntity>());
              }
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionAbonos.containsKey(_tmpKey_2)) {
                _collectionAbonos.put(_tmpKey_2, new ArrayList<AbonoEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipclientesAscomStockappDataLocalEntityClienteEntity(_collectionCliente);
            __fetchRelationshipventaItemsAscomStockappDataLocalEntityVentaItemEntity(_collectionItems);
            __fetchRelationshipabonosAscomStockappDataLocalEntityAbonoEntity(_collectionAbonos);
            final List<VentaConDetalle> _result = new ArrayList<VentaConDetalle>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final VentaConDetalle _item;
              final VentaEntity _tmpVenta;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final Long _tmpClienteId;
              if (_cursor.isNull(_cursorIndexOfClienteId)) {
                _tmpClienteId = null;
              } else {
                _tmpClienteId = _cursor.getLong(_cursorIndexOfClienteId);
              }
              final long _tmpFecha;
              _tmpFecha = _cursor.getLong(_cursorIndexOfFecha);
              final double _tmpSubtotal;
              _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
              final double _tmpDescuento;
              _tmpDescuento = _cursor.getDouble(_cursorIndexOfDescuento);
              final double _tmpTotal;
              _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
              final String _tmpEstadoPago;
              _tmpEstadoPago = _cursor.getString(_cursorIndexOfEstadoPago);
              final String _tmpNotas;
              if (_cursor.isNull(_cursorIndexOfNotas)) {
                _tmpNotas = null;
              } else {
                _tmpNotas = _cursor.getString(_cursorIndexOfNotas);
              }
              _tmpVenta = new VentaEntity(_tmpId,_tmpClienteId,_tmpFecha,_tmpSubtotal,_tmpDescuento,_tmpTotal,_tmpEstadoPago,_tmpNotas);
              final ClienteEntity _tmpCliente;
              final Long _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfClienteId)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getLong(_cursorIndexOfClienteId);
              }
              if (_tmpKey_3 != null) {
                _tmpCliente = _collectionCliente.get(_tmpKey_3);
              } else {
                _tmpCliente = null;
              }
              final ArrayList<VentaItemEntity> _tmpItemsCollection;
              final long _tmpKey_4;
              _tmpKey_4 = _cursor.getLong(_cursorIndexOfId);
              _tmpItemsCollection = _collectionItems.get(_tmpKey_4);
              final ArrayList<AbonoEntity> _tmpAbonosCollection;
              final long _tmpKey_5;
              _tmpKey_5 = _cursor.getLong(_cursorIndexOfId);
              _tmpAbonosCollection = _collectionAbonos.get(_tmpKey_5);
              _item = new VentaConDetalle(_tmpVenta,_tmpCliente,_tmpItemsCollection,_tmpAbonosCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<VentaConDetalle>> getVentasPorFecha(final long desde, final long hasta) {
    final String _sql = "SELECT * FROM ventas WHERE fecha BETWEEN ? AND ? ORDER BY fecha DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, desde);
    _argIndex = 2;
    _statement.bindLong(_argIndex, hasta);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"clientes", "venta_items", "abonos",
        "ventas"}, new Callable<List<VentaConDetalle>>() {
      @Override
      @NonNull
      public List<VentaConDetalle> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfClienteId = CursorUtil.getColumnIndexOrThrow(_cursor, "clienteId");
            final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
            final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
            final int _cursorIndexOfDescuento = CursorUtil.getColumnIndexOrThrow(_cursor, "descuento");
            final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
            final int _cursorIndexOfEstadoPago = CursorUtil.getColumnIndexOrThrow(_cursor, "estadoPago");
            final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
            final LongSparseArray<ClienteEntity> _collectionCliente = new LongSparseArray<ClienteEntity>();
            final LongSparseArray<ArrayList<VentaItemEntity>> _collectionItems = new LongSparseArray<ArrayList<VentaItemEntity>>();
            final LongSparseArray<ArrayList<AbonoEntity>> _collectionAbonos = new LongSparseArray<ArrayList<AbonoEntity>>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfClienteId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfClienteId);
              }
              if (_tmpKey != null) {
                _collectionCliente.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionItems.containsKey(_tmpKey_1)) {
                _collectionItems.put(_tmpKey_1, new ArrayList<VentaItemEntity>());
              }
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionAbonos.containsKey(_tmpKey_2)) {
                _collectionAbonos.put(_tmpKey_2, new ArrayList<AbonoEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipclientesAscomStockappDataLocalEntityClienteEntity(_collectionCliente);
            __fetchRelationshipventaItemsAscomStockappDataLocalEntityVentaItemEntity(_collectionItems);
            __fetchRelationshipabonosAscomStockappDataLocalEntityAbonoEntity(_collectionAbonos);
            final List<VentaConDetalle> _result = new ArrayList<VentaConDetalle>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final VentaConDetalle _item;
              final VentaEntity _tmpVenta;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final Long _tmpClienteId;
              if (_cursor.isNull(_cursorIndexOfClienteId)) {
                _tmpClienteId = null;
              } else {
                _tmpClienteId = _cursor.getLong(_cursorIndexOfClienteId);
              }
              final long _tmpFecha;
              _tmpFecha = _cursor.getLong(_cursorIndexOfFecha);
              final double _tmpSubtotal;
              _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
              final double _tmpDescuento;
              _tmpDescuento = _cursor.getDouble(_cursorIndexOfDescuento);
              final double _tmpTotal;
              _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
              final String _tmpEstadoPago;
              _tmpEstadoPago = _cursor.getString(_cursorIndexOfEstadoPago);
              final String _tmpNotas;
              if (_cursor.isNull(_cursorIndexOfNotas)) {
                _tmpNotas = null;
              } else {
                _tmpNotas = _cursor.getString(_cursorIndexOfNotas);
              }
              _tmpVenta = new VentaEntity(_tmpId,_tmpClienteId,_tmpFecha,_tmpSubtotal,_tmpDescuento,_tmpTotal,_tmpEstadoPago,_tmpNotas);
              final ClienteEntity _tmpCliente;
              final Long _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfClienteId)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getLong(_cursorIndexOfClienteId);
              }
              if (_tmpKey_3 != null) {
                _tmpCliente = _collectionCliente.get(_tmpKey_3);
              } else {
                _tmpCliente = null;
              }
              final ArrayList<VentaItemEntity> _tmpItemsCollection;
              final long _tmpKey_4;
              _tmpKey_4 = _cursor.getLong(_cursorIndexOfId);
              _tmpItemsCollection = _collectionItems.get(_tmpKey_4);
              final ArrayList<AbonoEntity> _tmpAbonosCollection;
              final long _tmpKey_5;
              _tmpKey_5 = _cursor.getLong(_cursorIndexOfId);
              _tmpAbonosCollection = _collectionAbonos.get(_tmpKey_5);
              _item = new VentaConDetalle(_tmpVenta,_tmpCliente,_tmpItemsCollection,_tmpAbonosCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<VentaConDetalle> getVentaConDetalle(final long id) {
    final String _sql = "SELECT * FROM ventas WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"clientes", "venta_items", "abonos",
        "ventas"}, new Callable<VentaConDetalle>() {
      @Override
      @Nullable
      public VentaConDetalle call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfClienteId = CursorUtil.getColumnIndexOrThrow(_cursor, "clienteId");
            final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
            final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
            final int _cursorIndexOfDescuento = CursorUtil.getColumnIndexOrThrow(_cursor, "descuento");
            final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
            final int _cursorIndexOfEstadoPago = CursorUtil.getColumnIndexOrThrow(_cursor, "estadoPago");
            final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
            final LongSparseArray<ClienteEntity> _collectionCliente = new LongSparseArray<ClienteEntity>();
            final LongSparseArray<ArrayList<VentaItemEntity>> _collectionItems = new LongSparseArray<ArrayList<VentaItemEntity>>();
            final LongSparseArray<ArrayList<AbonoEntity>> _collectionAbonos = new LongSparseArray<ArrayList<AbonoEntity>>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfClienteId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfClienteId);
              }
              if (_tmpKey != null) {
                _collectionCliente.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionItems.containsKey(_tmpKey_1)) {
                _collectionItems.put(_tmpKey_1, new ArrayList<VentaItemEntity>());
              }
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionAbonos.containsKey(_tmpKey_2)) {
                _collectionAbonos.put(_tmpKey_2, new ArrayList<AbonoEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipclientesAscomStockappDataLocalEntityClienteEntity(_collectionCliente);
            __fetchRelationshipventaItemsAscomStockappDataLocalEntityVentaItemEntity(_collectionItems);
            __fetchRelationshipabonosAscomStockappDataLocalEntityAbonoEntity(_collectionAbonos);
            final VentaConDetalle _result;
            if (_cursor.moveToFirst()) {
              final VentaEntity _tmpVenta;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final Long _tmpClienteId;
              if (_cursor.isNull(_cursorIndexOfClienteId)) {
                _tmpClienteId = null;
              } else {
                _tmpClienteId = _cursor.getLong(_cursorIndexOfClienteId);
              }
              final long _tmpFecha;
              _tmpFecha = _cursor.getLong(_cursorIndexOfFecha);
              final double _tmpSubtotal;
              _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
              final double _tmpDescuento;
              _tmpDescuento = _cursor.getDouble(_cursorIndexOfDescuento);
              final double _tmpTotal;
              _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
              final String _tmpEstadoPago;
              _tmpEstadoPago = _cursor.getString(_cursorIndexOfEstadoPago);
              final String _tmpNotas;
              if (_cursor.isNull(_cursorIndexOfNotas)) {
                _tmpNotas = null;
              } else {
                _tmpNotas = _cursor.getString(_cursorIndexOfNotas);
              }
              _tmpVenta = new VentaEntity(_tmpId,_tmpClienteId,_tmpFecha,_tmpSubtotal,_tmpDescuento,_tmpTotal,_tmpEstadoPago,_tmpNotas);
              final ClienteEntity _tmpCliente;
              final Long _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfClienteId)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getLong(_cursorIndexOfClienteId);
              }
              if (_tmpKey_3 != null) {
                _tmpCliente = _collectionCliente.get(_tmpKey_3);
              } else {
                _tmpCliente = null;
              }
              final ArrayList<VentaItemEntity> _tmpItemsCollection;
              final long _tmpKey_4;
              _tmpKey_4 = _cursor.getLong(_cursorIndexOfId);
              _tmpItemsCollection = _collectionItems.get(_tmpKey_4);
              final ArrayList<AbonoEntity> _tmpAbonosCollection;
              final long _tmpKey_5;
              _tmpKey_5 = _cursor.getLong(_cursorIndexOfId);
              _tmpAbonosCollection = _collectionAbonos.get(_tmpKey_5);
              _result = new VentaConDetalle(_tmpVenta,_tmpCliente,_tmpItemsCollection,_tmpAbonosCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<AbonoEntity>> getAbonosByVenta(final long ventaId) {
    final String _sql = "SELECT * FROM abonos WHERE ventaId = ? ORDER BY fecha DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, ventaId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"abonos"}, new Callable<List<AbonoEntity>>() {
      @Override
      @NonNull
      public List<AbonoEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVentaId = CursorUtil.getColumnIndexOrThrow(_cursor, "ventaId");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfMonto = CursorUtil.getColumnIndexOrThrow(_cursor, "monto");
          final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
          final List<AbonoEntity> _result = new ArrayList<AbonoEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AbonoEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpVentaId;
            _tmpVentaId = _cursor.getLong(_cursorIndexOfVentaId);
            final long _tmpFecha;
            _tmpFecha = _cursor.getLong(_cursorIndexOfFecha);
            final double _tmpMonto;
            _tmpMonto = _cursor.getDouble(_cursorIndexOfMonto);
            final String _tmpNotas;
            if (_cursor.isNull(_cursorIndexOfNotas)) {
              _tmpNotas = null;
            } else {
              _tmpNotas = _cursor.getString(_cursorIndexOfNotas);
            }
            _item = new AbonoEntity(_tmpId,_tmpVentaId,_tmpFecha,_tmpMonto,_tmpNotas);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTotalAbonadoByVenta(final long ventaId,
      final Continuation<? super Double> $completion) {
    final String _sql = "SELECT COALESCE(SUM(monto), 0) FROM abonos WHERE ventaId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, ventaId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Double> getTotalCxCPendiente() {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(v.total), 0) - COALESCE(SUM(a.monto), 0)\n"
            + "        FROM ventas v\n"
            + "        LEFT JOIN abonos a ON a.ventaId = v.id\n"
            + "        WHERE v.estadoPago IN ('FIADO', 'PARCIAL')\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"ventas",
        "abonos"}, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Double> getTotalVentasPorPeriodo(final long desde, final long hasta) {
    final String _sql = "SELECT COALESCE(SUM(total), 0) FROM ventas WHERE fecha BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, desde);
    _argIndex = 2;
    _statement.bindLong(_argIndex, hasta);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"ventas"}, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> getCountVentasPorPeriodo(final long desde, final long hasta) {
    final String _sql = "SELECT COUNT(*) FROM ventas WHERE fecha BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, desde);
    _argIndex = 2;
    _statement.bindLong(_argIndex, hasta);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"ventas"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getVentaById(final long id, final Continuation<? super VentaEntity> $completion) {
    final String _sql = "SELECT * FROM ventas WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<VentaEntity>() {
      @Override
      @Nullable
      public VentaEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfClienteId = CursorUtil.getColumnIndexOrThrow(_cursor, "clienteId");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfDescuento = CursorUtil.getColumnIndexOrThrow(_cursor, "descuento");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfEstadoPago = CursorUtil.getColumnIndexOrThrow(_cursor, "estadoPago");
          final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
          final VentaEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpClienteId;
            if (_cursor.isNull(_cursorIndexOfClienteId)) {
              _tmpClienteId = null;
            } else {
              _tmpClienteId = _cursor.getLong(_cursorIndexOfClienteId);
            }
            final long _tmpFecha;
            _tmpFecha = _cursor.getLong(_cursorIndexOfFecha);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpDescuento;
            _tmpDescuento = _cursor.getDouble(_cursorIndexOfDescuento);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final String _tmpEstadoPago;
            _tmpEstadoPago = _cursor.getString(_cursorIndexOfEstadoPago);
            final String _tmpNotas;
            if (_cursor.isNull(_cursorIndexOfNotas)) {
              _tmpNotas = null;
            } else {
              _tmpNotas = _cursor.getString(_cursorIndexOfNotas);
            }
            _result = new VentaEntity(_tmpId,_tmpClienteId,_tmpFecha,_tmpSubtotal,_tmpDescuento,_tmpTotal,_tmpEstadoPago,_tmpNotas);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipclientesAscomStockappDataLocalEntityClienteEntity(
      @NonNull final LongSparseArray<ClienteEntity> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipclientesAscomStockappDataLocalEntityClienteEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`nombre`,`telefono`,`notas`,`fechaCreacion` FROM `clientes` WHERE `id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfNombre = 1;
      final int _cursorIndexOfTelefono = 2;
      final int _cursorIndexOfNotas = 3;
      final int _cursorIndexOfFechaCreacion = 4;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final ClienteEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final String _tmpNombre;
          _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
          final String _tmpTelefono;
          if (_cursor.isNull(_cursorIndexOfTelefono)) {
            _tmpTelefono = null;
          } else {
            _tmpTelefono = _cursor.getString(_cursorIndexOfTelefono);
          }
          final String _tmpNotas;
          if (_cursor.isNull(_cursorIndexOfNotas)) {
            _tmpNotas = null;
          } else {
            _tmpNotas = _cursor.getString(_cursorIndexOfNotas);
          }
          final long _tmpFechaCreacion;
          _tmpFechaCreacion = _cursor.getLong(_cursorIndexOfFechaCreacion);
          _item_1 = new ClienteEntity(_tmpId,_tmpNombre,_tmpTelefono,_tmpNotas,_tmpFechaCreacion);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipventaItemsAscomStockappDataLocalEntityVentaItemEntity(
      @NonNull final LongSparseArray<ArrayList<VentaItemEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipventaItemsAscomStockappDataLocalEntityVentaItemEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`ventaId`,`productoId`,`productoNombre`,`varianteId`,`varianteLabel`,`cantidad`,`tipoPrecio`,`precioUnitario`,`subtotal` FROM `venta_items` WHERE `ventaId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "ventaId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfVentaId = 1;
      final int _cursorIndexOfProductoId = 2;
      final int _cursorIndexOfProductoNombre = 3;
      final int _cursorIndexOfVarianteId = 4;
      final int _cursorIndexOfVarianteLabel = 5;
      final int _cursorIndexOfCantidad = 6;
      final int _cursorIndexOfTipoPrecio = 7;
      final int _cursorIndexOfPrecioUnitario = 8;
      final int _cursorIndexOfSubtotal = 9;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<VentaItemEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final VentaItemEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpVentaId;
          _tmpVentaId = _cursor.getLong(_cursorIndexOfVentaId);
          final long _tmpProductoId;
          _tmpProductoId = _cursor.getLong(_cursorIndexOfProductoId);
          final String _tmpProductoNombre;
          _tmpProductoNombre = _cursor.getString(_cursorIndexOfProductoNombre);
          final Long _tmpVarianteId;
          if (_cursor.isNull(_cursorIndexOfVarianteId)) {
            _tmpVarianteId = null;
          } else {
            _tmpVarianteId = _cursor.getLong(_cursorIndexOfVarianteId);
          }
          final String _tmpVarianteLabel;
          if (_cursor.isNull(_cursorIndexOfVarianteLabel)) {
            _tmpVarianteLabel = null;
          } else {
            _tmpVarianteLabel = _cursor.getString(_cursorIndexOfVarianteLabel);
          }
          final int _tmpCantidad;
          _tmpCantidad = _cursor.getInt(_cursorIndexOfCantidad);
          final String _tmpTipoPrecio;
          _tmpTipoPrecio = _cursor.getString(_cursorIndexOfTipoPrecio);
          final double _tmpPrecioUnitario;
          _tmpPrecioUnitario = _cursor.getDouble(_cursorIndexOfPrecioUnitario);
          final double _tmpSubtotal;
          _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
          _item_1 = new VentaItemEntity(_tmpId,_tmpVentaId,_tmpProductoId,_tmpProductoNombre,_tmpVarianteId,_tmpVarianteLabel,_tmpCantidad,_tmpTipoPrecio,_tmpPrecioUnitario,_tmpSubtotal);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipabonosAscomStockappDataLocalEntityAbonoEntity(
      @NonNull final LongSparseArray<ArrayList<AbonoEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipabonosAscomStockappDataLocalEntityAbonoEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`ventaId`,`fecha`,`monto`,`notas` FROM `abonos` WHERE `ventaId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "ventaId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfVentaId = 1;
      final int _cursorIndexOfFecha = 2;
      final int _cursorIndexOfMonto = 3;
      final int _cursorIndexOfNotas = 4;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<AbonoEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final AbonoEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpVentaId;
          _tmpVentaId = _cursor.getLong(_cursorIndexOfVentaId);
          final long _tmpFecha;
          _tmpFecha = _cursor.getLong(_cursorIndexOfFecha);
          final double _tmpMonto;
          _tmpMonto = _cursor.getDouble(_cursorIndexOfMonto);
          final String _tmpNotas;
          if (_cursor.isNull(_cursorIndexOfNotas)) {
            _tmpNotas = null;
          } else {
            _tmpNotas = _cursor.getString(_cursorIndexOfNotas);
          }
          _item_1 = new AbonoEntity(_tmpId,_tmpVentaId,_tmpFecha,_tmpMonto,_tmpNotas);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
