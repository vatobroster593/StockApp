package com.stockapp.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.stockapp.data.local.entity.ProductoEntity;
import com.stockapp.data.local.entity.VarianteEntity;
import com.stockapp.data.local.relation.ProductoConVariantes;
import java.lang.Class;
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
public final class ProductoDao_Impl implements ProductoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ProductoEntity> __insertionAdapterOfProductoEntity;

  private final EntityInsertionAdapter<VarianteEntity> __insertionAdapterOfVarianteEntity;

  private final EntityDeletionOrUpdateAdapter<ProductoEntity> __updateAdapterOfProductoEntity;

  private final EntityDeletionOrUpdateAdapter<VarianteEntity> __updateAdapterOfVarianteEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteProducto;

  private final SharedSQLiteStatement __preparedStmtOfDeleteVariante;

  private final SharedSQLiteStatement __preparedStmtOfDecrementarStock;

  public ProductoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProductoEntity = new EntityInsertionAdapter<ProductoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `productos` (`id`,`nombre`,`descripcion`,`categoria`,`precioNormal`,`precioPorMayor`,`fotoUri`,`fechaCreacion`,`activo`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
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
        statement.bindLong(8, entity.getFechaCreacion());
        final int _tmp = entity.getActivo() ? 1 : 0;
        statement.bindLong(9, _tmp);
      }
    };
    this.__insertionAdapterOfVarianteEntity = new EntityInsertionAdapter<VarianteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `variantes` (`id`,`productoId`,`atributo`,`valor`,`stock`,`activo`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VarianteEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProductoId());
        statement.bindString(3, entity.getAtributo());
        statement.bindString(4, entity.getValor());
        statement.bindLong(5, entity.getStock());
        final int _tmp = entity.getActivo() ? 1 : 0;
        statement.bindLong(6, _tmp);
      }
    };
    this.__updateAdapterOfProductoEntity = new EntityDeletionOrUpdateAdapter<ProductoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `productos` SET `id` = ?,`nombre` = ?,`descripcion` = ?,`categoria` = ?,`precioNormal` = ?,`precioPorMayor` = ?,`fotoUri` = ?,`fechaCreacion` = ?,`activo` = ? WHERE `id` = ?";
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
        statement.bindLong(8, entity.getFechaCreacion());
        final int _tmp = entity.getActivo() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindLong(10, entity.getId());
      }
    };
    this.__updateAdapterOfVarianteEntity = new EntityDeletionOrUpdateAdapter<VarianteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `variantes` SET `id` = ?,`productoId` = ?,`atributo` = ?,`valor` = ?,`stock` = ?,`activo` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VarianteEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProductoId());
        statement.bindString(3, entity.getAtributo());
        statement.bindString(4, entity.getValor());
        statement.bindLong(5, entity.getStock());
        final int _tmp = entity.getActivo() ? 1 : 0;
        statement.bindLong(6, _tmp);
        statement.bindLong(7, entity.getId());
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
    this.__preparedStmtOfDeleteVariante = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE variantes SET activo = 0 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDecrementarStock = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE variantes SET stock = stock - ? WHERE id = ? AND stock >= ?";
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
  public Object insertVariante(final VarianteEntity variante,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfVarianteEntity.insertAndReturnId(variante);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertVariantes(final List<VarianteEntity> variantes,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfVarianteEntity.insert(variantes);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
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
  public Object updateVariante(final VarianteEntity variante,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfVarianteEntity.handle(variante);
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
  public Object deleteVariante(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteVariante.acquire();
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
          __preparedStmtOfDeleteVariante.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object decrementarStock(final long varianteId, final int cantidad,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDecrementarStock.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cantidad);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, varianteId);
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
  public Flow<List<ProductoConVariantes>> getProductosConVariantes() {
    final String _sql = "SELECT * FROM productos WHERE activo = 1 ORDER BY nombre ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"variantes",
        "productos"}, new Callable<List<ProductoConVariantes>>() {
      @Override
      @NonNull
      public List<ProductoConVariantes> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
            final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
            final int _cursorIndexOfCategoria = CursorUtil.getColumnIndexOrThrow(_cursor, "categoria");
            final int _cursorIndexOfPrecioNormal = CursorUtil.getColumnIndexOrThrow(_cursor, "precioNormal");
            final int _cursorIndexOfPrecioPorMayor = CursorUtil.getColumnIndexOrThrow(_cursor, "precioPorMayor");
            final int _cursorIndexOfFotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoUri");
            final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
            final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
            final LongSparseArray<ArrayList<VarianteEntity>> _collectionVariantes = new LongSparseArray<ArrayList<VarianteEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionVariantes.containsKey(_tmpKey)) {
                _collectionVariantes.put(_tmpKey, new ArrayList<VarianteEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipvariantesAscomStockappDataLocalEntityVarianteEntity(_collectionVariantes);
            final List<ProductoConVariantes> _result = new ArrayList<ProductoConVariantes>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final ProductoConVariantes _item;
              final ProductoEntity _tmpProducto;
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
              final long _tmpFechaCreacion;
              _tmpFechaCreacion = _cursor.getLong(_cursorIndexOfFechaCreacion);
              final boolean _tmpActivo;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfActivo);
              _tmpActivo = _tmp != 0;
              _tmpProducto = new ProductoEntity(_tmpId,_tmpNombre,_tmpDescripcion,_tmpCategoria,_tmpPrecioNormal,_tmpPrecioPorMayor,_tmpFotoUri,_tmpFechaCreacion,_tmpActivo);
              final ArrayList<VarianteEntity> _tmpVariantesCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpVariantesCollection = _collectionVariantes.get(_tmpKey_1);
              _item = new ProductoConVariantes(_tmpProducto,_tmpVariantesCollection);
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
  public Flow<List<ProductoConVariantes>> getProductosPorCategoria(final String categoria) {
    final String _sql = "SELECT * FROM productos WHERE activo = 1 AND categoria = ? ORDER BY nombre ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, categoria);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"variantes",
        "productos"}, new Callable<List<ProductoConVariantes>>() {
      @Override
      @NonNull
      public List<ProductoConVariantes> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
            final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
            final int _cursorIndexOfCategoria = CursorUtil.getColumnIndexOrThrow(_cursor, "categoria");
            final int _cursorIndexOfPrecioNormal = CursorUtil.getColumnIndexOrThrow(_cursor, "precioNormal");
            final int _cursorIndexOfPrecioPorMayor = CursorUtil.getColumnIndexOrThrow(_cursor, "precioPorMayor");
            final int _cursorIndexOfFotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoUri");
            final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
            final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
            final LongSparseArray<ArrayList<VarianteEntity>> _collectionVariantes = new LongSparseArray<ArrayList<VarianteEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionVariantes.containsKey(_tmpKey)) {
                _collectionVariantes.put(_tmpKey, new ArrayList<VarianteEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipvariantesAscomStockappDataLocalEntityVarianteEntity(_collectionVariantes);
            final List<ProductoConVariantes> _result = new ArrayList<ProductoConVariantes>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final ProductoConVariantes _item;
              final ProductoEntity _tmpProducto;
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
              final long _tmpFechaCreacion;
              _tmpFechaCreacion = _cursor.getLong(_cursorIndexOfFechaCreacion);
              final boolean _tmpActivo;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfActivo);
              _tmpActivo = _tmp != 0;
              _tmpProducto = new ProductoEntity(_tmpId,_tmpNombre,_tmpDescripcion,_tmpCategoria,_tmpPrecioNormal,_tmpPrecioPorMayor,_tmpFotoUri,_tmpFechaCreacion,_tmpActivo);
              final ArrayList<VarianteEntity> _tmpVariantesCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpVariantesCollection = _collectionVariantes.get(_tmpKey_1);
              _item = new ProductoConVariantes(_tmpProducto,_tmpVariantesCollection);
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
  public Flow<List<ProductoConVariantes>> buscarProductos(final String query) {
    final String _sql = "SELECT * FROM productos WHERE activo = 1 AND nombre LIKE '%' || ? || '%' ORDER BY nombre ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"variantes",
        "productos"}, new Callable<List<ProductoConVariantes>>() {
      @Override
      @NonNull
      public List<ProductoConVariantes> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
            final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
            final int _cursorIndexOfCategoria = CursorUtil.getColumnIndexOrThrow(_cursor, "categoria");
            final int _cursorIndexOfPrecioNormal = CursorUtil.getColumnIndexOrThrow(_cursor, "precioNormal");
            final int _cursorIndexOfPrecioPorMayor = CursorUtil.getColumnIndexOrThrow(_cursor, "precioPorMayor");
            final int _cursorIndexOfFotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoUri");
            final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
            final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
            final LongSparseArray<ArrayList<VarianteEntity>> _collectionVariantes = new LongSparseArray<ArrayList<VarianteEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionVariantes.containsKey(_tmpKey)) {
                _collectionVariantes.put(_tmpKey, new ArrayList<VarianteEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipvariantesAscomStockappDataLocalEntityVarianteEntity(_collectionVariantes);
            final List<ProductoConVariantes> _result = new ArrayList<ProductoConVariantes>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final ProductoConVariantes _item;
              final ProductoEntity _tmpProducto;
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
              final long _tmpFechaCreacion;
              _tmpFechaCreacion = _cursor.getLong(_cursorIndexOfFechaCreacion);
              final boolean _tmpActivo;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfActivo);
              _tmpActivo = _tmp != 0;
              _tmpProducto = new ProductoEntity(_tmpId,_tmpNombre,_tmpDescripcion,_tmpCategoria,_tmpPrecioNormal,_tmpPrecioPorMayor,_tmpFotoUri,_tmpFechaCreacion,_tmpActivo);
              final ArrayList<VarianteEntity> _tmpVariantesCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpVariantesCollection = _collectionVariantes.get(_tmpKey_1);
              _item = new ProductoConVariantes(_tmpProducto,_tmpVariantesCollection);
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
  public Flow<ProductoConVariantes> getProductoConVariantes(final long id) {
    final String _sql = "SELECT * FROM productos WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"variantes",
        "productos"}, new Callable<ProductoConVariantes>() {
      @Override
      @Nullable
      public ProductoConVariantes call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
            final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
            final int _cursorIndexOfCategoria = CursorUtil.getColumnIndexOrThrow(_cursor, "categoria");
            final int _cursorIndexOfPrecioNormal = CursorUtil.getColumnIndexOrThrow(_cursor, "precioNormal");
            final int _cursorIndexOfPrecioPorMayor = CursorUtil.getColumnIndexOrThrow(_cursor, "precioPorMayor");
            final int _cursorIndexOfFotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoUri");
            final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
            final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
            final LongSparseArray<ArrayList<VarianteEntity>> _collectionVariantes = new LongSparseArray<ArrayList<VarianteEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionVariantes.containsKey(_tmpKey)) {
                _collectionVariantes.put(_tmpKey, new ArrayList<VarianteEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipvariantesAscomStockappDataLocalEntityVarianteEntity(_collectionVariantes);
            final ProductoConVariantes _result;
            if (_cursor.moveToFirst()) {
              final ProductoEntity _tmpProducto;
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
              final long _tmpFechaCreacion;
              _tmpFechaCreacion = _cursor.getLong(_cursorIndexOfFechaCreacion);
              final boolean _tmpActivo;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfActivo);
              _tmpActivo = _tmp != 0;
              _tmpProducto = new ProductoEntity(_tmpId,_tmpNombre,_tmpDescripcion,_tmpCategoria,_tmpPrecioNormal,_tmpPrecioPorMayor,_tmpFotoUri,_tmpFechaCreacion,_tmpActivo);
              final ArrayList<VarianteEntity> _tmpVariantesCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpVariantesCollection = _collectionVariantes.get(_tmpKey_1);
              _result = new ProductoConVariantes(_tmpProducto,_tmpVariantesCollection);
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
  public Flow<List<VarianteEntity>> getVariantesByProducto(final long productoId) {
    final String _sql = "SELECT * FROM variantes WHERE productoId = ? AND activo = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, productoId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"variantes"}, new Callable<List<VarianteEntity>>() {
      @Override
      @NonNull
      public List<VarianteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProductoId = CursorUtil.getColumnIndexOrThrow(_cursor, "productoId");
          final int _cursorIndexOfAtributo = CursorUtil.getColumnIndexOrThrow(_cursor, "atributo");
          final int _cursorIndexOfValor = CursorUtil.getColumnIndexOrThrow(_cursor, "valor");
          final int _cursorIndexOfStock = CursorUtil.getColumnIndexOrThrow(_cursor, "stock");
          final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
          final List<VarianteEntity> _result = new ArrayList<VarianteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VarianteEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProductoId;
            _tmpProductoId = _cursor.getLong(_cursorIndexOfProductoId);
            final String _tmpAtributo;
            _tmpAtributo = _cursor.getString(_cursorIndexOfAtributo);
            final String _tmpValor;
            _tmpValor = _cursor.getString(_cursorIndexOfValor);
            final int _tmpStock;
            _tmpStock = _cursor.getInt(_cursorIndexOfStock);
            final boolean _tmpActivo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActivo);
            _tmpActivo = _tmp != 0;
            _item = new VarianteEntity(_tmpId,_tmpProductoId,_tmpAtributo,_tmpValor,_tmpStock,_tmpActivo);
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
  public Flow<List<ProductoConVariantes>> getProductosConStockBajo(final int umbral) {
    final String _sql = "\n"
            + "        SELECT p.* FROM productos p\n"
            + "        INNER JOIN variantes v ON v.productoId = p.id\n"
            + "        WHERE p.activo = 1 AND v.activo = 1\n"
            + "        GROUP BY p.id\n"
            + "        HAVING SUM(v.stock) <= ?\n"
            + "        ORDER BY SUM(v.stock) ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, umbral);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"variantes",
        "productos"}, new Callable<List<ProductoConVariantes>>() {
      @Override
      @NonNull
      public List<ProductoConVariantes> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
            final int _cursorIndexOfDescripcion = CursorUtil.getColumnIndexOrThrow(_cursor, "descripcion");
            final int _cursorIndexOfCategoria = CursorUtil.getColumnIndexOrThrow(_cursor, "categoria");
            final int _cursorIndexOfPrecioNormal = CursorUtil.getColumnIndexOrThrow(_cursor, "precioNormal");
            final int _cursorIndexOfPrecioPorMayor = CursorUtil.getColumnIndexOrThrow(_cursor, "precioPorMayor");
            final int _cursorIndexOfFotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "fotoUri");
            final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
            final int _cursorIndexOfActivo = CursorUtil.getColumnIndexOrThrow(_cursor, "activo");
            final LongSparseArray<ArrayList<VarianteEntity>> _collectionVariantes = new LongSparseArray<ArrayList<VarianteEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionVariantes.containsKey(_tmpKey)) {
                _collectionVariantes.put(_tmpKey, new ArrayList<VarianteEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipvariantesAscomStockappDataLocalEntityVarianteEntity(_collectionVariantes);
            final List<ProductoConVariantes> _result = new ArrayList<ProductoConVariantes>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final ProductoConVariantes _item;
              final ProductoEntity _tmpProducto;
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
              final long _tmpFechaCreacion;
              _tmpFechaCreacion = _cursor.getLong(_cursorIndexOfFechaCreacion);
              final boolean _tmpActivo;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfActivo);
              _tmpActivo = _tmp != 0;
              _tmpProducto = new ProductoEntity(_tmpId,_tmpNombre,_tmpDescripcion,_tmpCategoria,_tmpPrecioNormal,_tmpPrecioPorMayor,_tmpFotoUri,_tmpFechaCreacion,_tmpActivo);
              final ArrayList<VarianteEntity> _tmpVariantesCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpVariantesCollection = _collectionVariantes.get(_tmpKey_1);
              _item = new ProductoConVariantes(_tmpProducto,_tmpVariantesCollection);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipvariantesAscomStockappDataLocalEntityVarianteEntity(
      @NonNull final LongSparseArray<ArrayList<VarianteEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipvariantesAscomStockappDataLocalEntityVarianteEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`productoId`,`atributo`,`valor`,`stock`,`activo` FROM `variantes` WHERE `productoId` IN (");
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
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "productoId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfProductoId = 1;
      final int _cursorIndexOfAtributo = 2;
      final int _cursorIndexOfValor = 3;
      final int _cursorIndexOfStock = 4;
      final int _cursorIndexOfActivo = 5;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<VarianteEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final VarianteEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpProductoId;
          _tmpProductoId = _cursor.getLong(_cursorIndexOfProductoId);
          final String _tmpAtributo;
          _tmpAtributo = _cursor.getString(_cursorIndexOfAtributo);
          final String _tmpValor;
          _tmpValor = _cursor.getString(_cursorIndexOfValor);
          final int _tmpStock;
          _tmpStock = _cursor.getInt(_cursorIndexOfStock);
          final boolean _tmpActivo;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfActivo);
          _tmpActivo = _tmp != 0;
          _item_1 = new VarianteEntity(_tmpId,_tmpProductoId,_tmpAtributo,_tmpValor,_tmpStock,_tmpActivo);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
