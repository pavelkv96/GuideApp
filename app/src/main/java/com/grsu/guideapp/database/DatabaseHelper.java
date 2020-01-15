package com.grsu.guideapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;
import com.grsu.guideapp.models.DtoDetail;
import com.grsu.guideapp.models.DtoObject;
import com.grsu.guideapp.models.DtoRoute;
import com.grsu.guideapp.models.DtoType;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.project_settings.Settings;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

class DatabaseHelper extends SQLiteOpenHelper implements Table {

    public final Context mContext;
    private static final String DB_NAME = Settings.DATABASE_INFORMATION_NAME;
    private SQLiteDatabase mDatabase;

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, Settings.DATABASE_INFORMATION_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
        Timber.e("onOpen: ");
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

        String select = "select c1.id_route, c2.name,c2.description, c1.duration, c1.distance, c1.reference_photo_route, c1.southwest, c1.northeast, c1.is_full\n";
        String from = "from routes c1, routes_language c2 where c1.id_route=c2.id_route and c2.language = '%s' and c1.id_route<>1 order by c1.is_full desc";

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

    public List<Route> getListRoutes(String locale, boolean isAll) {
        List<Route> routesList = new ArrayList<>();
        openDatabase();

        String select = "select c1.id_route, c2.name, c2.description, c1.duration, c1.distance, c1.reference_photo_route, c1.southwest, c1.northeast, c1.is_full\n";
        String from = "from routes c1, routes_language c2\n";
        String where = "where c1.id_route=c2.id_route and c2.language = '%s' and c1.is_full<>0 order by c1.is_full desc";
        if (isAll){
            where = "where c1.id_route=c2.id_route and c2.language = '%s' order by c1.is_full desc";
        }

        String query = String.format(select + from + where, locale);
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

    public DtoRoute getRoute(Integer id_route, String locale) {
        String select = "SELECT c1.id_route, c2.name, c2.description, c1.duration, c1.distance, c1.reference_photo_route, c1.southwest, c1.northeast, c1.is_full\n";
        String from = "FROM routes c1, routes_language c2\n";
        String where = " WHERE c1.id_route = c2.id_route and c1.id_route= %s and c2.language = '%s' limit 1";

        String s = String.format(select + from + where, id_route, locale);
        Cursor cursor = getReadableDatabase().rawQuery(s, null);

        if (cursor != null) {
            cursor.moveToFirst();
            DtoRoute route = DtoRoute.fromCursor(cursor);
            cursor.close();
            return route;
        }

        return null;
    }

    public void setDownload(Integer id_route, int is_full) {
        ContentValues values = new ContentValues();
        values.put(Routes.is_full, is_full);
        String[] args = {String.valueOf(id_route)};
        getWritableDatabase().update(Routes.table_name, values, "id_route = ?", args);
        //return new Route();
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

    public DtoDetail getInfoById(int id_poi, String locale) {
        openDatabase();

        String select = "select c2.name, c2.description, c1.photo_reference, c1.link, c1.address, c1.email, c1.phone\n";
        String from = "from poi c1, poi_language c2\n";
        String where = "where c1.id_poi = c2.id_poi and c2.language = '%s' and c1.id_poi = %s limit 1";
        String query = String.format(select + from + where, locale, id_poi);

        Cursor cursor = mDatabase.rawQuery(query, null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            DtoDetail detail = DtoDetail.fromCursor(cursor);
            cursor.close();
            return detail;
        }
        closeDatabase();
        return null;
    }

    public Cursor getAllTypes(String locale) {
        openDatabase();
        String select = String.format("SELECT id_type as _id, is_checked, language_%s", locale);
        String from = String.format(" FROM types WHERE not ifnull(language_%s, '') = ''", locale);
        String query = select + from;
        return mDatabase.rawQuery(query, null);
    }

    public List<DtoType> getAllType(String locale) {
        Cursor cursor = getAllTypes(locale);
        List<DtoType> types = DtoType.fromCursor(cursor);
        closeDatabase();
        return types;
    }

    public List<DtoObject> getAllObjectFromType(String locale, int type){
        openDatabase();
        String select = "SELECT distinct c1.id_poi, c2.name, c1.photo_reference";
        String from = " FROM poi c1, poi_language c2";
        String where = " WHERE c1.id_poi = c2.id_poi AND c2.language = '%s' AND c1.id_type = %s ORDER BY c1.number ASC";
        String query = String.format(select + from + where, locale, type);
        Cursor cursor = mDatabase.rawQuery(query, null);
        List<DtoObject> objects = DtoObject.fromCursor(cursor);
        closeDatabase();
        return objects;
    }

    public List<DtoObject> getObjectById(int id, String locale){
        openDatabase();
        String select = "SELECT distinct c1.id_poi, c2.name, c1.photo_reference";
        String from = " FROM poi c1, poi_language c2";
        String where = " WHERE c1.id_poi = c2.id_poi AND c2.language = '%s' AND c1.id_poi = %s limit 1";
        String query = String.format(select + from + where, locale, id);
        Cursor cursor = mDatabase.rawQuery(query, null);
        List<DtoObject> objects = DtoObject.fromCursor(cursor);
        closeDatabase();
        return objects;
    }

    public void changeRec(long pos, boolean isChecked) {
        ContentValues cv = new ContentValues();
        cv.put(Types.is_checked, (isChecked) ? 1 : 0);
        if (pos == 0) {
            getWritableDatabase().update(Types.name_table, cv, null, null);
        } else {
            int all = getAllCountTypes();
            getWritableDatabase().update(Types.name_table, cv, Types.id_type + " = " + pos, null);
            int checked = getCountTypes();

            ContentValues cv1 = new ContentValues();
            if (all == checked) {
                cv1.put(Types.is_checked, 1);
                getWritableDatabase()
                        .update(Types.name_table, cv1, Types.id_type + " = " + 0, null);
            } else {
                cv1.put(Types.is_checked, 0);
                getWritableDatabase()
                        .update(Types.name_table, cv1, Types.id_type + " = " + 0, null);
            }
        }
    }

    private int getCountTypes() {
        int count = 0;
        //openDatabase();
        Cursor cur = mDatabase.rawQuery("SELECT id_type FROM types WHERE is_checked = 1 AND id_type <> 0", null);
        if (cur != null) {
            count = cur.getCount();
            cur.close();
        }
        //closeDatabase();
        return count;
    }

    private int getAllCountTypes() {
        int count = 0;
        //openDatabase();
        Cursor cur = mDatabase.rawQuery("SELECT id_type FROM types WHERE id_type <> 0", null);
        if (cur != null) {
            count = cur.getCount();
            cur.close();
        }
        //closeDatabase();
        return count;
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

        String selectOne = "select c2.id_poi, c2.location, c2.icon_type from `list_poi` c1, `poi` c2";
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
        return "SELECT id_type FROM types WHERE is_checked = 1";
    }

    @NonNull
    private String getByIdPoint(String point) {
        return " AND (c1.id_point = '" + point + "')";
    }

    void clearRouteById(long id) {
        //String query = String.format("DELETE FROM routes WHERE id_route = %s", id);
        getWritableDatabase().delete(Routes.table_name, "id_route = ?", new String[]{String.valueOf(id)});
    }

    void clearPoi() {
        String query = "DELETE FROM poi WHERE id_poi not IN (SELECT id_poi FROM list_poi WHERE id_poi is not null)";
        getWritableDatabase().execSQL(query);
    }

    void clearTypes() {
        String query = "DELETE FROM types WHERE id_type not IN (SELECT id_type FROM poi WHERE id_type is not null) and id_type <> 0";
        getWritableDatabase().execSQL(query);
    }

    void clearLines() {
        String query = "DELETE FROM lines WHERE id_line not IN (SELECT id_line FROM list_lines WHERE id_line is not null)";
        getWritableDatabase().execSQL(query);
    }
}
// Удалить все poi неимеющие связей в таблице list_poi
// DELETE FROM poi WHERE id_poi not IN (SELECT id_poi FROM list_poi WHERE id_poi is not null)

// Удалить все types неимеющие связей в таблице poi, кроме 0 записи
// DELETE FROM types WHERE id_type not IN (SELECT id_type FROM poi WHERE id_type is not null) and id_type <> 0

// Удалить все lines неимеющие связей в таблице list_lines
// DELETE FROM lines WHERE id_line not IN (SELECT id_line FROM list_lines WHERE id_line is not null)
