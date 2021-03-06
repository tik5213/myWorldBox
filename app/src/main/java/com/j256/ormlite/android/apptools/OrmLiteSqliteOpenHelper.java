package com.j256.ormlite.android.apptools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.misc.IOUtils;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.DatabaseTableConfigLoader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

public abstract class OrmLiteSqliteOpenHelper extends SQLiteOpenHelper {
    protected static Logger logger = LoggerFactory.getLogger(OrmLiteSqliteOpenHelper.class);
    protected boolean cancelQueriesEnabled;
    protected AndroidConnectionSource connectionSource;
    private volatile boolean isOpen;

    public abstract void onCreate(SQLiteDatabase sQLiteDatabase, ConnectionSource connectionSource);

    public abstract void onUpgrade(SQLiteDatabase sQLiteDatabase, ConnectionSource connectionSource, int i, int i2);

    public OrmLiteSqliteOpenHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
        this.connectionSource = new AndroidConnectionSource((SQLiteOpenHelper) this);
        this.isOpen = true;
        logger.trace("{}: constructed connectionSource {}", (Object) this, this.connectionSource);
    }

    public OrmLiteSqliteOpenHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion, int configFileId) {
        this(context, databaseName, factory, databaseVersion, openFileId(context, configFileId));
    }

    public OrmLiteSqliteOpenHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion, File configFile) {
        this(context, databaseName, factory, databaseVersion, openFile(configFile));
    }

    public OrmLiteSqliteOpenHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion, InputStream stream) {
        SQLException e;
        Throwable th;
        super(context, databaseName, factory, databaseVersion);
        this.connectionSource = new AndroidConnectionSource((SQLiteOpenHelper) this);
        this.isOpen = true;
        if (stream != null) {
            BufferedReader reader = null;
            try {
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(stream), 4096);
                stream = null;
                try {
                    DaoManager.addCachedDatabaseConfigs(DatabaseTableConfigLoader.loadDatabaseConfigFromReader(reader2));
                    IOUtils.closeQuietly(reader2);
                    IOUtils.closeQuietly(null);
                } catch (SQLException e2) {
                    e = e2;
                    reader = reader2;
                    try {
                        throw new IllegalStateException("Could not load object config file", e);
                    } catch (Throwable th2) {
                        th = th2;
                        IOUtils.closeQuietly(reader);
                        IOUtils.closeQuietly(stream);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    reader = reader2;
                    IOUtils.closeQuietly(reader);
                    IOUtils.closeQuietly(stream);
                    throw th;
                }
            } catch (SQLException e3) {
                e = e3;
                throw new IllegalStateException("Could not load object config file", e);
            }
        }
    }

    public ConnectionSource getConnectionSource() {
        if (!this.isOpen) {
            logger.warn(new IllegalStateException(), "Getting connectionSource was called after closed");
        }
        return this.connectionSource;
    }

    public final void onCreate(SQLiteDatabase db) {
        ConnectionSource cs = getConnectionSource();
        DatabaseConnection conn = cs.getSpecialConnection(null);
        boolean clearSpecial = false;
        if (conn == null) {
            conn = new AndroidDatabaseConnection(db, true, this.cancelQueriesEnabled);
            try {
                cs.saveSpecialConnection(conn);
                clearSpecial = true;
            } catch (SQLException e) {
                throw new IllegalStateException("Could not save special connection", e);
            }
        }
        try {
            onCreate(db, cs);
        } finally {
            if (clearSpecial) {
                cs.clearSpecialConnection(conn);
            }
        }
    }

    public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ConnectionSource cs = getConnectionSource();
        DatabaseConnection conn = cs.getSpecialConnection(null);
        boolean clearSpecial = false;
        if (conn == null) {
            conn = new AndroidDatabaseConnection(db, true, this.cancelQueriesEnabled);
            try {
                cs.saveSpecialConnection(conn);
                clearSpecial = true;
            } catch (SQLException e) {
                throw new IllegalStateException("Could not save special connection", e);
            }
        }
        try {
            onUpgrade(db, cs, oldVersion, newVersion);
        } finally {
            if (clearSpecial) {
                cs.clearSpecialConnection(conn);
            }
        }
    }

    public void close() {
        super.close();
        this.connectionSource.close();
        this.isOpen = false;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException {
        return DaoManager.createDao(getConnectionSource(), (Class) clazz);
    }

    public <D extends RuntimeExceptionDao<T, ?>, T> D getRuntimeExceptionDao(Class<T> clazz) {
        try {
            return new RuntimeExceptionDao(getDao(clazz));
        } catch (SQLException e) {
            throw new RuntimeException("Could not create RuntimeExcepitionDao for class " + clazz, e);
        }
    }

    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(super.hashCode());
    }

    private static InputStream openFileId(Context context, int fileId) {
        InputStream stream = context.getResources().openRawResource(fileId);
        if (stream != null) {
            return stream;
        }
        throw new IllegalStateException("Could not find object config file with id " + fileId);
    }

    private static InputStream openFile(File configFile) {
        if (configFile == null) {
            return null;
        }
        try {
            return new FileInputStream(configFile);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not open config file " + configFile, e);
        }
    }
}
