package com.grsu.guideapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import com.grsu.guideapp.models.InfoAboutPoi;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.project_settings.Settings;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private final Context mContext;
    private static final String DB_NAME = Settings.DATABASE_INFORMATION_NAME;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, Settings.DATABASE_INFORMATION_VERSION);
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
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    private void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }


    public List<Route> getListRoutes() {
        List<Route> routesList = new ArrayList<>();
        openDatabase();
        String query = String.format("select c1.id_route, c2.%s, c1.id_author, c1.duration, c1.distance, c1.short_description, c1.reference_photo_route\n"
                + "from routes c1, name_by_language c2 where c1.name_route=c2.id_name", "name_ru");
        Cursor cursor = mDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            routesList.add(Route.fromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return routesList;
    }

    public List<Line> getRouteById(Integer id_route) {
        List<Line> linesList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(
                "SELECT c2.sequence, c1.start_point, c1.end_point, c1.polyline, c1.audio_reference FROM lines c1, list_lines c2 WHERE c1.id_line=c2.id_line and c2.id_route = ? ORDER BY sequence",
                new String[]{String.valueOf(id_route)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            linesList.add(Line.fromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return linesList;
    }

    public List<Poi> getListPoi(String position, int radius, long[] typesObjects) {

        List<Poi> poiList = new ArrayList<>();
        openDatabase();
        String placeWithRadiusQuery = getPlaceWithRadius(position, radius, typesObjects);
        Cursor cursor = mDatabase.rawQuery(placeWithRadiusQuery, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            poiList.add(Poi.fromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return poiList;
    }

    public List<Poi> getListPoi(Integer id, int radius, long[] typesObjects) {

        List<Poi> poiList = new ArrayList<>();
        openDatabase();
        String placeWithRadiusQuery = getPlace(id) + getByRadius(radius) + getByTypes(typesObjects);
        Cursor cursor = mDatabase.rawQuery(placeWithRadiusQuery, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            poiList.add(Poi.fromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return poiList;
    }

    public InfoAboutPoi getInfoById(String id_point) {
        openDatabase();
        String query = String
                .format("select type, %s, short_description_point, audio_reference, photo_reference, link from poi c1, name_by_language c2 where c1.name_poi = c2.id_name and id_poi = ? limit 1",
                        "name_ru");

        Cursor cursor = mDatabase.rawQuery(query, new String[]{id_point});

        InfoAboutPoi poi = null;
        if (cursor.moveToFirst()) {
            poi = InfoAboutPoi.fromCursor(cursor);
        }
        cursor.close();
        closeDatabase();
        return poi;
    }

    @NonNull
    private String getPlaceWithRadius(String position, int radius, long[] typesObjects) {
        return getPlace() + getByRadius(radius) + getByTypes(typesObjects) + getByIdPoint(position);
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
    private String getByRadius(int radius) {
        return " AND (c1.distance <= " + radius + ")";
    }

    @NonNull
    private String getByTypes(long[] typesObjects) {
        StringBuilder stringBuilder = new StringBuilder();
        for (long l : typesObjects) {
            stringBuilder.append(l).append(",");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        return " AND type IN(" + stringBuilder + ")";
    }

    @NonNull
    private String getByIdPoint(String position) {
        return " AND (c1.id_point = '" + position + "')";
    }

    @NonNull
    private String getPointByIdRoute(Integer id) {
        return "SELECT c2.end_point FROM list_lines c1, lines c2 WHERE c1.id_line=c2.id_line"
                + " AND c1.id_route = " + id;
    }
}
