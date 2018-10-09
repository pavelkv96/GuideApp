package com.grsu.guideapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteOpenHelper;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.StorageUtils;
import com.grsu.guideapp.utils.StreamUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class CacheDBHelper extends SQLiteOpenHelper {

    private static final Object mLock = new Object();

    private static final String TAG = CacheDBHelper.class.getSimpleName();
    private static SQLiteDatabase mDb;

    private static final String primaryKey =
            TileConstants.COLUMN_KEY + "=? and " + TileConstants.COLUMN_PROVIDER + "=? limit 1";
    private static final String[] columns = {TileConstants.COLUMN_TILE};

    public CacheDBHelper(Context context) {
        super(context, Settings.CACHE_DATABASE_NAME, null, Settings.CACHE_DATABASE_VERSION);
        mDb = getDb();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private static SQLiteDatabase getDb() {
        if (mDb != null) {
            return mDb;
        }
        synchronized (mLock) {
            new File(Settings.CACHE).mkdir();
            File dbFile = new File(Settings.CACHE, Settings.CACHE_DATABASE_NAME);

            if (mDb == null) {
                try {
                    mDb = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
                    mDb.execSQL(TileConstants.CREATE_TABLE);
                } catch (Exception ex) {
                    Logs.e(TAG, "Unable to start the sqlite tile writer.", ex);
                    catchException(ex);
                    return null;
                }
            }
        }
        return mDb;
    }

    public static void refreshDb() {
        synchronized (mLock) {
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
            cv.put(TileConstants.COLUMN_KEY, index);
            cv.put(TileConstants.COLUMN_PROVIDER, pProvider);
            cv.put(TileConstants.COLUMN_TILE, bits);
            cv.put(TileConstants.COLUMN_EXPIRES, pTime);
            mDb.replace(TileConstants.TABLE, null, cv);


        } catch (SQLiteFullException ex) {
            catchException(ex);
        } catch (Exception ex) {
            Logs.e(TAG, "Unable to store cached tile from " + toString + " ", ex);
            catchException(ex);
        } finally {
            StreamUtils.closeStream(bos);
        }
    }

    public static byte[] getTile(long index, String tileProvider) {

        Cursor cur = getTileCursor(getParameters(index, tileProvider));
        byte[] bits = null;
        try {
            if (cur.moveToFirst()) {
                bits = cur.getBlob(cur.getColumnIndex(TileConstants.COLUMN_TILE));
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
            refreshDb();
        }

        synchronized (mLock) {
            StorageUtils.removeDir(Settings.CACHE);
        }
    }

    private static Cursor getTileCursor(String[] pParameters) {
        mDb = getDb();
        return mDb.query(TileConstants.TABLE, columns, primaryKey, pParameters, null, null, null);
    }

    private static String[] getParameters(long pIndex, String pTileSourceInfo) {
        return new String[]{String.valueOf(pIndex), pTileSourceInfo};
    }

    private static boolean isFunctionalException(final SQLiteException pSQLiteException) {
        switch (pSQLiteException.getClass().getSimpleName()) {
            case "SQLiteBindOrColumnIndexOutOfRangeException":
            case "SQLiteBlobTooBigException":
            case "SQLiteConstraintException":
            case "SQLiteDatatypeMismatchException":
            case "SQLiteFullException":
            case "SQLiteMisuseException":
            case "SQLiteTableLockedException":
                return true;
            case "SQLiteAbortException":
            case "SQLiteAccessPermException":
            case "SQLiteCantOpenDatabaseException":
            case "SQLiteDatabaseCorruptException":
            case "SQLiteDatabaseLockedException":
            case "SQLiteDiskIOException":
            case "SQLiteDoneException":
            case "SQLiteOutOfMemoryException":
            case "SQLiteReadOnlyDatabaseException":
                return false;
            default:
                return false;
        }
    }

    private static void catchException(final Exception pException) {
        if (pException instanceof SQLiteException) {
            if (!isFunctionalException((SQLiteException) pException)) {
                refreshDb();
            }
        }
    }

}
