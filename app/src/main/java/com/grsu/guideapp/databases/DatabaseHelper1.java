package com.grsu.guideapp.databases;

import static com.grsu.guideapp.databases.DatabaseUtils.getByTypes;
import static com.grsu.guideapp.databases.DatabaseUtils.getPlace;
import static com.grsu.guideapp.databases.DatabaseUtils.getPlaceWithRadius;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.grsu.guideapp.models.InfoAboutPoi;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper1 extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper1.class.getSimpleName();
    private static final String DB_NAME = Settings.DATABASE_INFORMATION_NAME;
    private static SQLiteDatabase mDb;
    private String dbPath;

    public DatabaseHelper1(Context context) {
        super(context, DB_NAME, null, Settings.DATABASE_INFORMATION_VERSION);
        dbPath = context.getDatabasePath(DB_NAME).getPath();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private SQLiteDatabase getDB() {
        if (mDb != null && mDb.isOpen()) {
            return mDb;
        }

        synchronized (TAG) {
            if (mDb == null) {
                try {
                    mDb = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
                } catch (Exception ex) {
                    Logs.e(TAG, "Unable to start the sqlite tile writer.", ex);
                    return null;
                } finally {
                    disconnectDB();
                }
            }
        }

        return mDb;
    }

    private static void disconnectDB() {
        synchronized (TAG) {
            if (mDb != null) {
                mDb.close();
                mDb = null;
            }
        }
    }


    public List<Route> getListRoutes() {
        List<Route> routesList = new ArrayList<>();
        mDb = getDB();
        if (mDb != null) {
            Cursor cursor = mDb.rawQuery("SELECT * FROM Routes", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                routesList.add(Route.fromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
            disconnectDB();
            return routesList;
        }

        return null;
    }


    public List<Poi> getListPoi(double cLat, double cLng, int radius, long[] typesObjects) {
        mDb = getDB();
        if (mDb != null) {
            List<Poi> poiList = new ArrayList<>();
            String placeWithRadiusQuery = getPlaceWithRadius(cLat, cLng, radius, typesObjects);
            Cursor cursor = mDb.rawQuery(placeWithRadiusQuery, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                poiList.add(Poi.fromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
            disconnectDB();
            return poiList;
        }
        return null;
    }

    public List<Poi> getListPoi(Integer id, long[] typesObjects) {
        mDb = getDB();
        if (mDb != null) {
            List<Poi> poiList = new ArrayList<>();
            String placeWithRadiusQuery = getPlace(id) + getByTypes(typesObjects);
            Cursor cursor = mDb.rawQuery(placeWithRadiusQuery, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                poiList.add(Poi.fromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
            disconnectDB();
            return poiList;
        }
        return null;
    }


    public List<Line> getRouteById(Integer id_route) {
        mDb = getDB();
        if (mDb != null) {
            List<Line> linesList = new ArrayList<>();
            Cursor cursor = mDb.rawQuery(
                    "select c2.sequence, c1.start_point, c1.end_point, c1.polyline from lines c1, list_lines c2 where c1.id_line=c2.id_line and c2.id_route = ? order by sequence",
                    new String[]{String.valueOf(id_route)});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                linesList.add(Line.fromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
            disconnectDB();
            return linesList;
        }
        return null;
    }

    public InfoAboutPoi getInfoById(String id_point) {
        mDb = getDB();
        if (mDb != null) {
            String query = String
                    .format("select type, %s, short_description_point, audio_reference, photo_reference, link from poi c1, name_by_language c2 where c1.name_poi = c2.id_name and id_poi = ? limit 1",
                            "name_ru");

            Cursor cursor = mDb.rawQuery(query, new String[]{id_point});

            InfoAboutPoi poi = null;
            if (cursor.moveToFirst()) {
                poi = InfoAboutPoi.fromCursor(cursor);
            }
            cursor.close();
            disconnectDB();
            return poi;
        }
        return null;
    }

}
