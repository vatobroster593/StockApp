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
import com.stockapp.data.local.entity.CompraProveedorEntity;
import com.stockapp.data.local.entity.PagoProveedorEntity;
import com.stockapp.data.local.entity.ProveedorEntity;
import com.stockapp.data.local.relation.CompraConPagos;
import com.stockapp.data.local.relation.ProveedorConDeuda;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
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
public final class ProveedorDao_Impl implements ProveedorDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ProveedorEntity> __insertionAdapterOfProveedorEntity;

  private final EntityInsertionAdapter<CompraProveedorEntity> __insertionAdapterOfCompraProveedorEntity;

  private final EntityInsertionAdapter<PagoProveedorEntity> __insertionAdapterOfPagoProveedorEntity;

  private final EntityDeletionOrUpdateAdapter<ProveedorEntity> __deletionAdapterOfProveedorEntity;

  private final EntityDeletionOrUpdateAdapter<ProveedorEntity> __updateAdapterOfProveedorEntity;

  private final EntityDeletionOrUpdateAdapter<CompraProveedorEntity> __updateAdapterOfCompraProveedorEntity;

  public ProveedorDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProveedorEntity = new EntityInsertionAdapter<ProveedorEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `proveedores` (`id`,`nombre`,`telefono`,`notas`,`fechaCreacion`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProveedorEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getNombre());
        if (entity.getTelefono() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTelefono());
        }
        if (entity.getNotas() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getNotas());
        }
        statement.bindLong(5, entity.getFechaCreacion());
      }
    };
    this.__insertionAdapterOfCompraProveedorEntity = new EntityInsertionAdapter<CompraProveedorEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `compras_proveedor` (`id`,`proveedorId`,`fecha`,`descripcion`,`total`,`estadoPago`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CompraProveedorEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProveedorId());
        statement.bindLong(3, entity.getFecha());
        statement.bindString(4, entity.getDescripcion());
        statement.bindDouble(5, entity.getTotal());
        statement.bindString(6, entity.getEstadoPago());
      }
    };
    this.__insertionAdapterOfPagoProveedorEntity = new EntityInsertionAdapter<PagoProveedorEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `pagos_proveedor` (`id`,`compraProveedorId`,`fecha`,`monto`,`notas`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PagoProveedorEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCompraProveedorId());
        statement.bindLong(3, entity.getFecha());
        statement.bindDouble(4, entity.getMonto());
        if (entity.getNotas() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getNotas());
        }
      }
    };
    this.__deletionAdapterOfProveedorEntity = new EntityDeletionOrUpdateAdapter<ProveedorEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `proveedores` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProveedorEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfProveedorEntity = new EntityDeletionOrUpdateAdapter<ProveedorEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `proveedores` SET `id` = ?,`nombre` = ?,`telefono` = ?,`notas` = ?,`fechaCreacion` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProveedorEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getNombre());
        if (entity.getTelefono() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTelefono());
        }
        if (entity.getNotas() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getNotas());
        }
        statement.bindLong(5, entity.getFechaCreacion());
        statement.bindLong(6, entity.getId());
      }
    };
    this.__updateAdapterOfCompraProveedorEntity = new EntityDeletionOrUpdateAdapter<CompraProveedorEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `compras_proveedor` SET `id` = ?,`proveedorId` = ?,`fecha` = ?,`descripcion` = ?,`total` = ?,`estadoPago` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CompraProveedorEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProveedorId());
        statement.bindLong(3, entity.getFecha());
        statement.bindString(4, entity.getDescripcion());
        statement.bindDouble(5, entity.getTotal());
        statement.bindString(6, entity.getEstadoPago());
        statement.bindLong(7, entity.getId());
      }
    };
  }

  @Override
  public Object insertProveedor(final ProveedorEntity proveedor,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfProveedorEntity.insertAndReturnId(proveedor);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertCompra(final CompraProveedorEntity compra,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCompraProveedorEntity.insertAndReturnId(compra);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertPago(final PagoProveedorEntity pago,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPagoProveedorEntity.insertAndReturnId(pago);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteProveedor(final ProveedorEntity proveedor,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfProveedorEntity.handle(proveedor);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateProveedor(final ProveedorEntity proveedor,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfProveedorEntity.handle(proveedor);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCompra(final CompraProveedorEntity compra,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCompraProveedorEntity.handle(compra);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ProveedorEntity>> getProveedores() {
    final String _sql = "SELECT * FROM proveedores ORDER BY nombre ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"proveedores"}, new Callable<List<ProveedorEntity>>() {
      @Override
      @NonNull
      public List<ProveedorEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
          final List<ProveedorEntity> _result = new ArrayList<ProveedorEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProveedorEntity _item;
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
            _item = new ProveedorEntity(_tmpId,_tmpNombre,_tmpTelefono,_tmpNotas,_tmpFechaCreacion);
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
  public Object getProveedorById(final long id,
      final Continuation<? super ProveedorEntity> $completion) {
    final String _sql = "SELECT * FROM proveedores WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ProveedorEntity>() {
      @Override
      @Nullable
      public ProveedorEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
          final ProveedorEntity _result;
          if (_cursor.moveToFirst()) {
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
            _result = new ProveedorEntity(_tmpId,_tmpNombre,_tmpTelefono,_tmpNotas,_tmpFechaCreacion);
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

  @Override
  public Flow<List<CompraProveedorEntity>> getComprasPorProveedor(final long proveedorId) {
    final String _sql = "SELECT * FROM compras_proveedor WHERE proveedorId = ? ORDER BY fecha DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, proveedorId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"compras_proveedor"}, new Callable<List<CompraProveedorEntity>>() {
      @Override
      @NonNull
      public List<CompraProveedorEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProveedorId = CursorUtil.getColumnIndexOrThrow(_cursor, "proveedorId");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfEstadoPago = CursorUtil.getColumnIndexOrThrow(_cursor, "estadoPago");
          final List<CompraProveedorEntity> _result = new ArrayList<CompraProveedorEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CompraProveedorEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProveedorId;
            _tmpProveedorId = _cursor.getLong(_cursorIndexOfProveedorId);
            final long _tmpFecha;
            _tmpFecha = _cursor.getLong(_cursorIndexOfFecha);
            final String _tmpDescripcion;
            _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final String _tmpEstadoPago;
            _tmpEstadoPago = _cursor.getString(_cursorIndexOfEstadoPago);
            _item = new CompraProveedorEntity(_tmpId,_tmpProveedorId,_tmpFecha,_tmpDescripcion,_tmpTotal,_tmpEstadoPago);
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
  public Flow<List<CompraConPagos>> getComprasConPagosPorProveedor(final long proveedorId) {
    final String _sql = "SELECT * FROM compras_proveedor WHERE proveedorId = ? ORDER BY fecha DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, proveedorId);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"pagos_proveedor",
        "compras_proveedor"}, new Callable<List<CompraConPagos>>() {
      @Override
      @NonNull
      public List<CompraConPagos> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfProveedorId = CursorUtil.getColumnIndexOrThrow(_cursor, "proveedorId");
            final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
            final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
            final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
            final int _cursorIndexOfEstadoPago = CursorUtil.getColumnIndexOrThrow(_cursor, "estadoPago");
            final LongSparseArray<ArrayList<PagoProveedorEntity>> _collectionPagos = new LongSparseArray<ArrayList<PagoProveedorEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionPagos.containsKey(_tmpKey)) {
                _collectionPagos.put(_tmpKey, new ArrayList<PagoProveedorEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshippagosProveedorAscomStockappDataLocalEntityPagoProveedorEntity(_collectionPagos);
            final List<CompraConPagos> _result = new ArrayList<CompraConPagos>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final CompraConPagos _item;
              final CompraProveedorEntity _tmpCompra;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final long _tmpProveedorId;
              _tmpProveedorId = _cursor.getLong(_cursorIndexOfProveedorId);
              final long _tmpFecha;
              _tmpFecha = _cursor.getLong(_cursorIndexOfFecha);
              final String _tmpDescripcion;
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
              final double _tmpTotal;
              _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
              final String _tmpEstadoPago;
              _tmpEstadoPago = _cursor.getString(_cursorIndexOfEstadoPago);
              _tmpCompra = new CompraProveedorEntity(_tmpId,_tmpProveedorId,_tmpFecha,_tmpDescripcion,_tmpTotal,_tmpEstadoPago);
              final ArrayList<PagoProveedorEntity> _tmpPagosCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpPagosCollection = _collectionPagos.get(_tmpKey_1);
              _item = new CompraConPagos(_tmpCompra,_tmpPagosCollection);
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
  public Object getCompraById(final long id,
      final Continuation<? super CompraProveedorEntity> $completion) {
    final String _sql = "SELECT * FROM compras_proveedor WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CompraProveedorEntity>() {
      @Override
      @Nullable
      public CompraProveedorEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProveedorId = CursorUtil.getColumnIndexOrThrow(_cursor, "proveedorId");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfEstadoPago = CursorUtil.getColumnIndexOrThrow(_cursor, "estadoPago");
          final CompraProveedorEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProveedorId;
            _tmpProveedorId = _cursor.getLong(_cursorIndexOfProveedorId);
            final long _tmpFecha;
            _tmpFecha = _cursor.getLong(_cursorIndexOfFecha);
            final String _tmpDescripcion;
            _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final String _tmpEstadoPago;
            _tmpEstadoPago = _cursor.getString(_cursorIndexOfEstadoPago);
            _result = new CompraProveedorEntity(_tmpId,_tmpProveedorId,_tmpFecha,_tmpDescripcion,_tmpTotal,_tmpEstadoPago);
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

  @Override
  public Flow<List<PagoProveedorEntity>> getPagosPorCompra(final long compraId) {
    final String _sql = "SELECT * FROM pagos_proveedor WHERE compraProveedorId = ? ORDER BY fecha DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, compraId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pagos_proveedor"}, new Callable<List<PagoProveedorEntity>>() {
      @Override
      @NonNull
      public List<PagoProveedorEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCompraProveedorId = CursorUtil.getColumnIndexOrThrow(_cursor, "compraProveedorId");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfMonto = CursorUtil.getColumnIndexOrThrow(_cursor, "monto");
          final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
          final List<PagoProveedorEntity> _result = new ArrayList<PagoProveedorEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PagoProveedorEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCompraProveedorId;
            _tmpCompraProveedorId = _cursor.getLong(_cursorIndexOfCompraProveedorId);
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
            _item = new PagoProveedorEntity(_tmpId,_tmpCompraProveedorId,_tmpFecha,_tmpMonto,_tmpNotas);
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
  public Object getTotalPagadoPorCompra(final long compraId,
      final Continuation<? super Double> $completion) {
    final String _sql = "SELECT COALESCE(SUM(monto), 0) FROM pagos_proveedor WHERE compraProveedorId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, compraId);
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
  public Flow<Double> getTotalCxPPendiente() {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(c.total), 0) - COALESCE(SUM(p.monto), 0)\n"
            + "        FROM compras_proveedor c\n"
            + "        LEFT JOIN pagos_proveedor p ON p.compraProveedorId = c.id\n"
            + "        WHERE c.estadoPago IN ('PENDIENTE', 'PARCIAL')\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"compras_proveedor",
        "pagos_proveedor"}, new Callable<Double>() {
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
  public Flow<List<ProveedorConDeuda>> getProveedoresConDeuda() {
    final String _sql = "\n"
            + "        SELECT pv.*,\n"
            + "               COALESCE(SUM(c.total), 0) - COALESCE(SUM(p.monto), 0) AS deudaPendiente\n"
            + "        FROM proveedores pv\n"
            + "        LEFT JOIN compras_proveedor c ON c.proveedorId = pv.id AND c.estadoPago IN ('PENDIENTE', 'PARCIAL')\n"
            + "        LEFT JOIN pagos_proveedor p ON p.compraProveedorId = c.id\n"
            + "        GROUP BY pv.id\n"
            + "        ORDER BY pv.nombre ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"proveedores", "compras_proveedor",
        "pagos_proveedor"}, new Callable<List<ProveedorConDeuda>>() {
      @Override
      @NonNull
      public List<ProveedorConDeuda> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
          final int _cursorIndexOfDeudaPendiente = CursorUtil.getColumnIndexOrThrow(_cursor, "deudaPendiente");
          final List<ProveedorConDeuda> _result = new ArrayList<ProveedorConDeuda>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProveedorConDeuda _item;
            final double _tmpDeudaPendiente;
            _tmpDeudaPendiente = _cursor.getDouble(_cursorIndexOfDeudaPendiente);
            final ProveedorEntity _tmpProveedor;
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
            _tmpProveedor = new ProveedorEntity(_tmpId,_tmpNombre,_tmpTelefono,_tmpNotas,_tmpFechaCreacion);
            _item = new ProveedorConDeuda(_tmpProveedor,_tmpDeudaPendiente);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshippagosProveedorAscomStockappDataLocalEntityPagoProveedorEntity(
      @NonNull final LongSparseArray<ArrayList<PagoProveedorEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshippagosProveedorAscomStockappDataLocalEntityPagoProveedorEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`compraProveedorId`,`fecha`,`monto`,`notas` FROM `pagos_proveedor` WHERE `compraProveedorId` IN (");
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
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "compraProveedorId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfCompraProveedorId = 1;
      final int _cursorIndexOfFecha = 2;
      final int _cursorIndexOfMonto = 3;
      final int _cursorIndexOfNotas = 4;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<PagoProveedorEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final PagoProveedorEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpCompraProveedorId;
          _tmpCompraProveedorId = _cursor.getLong(_cursorIndexOfCompraProveedorId);
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
          _item_1 = new PagoProveedorEntity(_tmpId,_tmpCompraProveedorId,_tmpFecha,_tmpMonto,_tmpNotas);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
