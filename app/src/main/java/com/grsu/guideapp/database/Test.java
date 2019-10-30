package com.grsu.guideapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import com.grsu.guideapp.App;
import com.grsu.guideapp.BuildConfig;
import com.grsu.guideapp.adapters.SaveAdapter;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.base.listeners.OnProgressListener;
import com.grsu.guideapp.network.APIService;
import com.grsu.guideapp.network.model.About;
import com.grsu.guideapp.network.model.Category;
import com.grsu.guideapp.network.model.Data;
import com.grsu.guideapp.network.model.Datum;
import com.grsu.guideapp.network.model.Image;
import com.grsu.guideapp.network.model.Name;
import com.grsu.guideapp.network.model.Objects;
import com.grsu.guideapp.network.model.Timestamp;
import com.grsu.guideapp.network.model.Turn;
import com.grsu.guideapp.network.model.Value;
import com.grsu.guideapp.project_settings.Constants.Language;
import com.grsu.guideapp.utils.Converter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

                            insertRoute(datum, polylinesIds);
                            String url = datum.getData().getImages().get(0).getHref();
                            insertTypesAndPoi(value.getObjects());
                            int id_route = datum.getId();
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

    private void insertRoute(Datum datum, List<Long> polylinesIds) {
        Data route = datum.getData();
        int id = datum.getId();
        Date date = datum.getTimestamp().getUpdatedAt();

        String s = "SELECT %s FROM %s WHERE %s = %s";
        String query = String.format(s, Routes.last_download, Routes.table_name, Routes.id_route, id);

        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        long last_update;
        Log.e(TAG, "insertRoute: " + id + "   " + cursor.moveToFirst());
        if (cursor.moveToFirst()) {
            last_update = cursor.getLong(0);
            cursor.close();

            if (last_update < date.getTime() / 1000) {
                updateRoute(route, id, polylinesIds);
            }
        } else {
            ContentValues values = Content.insertRoute(route, id, date.getTime() / 1000);
            getWritableDatabase().insert(Routes.table_name, null, values);
            List<ContentValues> valuesList = Content
                    .insertLanguage(route.getName(), route.getAbout(), id,
                            RoutesLanguage.id_route);
            for (ContentValues value : valuesList) {
                getWritableDatabase().insert(RoutesLanguage.name_table, null, value);
            }
            insertListLines(polylinesIds, id);
        }
    }

    private void updateRoute(Data route, int id, List<Long> polylinesIds) {
        ContentValues values = Content.updateRoute(route, id);
        getWritableDatabase().update(Routes.table_name, values, "id_route = ?",
                new String[]{String.valueOf(id)});

        List<ContentValues> valuesList = Content
                .insertLanguage(route.getName(), route.getAbout(), id, RoutesLanguage.id_route);

        for (int i = 0; i < valuesList.size(); i++) {
            String clause = "id_route = ? and language = ?";
            String[] args = {String.valueOf(id), Language.values()[i].name()};
            getWritableDatabase()
                    .update(RoutesLanguage.name_table, valuesList.get(i), clause, args);
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

            ContentValues type = new ContentValues();
            Integer id = object.getCategories();
            type.put(Types.id_type, id);
            type.put(Types.is_checked, 1);
            long insert = getWritableDatabase().insertWithOnConflict(Types.name_table, null, type, 4);
            if (insert==-1) {
                getWritableDatabase().update(Types.name_table, type, "id_type = ?", new String[]{String.valueOf(id)});
            }

            byte[] icon = SaveAdapter.saveIcon(this, object.getImage());
            ContentValues values = new ContentValues();
            values.put(POI.id_poi, object.getId());
            values.put(POI.location, object.getCryptoLatLng());
            values.put(POI.id_type, id);
            values.put(POI.icon_type, icon);
            values.put(POI.last_download, new Date().getTime() / 1000);


            long row = getWritableDatabase().insertWithOnConflict(POI.name_table, null, values, 4);
            if (row == -1) {
                String[] args = {String.valueOf(object.getId())};
                getWritableDatabase().update(POI.name_table, values, /*POI.id_poi + */"id_poi = ?", args);
            }
        }
    }

    private void insertPoiList(List<Turn> polylines, long id_route, List<Objects> objects) {

        int start = 70;
        int end = 1000;

        for (Objects object : objects) {
            ContentValues values = new ContentValues();
            values.put(ListPoi.id_route, id_route);
            values.put(ListPoi.id_point, "");
            values.put(ListPoi.id_poi, object.getId());

            values.put(ListPoi.distance, start + (Math.random() * (end - start)));

            getWritableDatabase().insert(ListPoi.name_table, null, values);
        }
        /*Set<Point> set = new ArraySet<>();

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
        }*/
    }


    //INSERT POI
    public void insertPoiAndTypes(Datum datum) {
        if (datum.getData() == null){
            return;
        }
        Date date = datum.getTimestamp().getUpdatedAt();

        Name name = datum.getData().getName();
        About about = datum.getData().getAbout();

        List<ContentValues> list = Content
                .insertLanguage(name, about, datum.getId(), PoiLanguage.id_poi);
        for (ContentValues values : list) {
            getWritableDatabase().insert(PoiLanguage.name_table, null, values);
        }

        ContentValues values1 = new ContentValues();
        List<Image> images = datum.getData().getImages();
        if (!images.isEmpty()) {
            String href = images.get(0).getHref();
            values1.put(POI.photo_reference, href);
            SaveAdapter.saveImage(href);
        }

        List<Category> categoryList = datum.getData().getCategory();
        if (categoryList == null || categoryList.isEmpty()) {
            String ru = name.getFull().getRu();
            throw new NullPointerException("getCategory: " + datum.getId() + "   " + ru);
        }

        Category category = categoryList.get(0);
        ContentValues values = new ContentValues();
        values.put(Types.id_type, category.getId());
        values.put(Types.language_ru, category.getName().getRu());
        values.put(Types.language_en, category.getName().getEn());
        values.put(Types.language_cn, category.getName().getCn());
        values.put(Types.language_pl, category.getName().getPl());
        values.put(Types.language_lt, category.getName().getLt());
        String[] id = {String.valueOf(category.getId())};
        if (getWritableDatabase().insertWithOnConflict(Types.name_table, null, values, 4) == -1) {
            getWritableDatabase().update(Types.name_table, values, Types.id_type + " = ?", id);
        }

        values1.put(POI.id_poi, datum.getId());
        values1.put(POI.id_type, category.getId());
        values1.put(POI.number, category.getNumber());
        values1.put(POI.address, datum.getData().getAddress());
        values1.put(POI.email, datum.getData().getEmail());
        values1.put(POI.link, datum.getData().getHref());
        values1.put(POI.phone, datum.getData().getPhone());
        values1.put(POI.last_update, date.getTime() / 1000);
        id = new String[]{String.valueOf(datum.getId())};
        if (getWritableDatabase().insertWithOnConflict(POI.name_table, null, values1, 4) == -1) {
            getWritableDatabase().update(POI.name_table, values1, POI.id_poi + " = ?", id);
        }
    }

    public void insertPoiAndTypesTransaction(SQLiteDatabase database, @Nullable Datum datum) {
        if (datum == null || datum.getData() == null) {
            return;
        }

        Date date = datum.getTimestamp().getUpdatedAt();

        Name name = datum.getData().getName();
        About about = datum.getData().getAbout();

        List<ContentValues> list = Content
                .insertLanguage(name, about, datum.getId(), PoiLanguage.id_poi);
        for (ContentValues values : list) {
            database.insert(PoiLanguage.name_table, null, values);
        }

        ContentValues values1 = new ContentValues();
        List<Image> images = datum.getData().getImages();
        if (!images.isEmpty()) {
            String href = images.get(0).getHref();
            values1.put(POI.photo_reference, href);
            SaveAdapter.saveImage(href);
        }

        List<Category> categoryList = datum.getData().getCategory();
        if (categoryList == null || categoryList.isEmpty()) {
            String ru = name.getFull().getRu();
            throw new NullPointerException("getCategory: " + datum.getId() + "   " + ru);
        }

        Category category = categoryList.get(0);
        ContentValues values = new ContentValues();
        values.put(Types.id_type, category.getId());
        values.put(Types.language_ru, category.getName().getRu());
        values.put(Types.language_en, category.getName().getEn());
        values.put(Types.language_cn, category.getName().getCn());
        values.put(Types.language_pl, category.getName().getPl());
        values.put(Types.language_lt, category.getName().getLt());
        String[] id = {String.valueOf(category.getId())};
        if (database.insertWithOnConflict(Types.name_table, null, values, 4) == -1) {
            database.update(Types.name_table, values, Types.id_type + " = ?", id);
        }

        values1.put(POI.id_poi, datum.getId());
        values1.put(POI.id_type, category.getId());
        values1.put(POI.number, category.getNumber());
        values1.put(POI.address, datum.getData().getAddress());
        values1.put(POI.email, datum.getData().getEmail());
        values1.put(POI.link, datum.getData().getHref());
        values1.put(POI.phone, datum.getData().getPhone());
        values1.put(POI.last_update, date.getTime() / 1000);
        id = new String[] {String.valueOf(datum.getId())};
        database.update(POI.name_table, values1, POI.id_poi + " = ?", id);


    }

    public String getLastCheck() {
        String query = "SELECT min(last_update) FROM routes where last_update > 0";

        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            long date = cursor.getLong(0);
            if (date != 0) {
                long time = new Date(date).getTime() * 1000;
                cursor.close();
                return Converter.toStringDate(time);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private void haveUpdate(int id, Timestamp timestamp, int is_full) {
        ContentValues values = new ContentValues();
        values.put(Routes.is_full, is_full);
        String[] args = {String.valueOf(id), String.valueOf(timestamp.getUpdatedAt().getTime()/1000)};
        String whereClause = "id_route = ? and is_full = 2 and last_update < ?";
        getWritableDatabase().updateWithOnConflict(Routes.table_name, values, whereClause, args, 5);
        //return new Route();
    }

    public void setHaveUpdate(final SparseArray<Timestamp> updateIds) {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < updateIds.size(); i++) {
                    int key = updateIds.keyAt(i);
                    haveUpdate(key, updateIds.get(key), Table.HAVE_UPDATE);
                }
            }
        });
    }

    public void updateFullRouteById(final OnProgressListener<String> listener, final long id) {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                try {
                    APIService network = App.getThread().networkIO();
                    Datum routeData = network.getRouteById(id, BuildConfig.ApiKey).execute().body();
                    if (routeData==null){
                        throw new NullPointerException("Null data for route");
                    }

                    List<Datum> list = network.updatePoi(id, BuildConfig.ApiKey).execute().body();
                    if (list == null || list.isEmpty()){
                        throw new NullPointerException("Null data for points");
                    }
                    float currentProgress = 0;
                    float partProgress = (float) 100 / (list.size() + 1);
                    listener.onProgress((int) currentProgress);
                    clearRouteById(id);
                    clearPoi();
                    clearLines();
                    clearTypes();

                    listener.onProgress((int) (currentProgress += partProgress));

                    Data data = routeData.getData();
                    Value value = data.getRoute().getValue();
                    List<Turn> turns = value.getPoints();
                    List<Long> polylinesIds = insertPolyline(turns);

                    int id_route = routeData.getId();
                    insertRoute(routeData, polylinesIds);
                    String url = routeData.getData().getImages().get(0).getHref();
                    insertTypesAndPoi(value.getObjects());
                    insertPoiList(turns, id_route, value.getObjects());
                    SaveAdapter.saveImage(url);

                    for (Datum poi : list) {
                        if (poi != null) {
                            if (poi.getActive()) {
                                insertPoiAndTypes(poi);
                                listener.onProgress((int) (currentProgress += partProgress));
                            }
                        }
                    }

                    setDownload((int) id, Table.DOWNLOAD);
                    listener.onSuccess("ОK");
                } catch (Exception e) {
                    listener.onFailure(e);
                }
                /*try {
                    APIService networkIO = App.getThread().networkIO();
                    Datum routeData = networkIO.getRouteById(id, BuildConfig.ApiKey).execute().body();
                    List<Datum> listObjects = networkIO.updatePoi(id, BuildConfig.ApiKey).execute().body();
                    if (routeData != null && listObjects != null && !listObjects.isEmpty()) {
                        SQLiteDatabase database = getWritableDatabase();
                        try {
                            database.beginTransaction();

                            clearRouteById(database, id);
                            clearPoi(database);
                            clearLines(database);
                            clearTypes(database);

                            Data data = routeData.getData();
                            Value value = data.getRoute().getValue();
                            List<Turn> turns = value.getPoints();

                            List<Long> polylinesIds = insertPolyline(turns);

                            int id_route = routeData.getId();

                            insertRoute(data, id_route, polylinesIds);

                            String url = routeData.getData().getImages().get(0).getHref();

                            insertTypesAndPoi(value.getObjects());
                            insertPoiList(turns, id_route, value.getObjects());

                            SaveAdapter.saveImage(url);

                            for (Datum poi : listObjects) {
                                if (poi != null) {
                                    insertPoiAndTypes(poi);
                                }
                            }

                            setDownload((int) id, Table.DOWNLOAD);

                            database.setTransactionSuccessful();
                        } finally {
                            database.endTransaction();
                        }

                        listener.onSuccess("ОK");
                    }
                } catch (Exception e) {
                    listener.onFailure(e);
                }*/
            }
        });
    }
}
