package com.grsu.guideapp.databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteFullException;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.StorageUtils;
import com.grsu.guideapp.utils.StreamUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class CacheDBHelper implements TileConstants {

    private static final String TAG = CacheDBHelper.class.getSimpleName();
    private static SQLiteDatabase mDb;

    private static SQLiteDatabase getDb() {
        if (mDb != null) {
            return mDb;
        }
        synchronized (TAG) {
            new File(Settings.CACHE).mkdirs();
            File dbFile = new File(Settings.CACHE, Settings.CACHE_DATABASE_NAME);

            if (mDb == null) {
                try {
                    mDb = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
                    mDb.execSQL(CREATE_TABLE);
                } catch (Exception ex) {
                    Logs.e(TAG, "Unable to start the sqlite tile writer.", ex);
                    catchException(ex);
                    return null;
                }
            }
        }
        return mDb;
    }

    public static void disconnectDB() {
        synchronized (TAG) {
            if (mDb != null) {
                mDb.close();
                mDb = null;
            }
        }
    }

    public static void saveTile(long pIndex, String pProvider, InputStream pStream, Long pTime) {

        String message = "Unable to store cached tile from ";
        String toString = MapUtils.toString(pIndex);

        mDb = getDb();
        if (mDb == null || !mDb.isOpen()) {
            Logs.e(TAG, message + toString + ", database not available.");
            return;
        }

        ByteArrayOutputStream bos = null;
        try {
            final long index = MapUtils.getIndex(pIndex);

            bos = new ByteArrayOutputStream();
            StreamUtils.copyFile(pStream, bos, new byte[512]);

            byte[] bits = bos.toByteArray();

            ContentValues cv = new ContentValues();
            cv.put(COLUMN_KEY, index);
            cv.put(COLUMN_PROVIDER, pProvider);
            cv.put(COLUMN_TILE, bits);
            cv.put(COLUMN_EXPIRES, pTime);
            mDb.replace(TABLE, null, cv);


        } catch (SQLiteFullException ex) {
            catchException(ex);
        } catch (Exception ex) {
            Logs.e(TAG, message + toString + " ", ex);
            catchException(ex);
        } finally {
            StreamUtils.closeStream(pStream);
            StreamUtils.closeStream(bos);
        }
    }

    public static byte[] getTile(long index, String tileProvider) {

        Cursor cur = getTileCursor(getParameters(index, tileProvider));
        byte[] bits = null;
        try {
            if (cur != null && cur.moveToFirst()) {
                bits = cur.getBlob(cur.getColumnIndex(COLUMN_TILE));
            }
        } catch (NullPointerException e) {
            Logs.e(TAG, e.getMessage(), e);
        } finally {
            StreamUtils.closeStream(cur);
        }

        return bits;
    }

    public static void clearCache() {
        if (mDb != null) {
            disconnectDB();
        }

        synchronized (TAG) {
            StorageUtils.removeDir(Settings.CACHE);
        }
    }

    private static Cursor getTileCursor(String[] pParameters) {
        mDb = getDb();
        if (mDb != null) {
            return mDb.query(TABLE, columns, primaryKey, pParameters, null, null, null);
        }
        return null;
    }

    private static String[] getParameters(long pIndex, String pTileSourceInfo) {
        return new String[]{String.valueOf(pIndex), pTileSourceInfo};
    }

    private static boolean isFunctionalException(final SQLiteException pSQLiteException) {
        switch (pSQLiteException.getClass().getSimpleName()) {
            case SQLITE_BIND_OR_COLUMN_INDEX_OUT_OF_RANGE_EXCEPTION:
            case SQLITE_BLOB_TOO_BIG_EXCEPTION:
            case SQLITE_CONSTRAINT_EXCEPTION:
            case SQLITE_DATATYPE_MISMATCH_EXCEPTION:
            case SQLITE_FULL_EXCEPTION:
            case SQLITE_MISUSE_EXCEPTION:
            case SQLITE_TABLE_LOCKED_EXCEPTION:
                return true;
            case SQLITE_ABORT_EXCEPTION:
            case SQLITE_ACCESS_PERM_EXCEPTION:
            case SQLITE_CANT_OPEN_DATABASE_EXCEPTION:
            case SQLITE_DATABASE_CORRUPT_EXCEPTION:
            case SQLITE_DATABASE_LOCKED_EXCEPTION:
            case SQLITE_DISK_IOEXCEPTION:
            case SQLITE_DONE_EXCEPTION:
            case SQLITE_OUT_OF_MEMORY_EXCEPTION:
            case SQLITE_READ_ONLY_DATABASE_EXCEPTION:
                return false;
            default:
                return false;
        }
    }

    private static void catchException(final Exception pException) {
        if (pException instanceof SQLiteException) {
            if (!isFunctionalException((SQLiteException) pException)) {
                disconnectDB();
            }
        }
    }

}
