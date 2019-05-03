package com.grsu.guideapp.database;

import android.content.ContentValues;
import com.grsu.guideapp.database.Table.Lines;
import com.grsu.guideapp.database.Table.ListLines;
import com.grsu.guideapp.database.Table.Routes;
import com.grsu.guideapp.database.Table.RoutesLanguage;
import com.grsu.guideapp.network.model.About;
import com.grsu.guideapp.network.model.Data;
import com.grsu.guideapp.network.model.Name;
import com.grsu.guideapp.network.model.Turn;
import com.grsu.guideapp.network.model.Value;
import com.grsu.guideapp.utils.CryptoUtils;
import java.util.ArrayList;
import java.util.List;

class Content {

    static ContentValues insertListLines(List<Long> polylinesIds, long id_route, int sequence) {
        ContentValues values = new ContentValues();
        values.put(ListLines.id_route, id_route);
        values.put(ListLines.id_line, polylinesIds.get(sequence));
        values.put(ListLines.sequence, sequence + 1);
        return values;
    }

    static ContentValues insertRoute(Data route, int id, long date) {
        Value value = route.getRoute().getValue();
        ContentValues values = new ContentValues();
        //values.put(Routes.short_name, -1);
        //values.put("name_route", -1);
        //values.put("id_author", -1);
        //values.put(Routes.full_name, -1);
        values.put(Routes.id_route, id);
        values.put(Routes.duration, 0);
        values.put(Routes.distance, 0);
        //values.put(Routes.short_description, -1);
        //values.put(Routes.full_description, -1);
        values.put(Routes.reference_photo_route, route.getImages().get(0).getHref());
        values.put(Routes.southwest, CryptoUtils.encodeP(value.getLimLeft().getLatLng()));
        values.put(Routes.northeast, CryptoUtils.encodeP(value.getLimRight().getLatLng()));
        values.put(Routes.last_update, date);
        values.put(Routes.last_download, System.currentTimeMillis() / 1000);
        return values;
    }

    static List<ContentValues> insertRouteLanguage(Name name, About about, int id, String table_key) {
        List<ContentValues> valuesList = new ArrayList<>();

        ContentValues values = new ContentValues();
        values.put(table_key, id);
        values.put(RoutesLanguage.type, 1);
        values.put(RoutesLanguage.language_ru, name.getShort().getRu());
        values.put(RoutesLanguage.language_en, name.getShort().getEn());
        values.put(RoutesLanguage.language_cn, name.getShort().getCn());
        values.put(RoutesLanguage.language_lt, name.getShort().getLt());
        values.put(RoutesLanguage.language_pl, name.getShort().getPl());
        valuesList.add(values);

        ContentValues values1 = new ContentValues();
        values1.put(table_key, id);
        values1.put(RoutesLanguage.type, 2);
        values1.put(RoutesLanguage.language_ru, name.getFull().getRu());
        values1.put(RoutesLanguage.language_en, name.getFull().getEn());
        values1.put(RoutesLanguage.language_cn, name.getFull().getCn());
        values1.put(RoutesLanguage.language_lt, name.getFull().getLt());
        values1.put(RoutesLanguage.language_pl, name.getFull().getPl());
        valuesList.add(values1);

        ContentValues values2 = new ContentValues();
        values2.put(table_key, id);
        values2.put(RoutesLanguage.type, 3);
        values2.put(RoutesLanguage.language_ru, about.getShort().getRu());
        values2.put(RoutesLanguage.language_en, about.getShort().getEn());
        values2.put(RoutesLanguage.language_cn, about.getShort().getCn());
        values2.put(RoutesLanguage.language_lt, about.getShort().getLt());
        values2.put(RoutesLanguage.language_pl, about.getShort().getPl());
        valuesList.add(values2);

        ContentValues values3 = new ContentValues();
        values3.put(table_key, id);
        values3.put(RoutesLanguage.type, 4);
        values3.put(RoutesLanguage.language_ru, about.getFull().getRu());
        values3.put(RoutesLanguage.language_en, about.getFull().getEn());
        values3.put(RoutesLanguage.language_cn, about.getFull().getCn());
        values3.put(RoutesLanguage.language_lt, about.getFull().getLt());
        values3.put(RoutesLanguage.language_pl, about.getFull().getPl());
        valuesList.add(values3);
        return valuesList;
    }

    static ContentValues insertPolyline(Turn polyline) {
        ContentValues values = new ContentValues();
        values.put(Lines.start_point, polyline.getStart().getCryptoLatLng());
        values.put(Lines.end_point, polyline.getEnd().getCryptoLatLng());
        values.put(Lines.polyline, polyline.getPolyline());
        return values;
    }
}
