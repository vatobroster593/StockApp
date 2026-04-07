package com.stockapp.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.stockapp.data.local.entity.ClienteEntity;
import com.stockapp.data.local.relation.ClienteConDeuda;
import java.lang.Class;
import java.lang.Exception;
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
public final class ClienteDao_Impl implements ClienteDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ClienteEntity> __insertionAdapterOfClienteEntity;

  private final EntityDeletionOrUpdateAdapter<ClienteEntity> __deletionAdapterOfClienteEntity;

  private final EntityDeletionOrUpdateAdapter<ClienteEntity> __updateAdapterOfClienteEntity;

  public ClienteDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfClienteEntity = new EntityInsertionAdapter<ClienteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `clientes` (`id`,`nombre`,`telefono`,`notas`,`fechaCreacion`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ClienteEntity entity) {
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
    this.__deletionAdapterOfClienteEntity = new EntityDeletionOrUpdateAdapter<ClienteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `clientes` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ClienteEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfClienteEntity = new EntityDeletionOrUpdateAdapter<ClienteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `clientes` SET `id` = ?,`nombre` = ?,`telefono` = ?,`notas` = ?,`fechaCreacion` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ClienteEntity entity) {
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
  }

  @Override
  public Object insertCliente(final ClienteEntity cliente,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfClienteEntity.insertAndReturnId(cliente);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCliente(final ClienteEntity cliente,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfClienteEntity.handle(cliente);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCliente(final ClienteEntity cliente,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfClienteEntity.handle(cliente);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ClienteEntity>> getClientes() {
    final String _sql = "SELECT * FROM clientes ORDER BY nombre ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"clientes"}, new Callable<List<ClienteEntity>>() {
      @Override
      @NonNull
      public List<ClienteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
          final List<ClienteEntity> _result = new ArrayList<ClienteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ClienteEntity _item;
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
            _item = new ClienteEntity(_tmpId,_tmpNombre,_tmpTelefono,_tmpNotas,_tmpFechaCreacion);
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
  public Flow<List<ClienteEntity>> buscarClientes(final String query) {
    final String _sql = "SELECT * FROM clientes WHERE nombre LIKE '%' || ? || '%' ORDER BY nombre ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"clientes"}, new Callable<List<ClienteEntity>>() {
      @Override
      @NonNull
      public List<ClienteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
          final List<ClienteEntity> _result = new ArrayList<ClienteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ClienteEntity _item;
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
            _item = new ClienteEntity(_tmpId,_tmpNombre,_tmpTelefono,_tmpNotas,_tmpFechaCreacion);
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
  public Object getClienteById(final long id,
      final Continuation<? super ClienteEntity> $completion) {
    final String _sql = "SELECT * FROM clientes WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ClienteEntity>() {
      @Override
      @Nullable
      public ClienteEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
          final ClienteEntity _result;
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
            _result = new ClienteEntity(_tmpId,_tmpNombre,_tmpTelefono,_tmpNotas,_tmpFechaCreacion);
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
  public Flow<List<ClienteConDeuda>> getClientesConDeuda() {
    final String _sql = "\n"
            + "        SELECT c.*,\n"
            + "               COALESCE(SUM(v.total), 0) - COALESCE(SUM(a.monto), 0) AS saldoPendiente\n"
            + "        FROM clientes c\n"
            + "        LEFT JOIN ventas v ON v.clienteId = c.id AND v.estadoPago IN ('FIADO', 'PARCIAL')\n"
            + "        LEFT JOIN abonos a ON a.ventaId = v.id\n"
            + "        GROUP BY c.id\n"
            + "        ORDER BY c.nombre ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"clientes", "ventas",
        "abonos"}, new Callable<List<ClienteConDeuda>>() {
      @Override
      @NonNull
      public List<ClienteConDeuda> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfTelefono = CursorUtil.getColumnIndexOrThrow(_cursor, "telefono");
          final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
          final int _cursorIndexOfFechaCreacion = CursorUtil.getColumnIndexOrThrow(_cursor, "fechaCreacion");
          final int _cursorIndexOfSaldoPendiente = CursorUtil.getColumnIndexOrThrow(_cursor, "saldoPendiente");
          final List<ClienteConDeuda> _result = new ArrayList<ClienteConDeuda>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ClienteConDeuda _item;
            final double _tmpSaldoPendiente;
            _tmpSaldoPendiente = _cursor.getDouble(_cursorIndexOfSaldoPendiente);
            final ClienteEntity _tmpCliente;
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
            _tmpCliente = new ClienteEntity(_tmpId,_tmpNombre,_tmpTelefono,_tmpNotas,_tmpFechaCreacion);
            _item = new ClienteConDeuda(_tmpCliente,_tmpSaldoPendiente);
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
