package com.grsu.guideapp.databases;

import static com.grsu.guideapp.databases.TileConstants.COLUMN_KEY;
import static com.grsu.guideapp.databases.TileConstants.COLUMN_PROVIDER;
import static com.grsu.guideapp.databases.TileConstants.COLUMN_TILE;
import static com.grsu.guideapp.databases.TileConstants.CREATE_TABLE;
import static com.grsu.guideapp.databases.TileConstants.TABLE;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;
import com.grsu.guideapp.utils.StorageUtils;
import java.io.File;

public class CacheDBHelper extends SQLiteOpenHelper {

    private static final Object mLock = new Object();

    private static final String TAG = CacheDBHelper.class.getSimpleName();
    private static SQLiteDatabase mDb;

    private static final String primaryKey =
            COLUMN_KEY + "=? and " + COLUMN_PROVIDER + "=? limit 1";
    private static final String[] queryColumns = {COLUMN_TILE};

    public CacheDBHelper(Context context) {
        super(context, "cache.db", null, 1);
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
            new File(StorageUtils.getStorage() + "/osmdroid/cache/").mkdir();
            File db_file = new File(StorageUtils.getStorage() + "/osmdroid/cache/", "cache.db");

            if (mDb == null) {
                try {
                    mDb = SQLiteDatabase.openOrCreateDatabase(db_file, null);
                    mDb.execSQL(CREATE_TABLE);
                } catch (Exception ex) {
                    Log.e(TAG, "Unable to start the sqlite tile writer.", ex);
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

    public static byte[] getTile(long index, String tileProvider) {

        Cursor cur = getTileCursor(getPrimaryKeyParameters(index, tileProvider), queryColumns);
        byte[] bits = null;
        try {
            if (cur.moveToFirst()) {
                bits = cur.getBlob(cur.getColumnIndex(COLUMN_TILE));
            }
        } finally {
            cur.close();
        }

        return bits;
    }

    private static Cursor getTileCursor(String[] pPrimaryKeyParameters, String[] pColumns) {
        mDb = getDb();
        return mDb.query(TABLE, pColumns, primaryKey, pPrimaryKeyParameters, null, null, null);
    }

    private static String[] getPrimaryKeyParameters(long pIndex, String pTileSourceInfo) {
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
