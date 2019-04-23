package com.grsu.guideapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import com.grsu.guideapp.models.InfoAboutPoi;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Names;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.models.Route1;
import com.grsu.guideapp.project_settings.Settings;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper implements Table {

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


    public List<Route> getListRoutes(String locale) {
        List<Route> routesList = new ArrayList<>();
        openDatabase();

        String select = "select c1.id_route, c2.language_%s, c1.duration, c1.distance, c1.reference_photo_route, c1.southwest, c1.northeast, c1.is_full\n";
        String from = "from routes c1, routes_language c2 where c1.id_route=c2.id_route and c2.type = 1 order by c1.is_full desc";

        String query = String.format(select + from, locale);
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

    public Route1 getRoute(Integer id_route, String locale) {
        String select = "select type, language_%s\n";
        String from = "from routes_language\n";
        String where = "where id_route = %s order by type asc limit 4";

        String s = String.format(select + from + where, locale, id_route);
        Cursor cursor = getReadableDatabase().rawQuery(s, null);

        Names names = Names.fromCursor(cursor);

        //cursor.close();

        String query = String.format("SELECT * FROM routes WHERE id_route = %s", id_route);
        cursor = getReadableDatabase().rawQuery(query, null);

        Route1 route = Route1.fromCursor(cursor, names);
        cursor.close();

        return route;
    }

    public Route setDownload(Integer id_route) {
        ContentValues values = new ContentValues();
        values.put(Routes.is_full, 1);
        String[] args = {String.valueOf(id_route)};
        getWritableDatabase().update(Routes.table_name, values, "id_route = ?", args);
        return new Route();
    }

    public List<Line> getRouteById(Integer id_route) {
        List<Line> linesList = new ArrayList<>();
        openDatabase();

        String select = "SELECT c2.sequence, c1.start_point, c1.end_point, c1.polyline";
        String from = "FROM lines c1, list_lines c2";
        String where = "WHERE c1.id_line=c2.id_line and c2.id_route = %s ORDER BY sequence";
        String total = String.format(where, id_route);
        String query = String.format("%s %s %s", select, from, total);

        Cursor cursor = mDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            linesList.add(Line.fromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return linesList;
    }

    public List<Poi> getListPoi(Integer id, String position, int radius) {

        List<Poi> poiList = new ArrayList<>();
        openDatabase();
        String placeWithRadiusQuery = getPlaceWithRadius(id, position, radius);
        Cursor cursor = mDatabase.rawQuery(placeWithRadiusQuery, null);
        if (cursor.moveToFirst()) {
            do {
                poiList.add(Poi.fromCursor(cursor));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        closeDatabase();
        return poiList;
    }

    public InfoAboutPoi getInfoById(String id_point, String locale) {
        openDatabase();

        String miniSelect = "select c1.id_poi, c2.language_%s, c1.audio_reference, c1.photo_reference, c1.link, c1.last_update\n";
        String miniFrom = "from poi c1, types c2 where c1.id_type=c2.id_type and location = '%s' limit 1";
        String miniQuery = String.format(miniSelect + miniFrom, locale, id_point);

        String select = "select c1.type, c1.language_%s\n";
        String from = "from poi_language c1, (" + miniQuery + ") z1\n";
        String where = "where z1.id_poi = c1.id_poi order by c1.type asc limit 4";
        String query = String.format(select + from + where, locale, id_point);

        Cursor cursor = mDatabase.rawQuery(query, null);
        Names names = Names.fromCursor(cursor);

        cursor = mDatabase.rawQuery(miniQuery, null);

        InfoAboutPoi poi = null;
        if (cursor.moveToFirst()) {
            poi = InfoAboutPoi.fromCursor(cursor, names);
        }
        cursor.close();
        closeDatabase();
        return poi;
    }

    public Cursor getAllTypes(String locale) {
        openDatabase();
        String select = String.format("SELECT id_type as _id, is_checked, language_%s", locale);
        String query = select + " FROM types";
        return mDatabase.rawQuery(query, null);
    }

    public void changeRec(long pos, boolean isChecked) {
        ContentValues cv = new ContentValues();
        cv.put(Types.is_checked, (isChecked) ? 1 : 0);
        if (pos == 0) {
            getWritableDatabase().update(Types.name_table, cv, null, null);
            return;
        }
        getWritableDatabase().update(Types.name_table, cv, Types.id_type + " = " + pos, null);
    }

    public int getCountCheckedTypes() {
        int count = 0;
        openDatabase();
        Cursor cur = mDatabase.rawQuery(getByTypes(), null);
        if (cur != null) {
            count = cur.getCount();
            cur.close();
        }
        closeDatabase();
        return count;
    }

    public List<Integer> getListPoiFromBD(long id_route) {
        List<Integer> list = new ArrayList<>();
        String s = "SELECT distinct id_poi FROM list_poi WHERE id_route = %s";
        String query = String.format(s, id_route);
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getInt(0));
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    @NonNull
    private String getPlaceWithRadius(Integer id, String point, int radius) {
        String idRoute = String.format(" AND c1.id_route = %s", id);
        String join = point != null ? getByIdPoint(point) : "";

        String selectOne = "select c2.id_poi, c2.location, z1.icon_type from `list_poi` c1, `poi` c2";
        String selectTwo = ", (" + getByTypes() + ") z1 ";
        String where = "where c1.id_poi=c2.id_poi AND c2.id_type=z1.id_type" + join + idRoute;

        return String.format("%s%s%s%s", selectOne, selectTwo, where, getByRadius(radius));
    }

    @NonNull
    private String getByRadius(int radius) {
        return " AND (c1.distance <= " + radius + ")";
    }

    @NonNull
    private String getByTypes() {
        return "SELECT id_type, icon_type FROM types WHERE is_checked = 1";
    }

    @NonNull
    private String getByIdPoint(String point) {
        return " AND (c1.id_point = '" + point + "')";
    }
}
// Удалить все poi неимеющие связей в таблице list_poi
// DELETE FROM poi WHERE id_poi not IN (SELECT id_poi FROM list_poi WHERE id_poi is not null)

// Удалить все types неимеющие связей в таблице poi, кроме 0 записи
// DELETE FROM types WHERE id_type not IN (SELECT id_type FROM poi WHERE id_type is not null) and id_type <> 0

// Удалить все lines неимеющие связей в таблице list_lines
// DELETE FROM lines WHERE id_line not IN (SELECT id_line FROM list_lines WHERE id_line is not null)
