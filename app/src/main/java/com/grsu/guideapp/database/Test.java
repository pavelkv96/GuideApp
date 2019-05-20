package com.grsu.guideapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.ArraySet;
import com.google.android.gms.maps.model.LatLng;
import com.grsu.guideapp.App;
import com.grsu.guideapp.BuildConfig;
import com.grsu.guideapp.adapters.SaveAdapter;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.network.APIService;
import com.grsu.guideapp.network.model.About;
import com.grsu.guideapp.network.model.Category;
import com.grsu.guideapp.network.model.Data;
import com.grsu.guideapp.network.model.Datum;
import com.grsu.guideapp.network.model.Name;
import com.grsu.guideapp.network.model.Objects;
import com.grsu.guideapp.network.model.Point;
import com.grsu.guideapp.network.model.Turn;
import com.grsu.guideapp.network.model.Value;
import com.grsu.guideapp.project_settings.Constants.Language;
import com.grsu.guideapp.utils.MapUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Test extends DatabaseHelper {

    private static final String TAG = Test.class.getSimpleName();

    public Test(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void loadRoute(final OnFinishedListener<Integer> listener, final List<Datum> list) {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                for (Datum datum : list) {
                    Data data = datum.getData();
                    if (data != null) {
                        if (data.getRoute() != null) {
                            Value value = data.getRoute().getValue();
                            List<Turn> turns = value.getPoints();
                            List<Long> polylinesIds = insertPolyline(turns);

                            int id_route = datum.getId();
                            insertRoute(data, id_route, polylinesIds);
                            String url = datum.getData().getImages().get(0).getHref();
                            insertTypesAndPoi(value.getObjects());
                            insertPoiList(turns, id_route, value.getObjects());
                            SaveAdapter.saveImage(url);
                        }
                    }
                }
                listener.onFinished(android.R.string.ok);
            }
        });
    }

    private void updateListLines(List<Long> polylinesIds, long id_route) {
        getWritableDatabase().delete(ListLines.table_name, "id_route = ?", new String[]{
                String.valueOf(id_route)});

        insertListLines(polylinesIds, id_route);
    }

    private void insertListLines(List<Long> polylinesIds, long id_route) {
        for (int i = 0; i < polylinesIds.size(); i++) {
            ContentValues values = Content.insertListLines(polylinesIds, id_route, i);
            getWritableDatabase().insert(ListLines.table_name, null, values);
        }
    }

    private void insertRoute(Data route, int id, List<Long> polylinesIds) {

        Date date = new Date();

        String s = "SELECT %s FROM %s WHERE %s = %s";
        String query = String.format(s, Routes.last_download, Routes.table_name, Routes.id_route, id);

        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        long last_update;
        if (cursor.moveToFirst()) {
            last_update = cursor.getLong(0);
            cursor.close();

            if (last_update < date.getTime() / 1000) {
                updateRoute(route, id, date.getTime() / 1000, polylinesIds);
            }
        } else {
            ContentValues values = Content.insertRoute(route, id, date.getTime() / 1000);
            getWritableDatabase().insert(Routes.table_name, null, values);
            List<ContentValues> valuesList = Content
                    .insertRouteLanguage(route.getName(), route.getAbout(), id,
                            RoutesLanguage.id_route);
            for (ContentValues value : valuesList) {
                getWritableDatabase().insert(RoutesLanguage.name_table, null, value);
            }
            insertListLines(polylinesIds, id);
        }
    }

    private void updateRoute(Data route, int id, long date, List<Long> polylinesIds) {
        ContentValues values = Content.insertRoute(route, id, date);
        getWritableDatabase().update(Routes.table_name, values, "id_route = ?", new String[]{String.valueOf(id)});
        String query = "UPDATE routes SET is_full = CASE WHEN is_full < 1 THEN 0 ELSE 2 END WHERE id_route = %s";

        getWritableDatabase().execSQL(String.format(query, id));

        List<ContentValues> valuesList = Content
                .insertRouteLanguage(route.getName(), route.getAbout(), id,
                        RoutesLanguage.id_route);

        for (int i = 0; i < valuesList.size(); i++) {
            String clause = "id_route = ? and language = ?";
            String[] args = {String.valueOf(id), Language.values()[i].name()};
            getWritableDatabase().update(RoutesLanguage.name_table, valuesList.get(i), clause, args);
        }
        updateListLines(polylinesIds, id);
    }

    private List<Long> insertPolyline(List<Turn> polylines) {
        String query = "SELECT id_line FROM lines WHERE polyline = \'%s\'";
        List<Long> listId = new ArrayList<>();

        for (Turn polyline : polylines) {
            ContentValues values = Content.insertPolyline(polyline);
            long id = getWritableDatabase().insertWithOnConflict(Lines.table_name, null, values, 4);
            if (id == -1) {
                String queryId = String.format(query, polyline.getPolyline());
                Cursor cursor = getWritableDatabase().rawQuery(queryId, null);
                cursor.moveToFirst();
                id = cursor.getLong(0);
                cursor.close();
            }
            listId.add(id);
        }

        return listId;
    }

    private void insertTypesAndPoi(List<Objects> objects) {
        for (Objects object : objects) {
            SaveAdapter.saveIcon(this, object.getImage(), object.getCategories());
            ContentValues values = new ContentValues();
            values.put(POI.id_poi, object.getId());
            values.put(POI.location, object.getCryptoLatLng());
            values.put(POI.id_type, object.getCategories());
            values.putNull(POI.audio_reference);
            values.putNull(POI.photo_reference);
            values.putNull(POI.link);
            values.put(POI.last_download, new Date().getTime() / 1000);

            getWritableDatabase().insertWithOnConflict(POI.name_table, null, values, 5);
        }
    }

    private void insertPoiList(List<Turn> polylines, long id_route, List<Objects> objects) {

        Set<Point> set = new ArraySet<>();

        for (Turn turn : polylines) {
            Point start = turn.getStart();
            if (start.getObjects() != null) {
                set.add(start);

            }
        }

        Point end = polylines.get(polylines.size() - 1).getEnd();
        if (end.getObjects() != null) {
            set.add(end);
        }

        for (Point point : set) {
            for (Integer id : point.getObjects()) {
                ContentValues values = new ContentValues();
                values.put(ListPoi.id_route, id_route);
                values.put(ListPoi.id_point, point.getCryptoLatLng());
                values.put(ListPoi.id_poi, id);

                int index = objects.indexOf(new Objects(id));
                LatLng from = point.getLatLng();
                LatLng to = objects.get(index).getLatLng();

                values.put(ListPoi.distance, (int) MapUtils.distanceTo(from, to));

                getWritableDatabase().insert(ListPoi.name_table, null, values);
            }
        }
    }


    //INSERT POI
    public void insertPoiAndTypes(Datum datum) {

        String updatedAt = datum.getTimestamp().getUpdatedAt();
        Date date;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss", Locale.US).parse(updatedAt);
        } catch (ParseException e) {
            date = new Date(0);
        }

        Name name = datum.getData().getName();
        About about = datum.getData().getAbout();

        List<ContentValues> list = Content
                .insertRouteLanguage(name, about, datum.getId(), PoiLanguage.id_poi);
        for (ContentValues values : list) {
            getWritableDatabase().insert(PoiLanguage.name_table, null, values);
        }

        String href = datum.getData().getImages().get(0).getHref();
        ContentValues values1 = new ContentValues();
        values1.put(POI.id_poi, datum.getId());
        values1.put(POI.photo_reference, href);
        values1.put(POI.last_update, date.getTime() / 1000);
        String[] id = {String.valueOf(datum.getId())};
        getWritableDatabase().update(POI.name_table, values1, POI.id_poi + " = ?", id);
        SaveAdapter.saveImage(href);


        Category category = datum.getData().getCategory();
        ContentValues values = new ContentValues();
        values.put(Types.id_type, category.getId());
        values.put(Types.language_ru, category.getName().getRu());
        values.put(Types.language_en, category.getName().getEn());
        values.put(Types.language_cn, category.getName().getCn());
        values.put(Types.language_pl, category.getName().getPl());
        values.put(Types.language_lt, category.getName().getLt());
        id = new String[]{String.valueOf(category.getId())};
        getWritableDatabase().update(Types.name_table, values, Types.id_type + " = ?", id);
    }

    public String getLastCheck() {
        String query = "SELECT min(last_download) FROM routes where last_download > 0";
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            long date = cursor.getLong(0);
            if (date != 0) {
                long time = new Date(date).getTime() * 1000;
                cursor.close();
                String pattern = "dd-MM-yyyy_HH:mm:ss";
                return new SimpleDateFormat(pattern, Locale.US).format(time);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private void haveUpdate(Integer id_route, int is_full) {
        ContentValues values = new ContentValues();
        values.put(Routes.is_full, is_full);
        String[] args = {String.valueOf(id_route)};
        String whereClause = "id_route = ? and is_full = 2";
        getWritableDatabase().updateWithOnConflict(Routes.table_name, values, whereClause, args, 5);
        //return new Route();
    }

    public void setHaveUpdate(List<Integer> updateIds) {
        for (Integer id : updateIds) {
            haveUpdate(id, Table.HAVE_UPDATE);
        }
    }

    public void updateFullRouteById(final OnSuccessListener<String> listener, final long id) {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                try {
                    APIService networkIO = App.getThread().networkIO();
                    Datum datum = networkIO.getRouteById(id, BuildConfig.ApiKey).execute().body();
                    if (datum != null) {
                        clearRouteById((int)id);
                        clearPoi();
                        clearLines();
                        clearTypes();

                        Data data = datum.getData();
                        Value value = data.getRoute().getValue();
                        List<Turn> turns = value.getPoints();
                        List<Long> polylinesIds = insertPolyline(turns);

                        int id_route = datum.getId();
                        insertRoute(data, id_route, polylinesIds);
                        String url = datum.getData().getImages().get(0).getHref();
                        insertTypesAndPoi(value.getObjects());
                        insertPoiList(turns, id_route, value.getObjects());
                        SaveAdapter.saveImage(url);

                        List<Integer> ids = getListPoiFromBD(id);
                        for (Integer poiId : ids) {

                            Datum body = networkIO.getPoi(poiId, BuildConfig.ApiKey).execute().body();
                            if (body != null) {
                                insertPoiAndTypes(body);
                            }
                        }


                        setDownload((int) id, Table.DOWNLOAD);
                        listener.onSuccess("Океюшки");
                    }
                } catch (Exception e) {
                    listener.onFailure(e);
                }
            }
        });
    }
}
