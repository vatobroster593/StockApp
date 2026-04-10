package com.stockapp.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.stockapp.data.local.entity.ProductoEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
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
public final class ProductoDao_Impl implements ProductoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ProductoEntity> __insertionAdapterOfProductoEntity;

  private final EntityDeletionOrUpdateAdapter<ProductoEntity> __updateAdapterOfProductoEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteProducto;

  private final SharedSQLiteStatement __preparedStmtOfDecrementarStock;

  public ProductoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProductoEntity = new EntityInsertionAdapter<ProductoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `productos` (`id`,`nombre`,`descripcion`,`categoria`,`precioNormal`,`precioPorMayor`,`fotoUri`,`stock`,`fechaCreacion`,`activo`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProductoEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getNombre());
        if (entity.getDescripcion() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescripcion());
        }
        statement.bindString(4, entity.getCategoria());
        statement.bindDouble(5, entity.getPrecioNormal());
        statement.bindDouble(6, entity.getPrecioPorMayor());
        if (entity.getFotoUri() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getFotoUri());
        }
        statement.bindLong(8, entity.getStock());
        statement.bindLong(9, entity.getFechaCreacion());
        final int _tmp = entity.getActivo() ? 1 : 0;
        statement.bindLong(10, _tmp);
      }
    };
    this.__updateAdapterOfProductoEntity = new EntityDeletionOrUpdateAdapter<ProductoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `productos` SET `id` = ?,`nombre` = ?,`descripcion` = ?,`categoria` = ?,`precioNormal` = ?,`precioPorMayor` = ?,`fotoUri` = ?,`stock` = ?,`fechaCreacion` = ?,`activo` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProductoEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getNombre());
        if (entity.getDescripcion() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescripcion());
        }
        statement.bindString(4, entity.getCategoria());
        statement.bindDouble(5, entity.getPrecioNormal());
        statement.bindDouble(6, entity.getPrecioPorMayor());
        if (entity.getFotoUri() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getFotoUri());
        }
        statement.bindLong(8, entity.getStock());
        statement.bindLong(9, entity.getFechaCreacion());
        final int _tmp = entity.getActivo() ? 1 : 0;
        statement.bindLong(10, _tmp);
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteProducto = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE productos SET activo = 0 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDecrementarStock = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE productos SET stock = stock - ? WHERE id = ? AND stock >= ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertProducto(final ProductoEntity producto,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfProductoEntity.insertAndReturnId(producto);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateProducto(final ProductoEntity producto,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfProductoEntity.handle(producto);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteProducto(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteProducto.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteProducto.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object decrementarStock(final long productoId, final int cantidad,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDecrementarStock.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cantidad);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, productoId);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, cantidad);
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDecrementarStock.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ProductoEntity>> getProductos() {
    final String _sql = "SELECT * FROM productos WHERE activo = 1 ORDER BY nombre ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"productos"}, new Callable<List<ProductoEntity>>() {
      @Override
      @NonNull
      public List<ProductoEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfCategoria = CursorUtil.getColumnIndexOrThrow(_cursor, "categoria");
          final int _cursorIndexOfPrecioNormal = CursorUtil.getColumnIndexOrThrow(_cursor, "precioNormal");
          final int _cursorIndexOfPrecioPorMayor = CursorUtil.getColumnIndexOrThrow(_cursor, "precioPorMayor");
          final int _cursorIndexOfFotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoUri");
          final int _cursorIndexOfStock = CursorUtil.getColumnIndexOrThrow(_cursor, "stock");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final List<ProductoEntity> _result = new ArrayList<ProductoEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProductoEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpNombre;
            _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            final String _tmpCategoria;
            _tmpCategoria = _cursor.getString(_cursorIndexOfCategoria);
            final double _tmpPrecioNormal;
            _tmpPrecioNormal = _cursor.getDouble(_cursorIndexOfPrecioNormal);
            final double _tmpPrecioPorMayor;
            _tmpPrecioPorMayor = _cursor.getDouble(_cursorIndexOfPrecioPorMayor);
            final String _tmpFotoUri;
            if (_cursor.isNull(_cursorIndexOfFotoUri)) {
              _tmpFotoUri = null;
            } else {
              _tmpFotoUri = _cursor.getString(_cursorIndexOfFotoUri);
            }
            final int _tmpStock;
            _tmpStock = _cursor.getInt(_cursorIndexOfStock);
            final long _tmpFechaCreacion;
            _tmpFechaCreacion = _cursor.getLong(_cursorIndexOfFechaCreacion);
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            _item = new ProductoEntity(_tmpId,_tmpNombre,_tmpDescripcion,_tmpCategoria,_tmpPrecioNormal,_tmpPrecioPorMayor,_tmpFotoUri,_tmpStock,_tmpFechaCreacion,_tmpActivo);
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
  public Flow<ProductoEntity> getProducto(final long id) {
    final String _sql = "SELECT * FROM productos WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"productos"}, new Callable<ProductoEntity>() {
      @Override
      @Nullable
      public ProductoEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfCategoria = CursorUtil.getColumnIndexOrThrow(_cursor, "categoria");
          final int _cursorIndexOfPrecioNormal = CursorUtil.getColumnIndexOrThrow(_cursor, "precioNormal");
          final int _cursorIndexOfPrecioPorMayor = CursorUtil.getColumnIndexOrThrow(_cursor, "precioPorMayor");
          final int _cursorIndexOfFotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoUri");
          final int _cursorIndexOfStock = CursorUtil.getColumnIndexOrThrow(_cursor, "stock");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final ProductoEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpNombre;
            _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            final String _tmpCategoria;
            _tmpCategoria = _cursor.getString(_cursorIndexOfCategoria);
            final double _tmpPrecioNormal;
            _tmpPrecioNormal = _cursor.getDouble(_cursorIndexOfPrecioNormal);
            final double _tmpPrecioPorMayor;
            _tmpPrecioPorMayor = _cursor.getDouble(_cursorIndexOfPrecioPorMayor);
            final String _tmpFotoUri;
            if (_cursor.isNull(_cursorIndexOfFotoUri)) {
              _tmpFotoUri = null;
            } else {
              _tmpFotoUri = _cursor.getString(_cursorIndexOfFotoUri);
            }
            final int _tmpStock;
            _tmpStock = _cursor.getInt(_cursorIndexOfStock);
            final long _tmpFechaCreacion;
            _tmpFechaCreacion = _cursor.getLong(_cursorIndexOfFechaCreacion);
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            _result = new ProductoEntity(_tmpId,_tmpNombre,_tmpDescripcion,_tmpCategoria,_tmpPrecioNormal,_tmpPrecioPorMayor,_tmpFotoUri,_tmpStock,_tmpFechaCreacion,_tmpActivo);
          } else {
            _result = null;
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
  public Flow<List<ProductoEntity>> getProductosConStockBajo(final int umbral) {
    final String _sql = "SELECT * FROM productos WHERE activo = 1 AND stock <= ? ORDER BY stock ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, umbral);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"productos"}, new Callable<List<ProductoEntity>>() {
      @Override
      @NonNull
      public List<ProductoEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
          final int _cursorIndexOfCategoria = CursorUtil.getColumnIndexOrThrow(_cursor, "categoria");
          final int _cursorIndexOfPrecioNormal = CursorUtil.getColumnIndexOrThrow(_cursor, "precioNormal");
          final int _cursorIndexOfPrecioPorMayor = CursorUtil.getColumnIndexOrThrow(_cursor, "precioPorMayor");
          final int _cursorIndexOfFotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoUri");
          final int _cursorIndexOfStock = CursorUtil.getColumnIndexOrThrow(_cursor, "stock");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final List<ProductoEntity> _result = new ArrayList<ProductoEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProductoEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpNombre;
            _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            final String _tmpDescripcion;
            if (_cursor.isNull(_cursorIndexOfDescripcion)) {
              _tmpDescripcion = null;
            } else {
              _tmpDescripcion = _cursor.getString(_cursorIndexOfDescripcion);
            }
            final String _tmpCategoria;
            _tmpCategoria = _cursor.getString(_cursorIndexOfCategoria);
            final double _tmpPrecioNormal;
            _tmpPrecioNormal = _cursor.getDouble(_cursorIndexOfPrecioNormal);
            final double _tmpPrecioPorMayor;
            _tmpPrecioPorMayor = _cursor.getDouble(_cursorIndexOfPrecioPorMayor);
            final String _tmpFotoUri;
            if (_cursor.isNull(_cursorIndexOfFotoUri)) {
              _tmpFotoUri = null;
            } else {
              _tmpFotoUri = _cursor.getString(_cursorIndexOfFotoUri);
            }
            final int _tmpStock;
            _tmpStock = _cursor.getInt(_cursorIndexOfStock);
            final long _tmpFechaCreacion;
            _tmpFechaCreacion = _cursor.getLong(_cursorIndexOfFechaCreacion);
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            _item = new ProductoEntity(_tmpId,_tmpNombre,_tmpDescripcion,_tmpCategoria,_tmpPrecioNormal,_tmpPrecioPorMayor,_tmpFotoUri,_tmpStock,_tmpFechaCreacion,_tmpActivo);
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
}
