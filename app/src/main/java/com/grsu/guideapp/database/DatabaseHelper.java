package com.grsu.guideapp.database;

import static com.grsu.guideapp.utils.Constants.DB_NAME;
import static com.grsu.guideapp.utils.Constants.ONE_METER_LAT;
import static com.grsu.guideapp.utils.Constants.ONE_METER_LNG;
import static com.grsu.guideapp.utils.Crypto.encodeP;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.util.GeoPoint;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private final Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void openDatabase() {
        String dbPath = mContext.getDatabasePath(DB_NAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    private void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public List<Route> getListRoutes() {
        List<Route> routesList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Routes", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            routesList.add(new Route(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getString(6)
            ));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return routesList;
    }

    public List<Poi> getListPoi(double cLatitude, double cLongitude, int radius) {
        return getListPoi(cLatitude, cLongitude, radius, null);
    }

    public List<Poi> getListPoi(double cLatitude, double cLongitude, int radius,
            List<Integer> typesObjects) {

        double lat = radius * ONE_METER_LAT;//1m ~ 0.000009
        double lng = radius * ONE_METER_LNG;//1m ~ 0.000015

        String rightDownLan = String.valueOf(cLatitude - lat);
        String leftUpLan = String.valueOf(cLatitude + lat);
        String rightDownLng = String.valueOf(cLongitude - lng);
        String leftUpLng = String.valueOf(cLongitude + lng);

        List<Poi> poiList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(
                d(/*rightDownLan, leftUpLan, rightDownLng, leftUpLng*/typesObjects), null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            poiList.add(new Poi(
                    cursor.getInt(0),
                    cursor.getFloat(1),
                    cursor.getFloat(2),
                    cursor.getInt(3)
            ));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return poiList;
    }

    public List<Line> getRouteById(Integer id_route) {
        List<Line> linesList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(
                "select c1.* from lines c1, list_lines c2 where c1.id_line=c2.id_line and c2.id_route = ?",
                new String[]{String.valueOf(id_route)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            linesList.add(new Line(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            ));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return linesList;
    }

    public List<Poi> getPoiById(GeoPoint geoPoint) {

        Logs.e(TAG, encodeP(geoPoint));
        List<Poi> poiList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(
                "select c2.* from list_poi c1, poi c2 where c1.id_poi=c2.id_poi and c1.id_point=?",
                new String[]{encodeP(geoPoint)}
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            poiList.add(new Poi(
                    cursor.getInt(0),
                    cursor.getFloat(1),
                    cursor.getFloat(2),
                    cursor.getInt(3)
            ));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return poiList;
    }


    public static boolean copyDatabase(Context context) {
        try {

            InputStream inputStream = context.getAssets().open(DB_NAME);
            String outFileName = context.getDatabasePath(DB_NAME).getPath();
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Logs.e(DatabaseHelper.class.getSimpleName(), "DB copied");
            return true;
        } catch (Exception e) {
            e.getMessage();
            Logs.e(DatabaseHelper.class.getSimpleName(), "DB don't copied");

            return false;
        }
    }

    private String d(String rightDownLan, String leftUpLan, String rightDownLng, String leftUpLng) {
        return "select*from `poi` where (Latitude BETWEEN " + rightDownLan + " AND " + leftUpLan
                + ") AND (Longitude BETWEEN " + rightDownLng + " AND " + leftUpLng + ")";
    }

    @NonNull
    private String d(List<Integer> typesObjects) {
        if (typesObjects != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Integer integer : typesObjects) {
                stringBuilder.append(integer).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return "select*from `poi`" + "where Type IN(" + stringBuilder + ")";
        }

        return "select*from `poi`";
    }
}
