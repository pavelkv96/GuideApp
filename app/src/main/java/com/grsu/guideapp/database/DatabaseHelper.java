package com.grsu.guideapp.database;

import static android.database.sqlite.SQLiteDatabase.OPEN_READWRITE;
import static com.grsu.guideapp.project_settings.Settings.DATABASE_INFORMATION_NAME;
import static com.grsu.guideapp.project_settings.Settings.DATABASE_INFORMATION_VERSION;
import static com.grsu.guideapp.project_settings.Constants.ONE_METER_LAT;
import static com.grsu.guideapp.project_settings.Constants.ONE_METER_LNG;
import static com.grsu.guideapp.utils.CryptoUtils.encodeP;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private final Context mContext;
    private static final String DB_NAME = DATABASE_INFORMATION_NAME;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_INFORMATION_VERSION);
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
        File database = context.getDatabasePath(Settings.DATABASE_INFORMATION_NAME);

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

    public List<Poi> getListPoi(Integer id, List<Integer> typesObjects) {

        List<Poi> poiList = new ArrayList<>();
        openDatabase();
        String placeWithRadiusQuery = getPlace(id) + getByTypes(typesObjects);
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
        double lat = radius * ONE_METER_LAT;
        double lng = radius * ONE_METER_LNG;

        String rightDownLan = String.valueOf(cLatitude - lat);
        String leftUpLan = String.valueOf(cLatitude + lat);
        String rightDownLng = String.valueOf(cLongitude - lng);
        String leftUpLng = String.valueOf(cLongitude + lng);

        return getPlace() + getByRadius(rightDownLan, leftUpLan, rightDownLng, leftUpLng)
                + getByTypes(typesObjects) + getByIdPoint(cLatitude, cLongitude);
    }

    @NonNull
    private String getPlace(Integer id) {
        return "select c2.* from `list_poi` c1, `poi` c2, (" + getPointByIdRoute(id) + ") z1 " +
                "where c1.id_poi=c2.id_poi" + " AND (c1.id_point = z1.end_point)";
    }

    @NonNull
    private String getPlace() {
        return "select c2.* from `list_poi` c1, `poi` c2 where c1.id_poi=c2.id_poi";
    }

    @NonNull
    private String getByRadius(String rightDownLan, String leftUpLan, String rightDownLng,
            String leftUpLng) {
        return " AND (c2.latitude BETWEEN " + rightDownLan + " AND " + leftUpLan
                + ") AND (c2.longitude BETWEEN " + rightDownLng + " AND " + leftUpLng + ")";
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
    private String getByIdPoint(double cLatitude, double cLongitude) {
        return " AND (c1.id_point = '" + encodeP(new LatLng(cLatitude, cLongitude)) + "')";
    }

    @NonNull
    private String getPointByIdRoute(Integer id) {
        return "SELECT c2.end_point FROM list_lines c1, lines c2 WHERE c1.id_line=c2.id_line"
                + " AND c1.id_route = " + id;
    }
}
