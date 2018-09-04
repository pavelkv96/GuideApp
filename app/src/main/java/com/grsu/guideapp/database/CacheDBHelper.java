package com.grsu.guideapp.database;

import static com.grsu.guideapp.utils.MapUtils.getIndex;
import static com.grsu.guideapp.utils.constants.ConstantsPaths.CACHE;
import static com.grsu.guideapp.utils.constants.ConstantsPaths.CACHE_FILE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteOpenHelper;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.StorageUtils;
import com.grsu.guideapp.utils.StreamUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class CacheDBHelper extends SQLiteOpenHelper implements TileConstants {

    private static final Object mLock = new Object();

    protected static SQLiteDatabase mDb;
    protected static File db_file;

    private static final String TAG = CacheDBHelper.class.getSimpleName();
    private static final Integer VERSION = 1;
    private static final String DB_NAME = "cache.db";

    public CacheDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        mDb = getDb();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }


    public static boolean saveFile(final long pMapTileIndex, final String pTileSourceInfo,
            final InputStream pStream, final Long pExpirationTime) {

        //final SQLiteDatabase db = getDb();

        mDb = getDb();
        if (mDb == null || !mDb.isOpen()) {
            Logs.e(TAG, "Unable to store cached tile from " + pTileSourceInfo + " " + MapUtils
                    .toString(pMapTileIndex) + ", database not available.");
            return false;
        }

        ByteArrayOutputStream bos = null;
        try {
            ContentValues cv = new ContentValues();
            final long index = getIndex(pMapTileIndex);
            cv.put(COLUMN_PROVIDER, pTileSourceInfo);

            byte[] buffer = new byte[512];
            int l;
            bos = new ByteArrayOutputStream();
            while ((l = pStream.read(buffer)) != -1) {
                bos.write(buffer, 0, l);
            }
            byte[] bits = bos.toByteArray(); // if a variable is required at all

            cv.put(COLUMN_KEY, index);
            cv.put(COLUMN_TILE, bits);
            cv.put(COLUMN_EXPIRES, pExpirationTime);
            mDb.replace(TABLE, null, cv);


        } catch (SQLiteFullException ex) {
            //the drive is full! trigger the clean up operation
            //may want to consider reducing the trim size automagically
            catchException(ex);
        } catch (Exception ex) {
            //note, although we check for db null state at the beginning of this method, it's possible for the
            //db to be closed during the execution of this method
            Logs.e(TAG, "Unable to store cached tile from " + pTileSourceInfo + " " + MapUtils
                    .toString(pMapTileIndex) + " ", ex);
            catchException(ex);
        } finally {
            StreamUtils.closeStream(bos);
        }
        return false;
    }

    private static SQLiteDatabase getDb() {
        if (mDb != null) {
            return mDb;
        }
        synchronized (mLock) {
            new File(CACHE).mkdir();
            db_file = new File(CACHE + CACHE_FILE);

            if (mDb == null) {
                try {
                    mDb = SQLiteDatabase.openOrCreateDatabase(db_file, null);
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

    private static void catchException(final Exception pException) {
        if (pException instanceof SQLiteException) {
            if (!isFunctionalException((SQLiteException) pException)) {
                refreshDb();
            }
        }
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

    public static void refreshDb() {
        synchronized (mLock) {
            if (mDb != null) {
                mDb.close();
                mDb = null;
            }
        }
    }

    public static boolean clearCache() {
        if (mDb != null) {
            refreshDb();
        }

        synchronized (mLock) {
            return StorageUtils.removeDir(CACHE);
        }
    }

    public static byte[] getTile(int x, int y, int zoom, String tileProvider) {
        return getTile(MapUtils.getIndex(x, y, zoom), tileProvider);
    }

    public static byte[] getTile(long index, String tileProvider) {

        Cursor cur = getTileCursor(getPrimaryKeyParameters(index, tileProvider), queryColumns);

        byte[] bits = null;

        if (cur.moveToFirst()) {
            bits = cur.getBlob(cur.getColumnIndex(COLUMN_TILE));
        }
        cur.close();
        if (bits == null) {
            return null;
        }
        return bits;
    }


    private static final String primaryKey = COLUMN_KEY + "=? and " + COLUMN_PROVIDER + "=?";

    private static final String[] queryColumns = {COLUMN_TILE};

    private static String[] getPrimaryKeyParameters(long pIndex, String pTileSourceInfo) {
        return new String[]{String.valueOf(pIndex), pTileSourceInfo};
    }

    private static Cursor getTileCursor(String[] pPrimaryKeyParameters, String[] pColumns) {
        mDb = getDb();
        return mDb.query(TABLE, pColumns, primaryKey, pPrimaryKeyParameters, null, null, null);
    }

}




