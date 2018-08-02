package com.grsu.guideapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.grsu.guideapp.models.Routes;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Route;

/**
 * Created by NgocTri on 11/7/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "test.db";
    public static final String DBLOCATION = "/data/data/com.grsu.guideapp/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public List<Routes> getListProduct() {
        List<Routes> productList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Routes", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            productList.add(new Routes(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getString(6),
                    cursor.getString(7)
            ));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return productList;
    }
}
