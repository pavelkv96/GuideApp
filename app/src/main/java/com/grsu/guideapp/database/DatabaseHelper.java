package com.grsu.guideapp.database;

import android.content.ContentValues;
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


    public List<Route> getListRoutes(String locale) {
        List<Route> routesList = new ArrayList<>();
        openDatabase();
        String select = "select c1.id_route, c2.name_%s, c1.id_author, c1.duration, c1.distance, c1.short_description, c1.reference_photo_route, c1.southwest, c1.northeast\n";
        String from = "from routes c1, name_by_language c2 where c1.name_route=c2.id_name";
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
        String select ="select type, name_%s, short_description_point, audio_reference, photo_reference, link\n";
        String from = "from poi c1, name_by_language c2 where c1.name_poi = c2.id_name and id_poi = ? limit 1";
        String query = String.format(select + from, locale);

        Cursor cursor = mDatabase.rawQuery(query, new String[]{id_point});

        InfoAboutPoi poi = null;
        if (cursor.moveToFirst()) {
            poi = InfoAboutPoi.fromCursor(cursor);
        }
        cursor.close();
        closeDatabase();
        return poi;
    }

    public Cursor getAllTypes() {
        openDatabase();
        /*Cursor cur = mDatabase.rawQuery("SELECT id_type as _id, is_checked, name FROM types", null);
        MatrixCursor extras = new MatrixCursor(cur.getColumnNames());
        String[] newRow = {"0", "0", "Get all types"};
        extras.addRow(newRow);
        return new MergeCursor(new Cursor[]{extras, cur});*/
        //closeDatabase();
        return mDatabase.rawQuery("SELECT id_type as _id, is_checked, name FROM types", null);
    }

    public void changeRec(int pos, boolean isChecked) {
        ContentValues cv = new ContentValues();
        cv.put("is_checked", (isChecked) ? 1 : 0);
        if (pos == 0) {
            getWritableDatabase().update("types", cv, null, null);
            return;
        }
        getWritableDatabase().update("types", cv, "id_type" + " = " + (pos), null);
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


    @NonNull
    private String getPlaceWithRadius(Integer id, String point, int radius) {
        String idRoute = id != null ? getPointByIdRoute(id) : "";
        String join = point != null ? getByIdPoint(point) : " AND (c1.id_point = z2.end_point)";

        String selectOne = "select c2.id_poi, z1.name, z1.icon_type from `list_poi` c1, `poi` c2, (";
        String selectTwo = getByTypes() + ") z1 " + idRoute;
        String where = "where c1.id_poi=c2.id_poi AND c2.type=z1.id_type" + join;

        return String.format("%s%s%s%s", selectOne, selectTwo, where, getByRadius(radius));
    }

    @NonNull
    private String getByRadius(int radius) {
        return " AND (c1.distance <= " + radius + ")";
    }

    @NonNull
    private String getByTypes() {
        return "SELECT id_type, name, icon_type FROM types WHERE is_checked = 1";
    }

    @NonNull
    private String getByIdPoint(String point) {
        return " AND (c1.id_point = '" + point + "')";
    }

    @NonNull
    private String getPointByIdRoute(Integer id) {
        String query = ", (SELECT c2.end_point FROM list_lines c1, lines c2 WHERE c1.id_line=c2.id_line AND c1.id_route =";
        return String.format("%s %s) z2 ", query, id);
    }
}
