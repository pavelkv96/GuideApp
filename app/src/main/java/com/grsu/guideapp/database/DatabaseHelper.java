package com.grsu.guideapp.database;

import static android.database.sqlite.SQLiteDatabase.OPEN_READWRITE;
import static com.grsu.guideapp.utils.Crypto.encodeP;
import static com.grsu.guideapp.utils.constants.Constants.DB_NAME;
import static com.grsu.guideapp.utils.constants.Constants.ONE_METER_LAT;
import static com.grsu.guideapp.utils.constants.Constants.ONE_METER_LNG;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.constants.Constants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.util.GeoPoint;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private final Context mContext;
    private static final Integer VERSION = 1;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
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
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, OPEN_READWRITE);
    }

    private void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public static boolean copyDatabase(@NonNull Context context) {
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

    public static boolean deleteDatabase(@NonNull Context context) {
        File database = context.getDatabasePath(Constants.DB_NAME);

        if (database.exists()) {
            database.delete();
            return true;
        }

        return false;
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


    public List<Poi> getListPoi(double cLat, double cLng, int radius,
            List<Integer> typesObjects) {

        List<Poi> poiList = new ArrayList<>();
        openDatabase();
        String placeWithRadiusQuery = getPlaceWithRadius(cLat, cLng, radius, typesObjects);
        Cursor cursor = mDatabase.rawQuery(placeWithRadiusQuery, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            poiList.add(new Poi(
                    cursor.getString(0),
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
                "select c2.sequence, c1.start_point, c1.end_point, c1.polyline from lines c1, list_lines c2 where c1.id_line=c2.id_line and c2.id_route = ? order by sequence",
                //"select c1.* from lines c1, list_lines c2 where c1.id_line=c2.id_line and c2.id_route = ?",
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

    @NonNull
    private String getPlaceWithRadius(double cLatitude, double cLongitude, int radius,
            List<Integer> typesObjects) {
        double lat = radius * ONE_METER_LAT;//1m ~ 0.000009
        double lng = radius * ONE_METER_LNG;//1m ~ 0.000015

        String rightDownLan = String.valueOf(cLatitude - lat);
        String leftUpLan = String.valueOf(cLatitude + lat);
        String rightDownLng = String.valueOf(cLongitude - lng);
        String leftUpLng = String.valueOf(cLongitude + lng);

        return "select c2.* from `list_poi` c1, `poi` c2 where c1.id_poi=c2.id_poi AND  (c2.latitude BETWEEN "
                + rightDownLan
                + " AND " + leftUpLan + ") AND (c2.longitude BETWEEN " + rightDownLng + " AND "
                + leftUpLng + ")" + getByTypes(typesObjects) + d(cLatitude, cLongitude);
    }

    @NonNull
    private String getByTypes(List<Integer> typesObjects) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer integer : typesObjects) {
            stringBuilder.append(integer).append(",");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        return " AND type IN(" + stringBuilder + ")";
    }

    @NonNull
    private String d(double cLatitude, double cLongitude) {
        return " AND (c1.id_point = '" + encodeP(new GeoPoint(cLatitude, cLongitude)) + "')";
    }
}
