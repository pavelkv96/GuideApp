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
import com.grsu.guideapp.project_settings.Constants;
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
        values.put(Routes.id_route, id);
        values.put(Routes.duration, 0);
        values.put(Routes.distance, 0);
        values.put(Routes.reference_photo_route, route.getImages().get(0).getHref());
        values.put(Routes.southwest, CryptoUtils.encodeP(value.getLimLeft().getLatLng()));
        values.put(Routes.northeast, CryptoUtils.encodeP(value.getLimRight().getLatLng()));
        values.put(Routes.last_update, date);
        values.put(Routes.last_download, System.currentTimeMillis() / 1000);
        return values;
    }

    static ContentValues updateRoute(Data route, int id) {
        Value value = route.getRoute().getValue();
        ContentValues values = new ContentValues();
        values.put(Routes.id_route, id);
        values.put(Routes.duration, 0);
        values.put(Routes.distance, 0);
        values.put(Routes.reference_photo_route, route.getImages().get(0).getHref());
        values.put(Routes.southwest, CryptoUtils.encodeP(value.getLimLeft().getLatLng()));
        values.put(Routes.northeast, CryptoUtils.encodeP(value.getLimRight().getLatLng()));
        values.put(Routes.last_download, System.currentTimeMillis() / 1000);
        return values;
    }

    static List<ContentValues> insertLanguage(Name name, About about, int id, String key) {
        List<ContentValues> valuesList = new ArrayList<>();

        valuesList.add(setRUContent(name, about, id, key));
        valuesList.add(setENContent(name, about, id, key));
        valuesList.add(setZHContent(name, about, id, key));
        valuesList.add(setPLContent(name, about, id, key));
        valuesList.add(setLTContent(name, about, id, key));
        return valuesList;
    }

    static ContentValues insertPolyline(Turn polyline) {
        ContentValues values = new ContentValues();
        values.put(Lines.start_point, polyline.getStart().getCryptoLatLng());
        values.put(Lines.end_point, polyline.getEnd().getCryptoLatLng());
        values.put(Lines.polyline, polyline.getPolyline());
        return values;
    }


    private static ContentValues setRUContent(Name name, About about, int id, String key) {
        ContentValues values = new ContentValues();
        values.put(key, id);
        values.put(RoutesLanguage.language, Constants.Language.ru.toString());

        if (name.getFull() != null) {
            values.put(RoutesLanguage.name, name.getFull().getRu());
        } else {
            values.put(RoutesLanguage.name,"");
        }

        if (about.getFull() != null) {
            values.put(RoutesLanguage.description, about.getFull().getRu());
        } else {
            values.put(RoutesLanguage.description, "");
        }
        return values;
    }

    private static ContentValues setENContent(Name name, About about, int id, String key) {
        ContentValues values = new ContentValues();
        values.put(key, id);
        values.put(RoutesLanguage.language, Constants.Language.en.toString());

        if (name.getFull() != null) {
            values.put(RoutesLanguage.name, name.getFull().getEn());
        } else {
            values.put(RoutesLanguage.name, "");
        }

        if (about.getFull() != null) {
            values.put(RoutesLanguage.description, about.getFull().getEn());
        } else {
            values.put(RoutesLanguage.description, "");
        }
        return values;
    }

    private static ContentValues setZHContent(Name name, About about, int id, String key) {
        ContentValues values = new ContentValues();
        values.put(key, id);
        values.put(RoutesLanguage.language, Constants.Language.zh.toString());

        if (name.getFull() != null) {
            values.put(RoutesLanguage.name, name.getFull().getCn());
        } else {
            values.put(RoutesLanguage.name, "");
        }

        if (about.getFull() != null) {
            values.put(RoutesLanguage.description, about.getFull().getCn());
        } else {
            values.put(RoutesLanguage.description, "");
        }
        return values;
    }

    private static ContentValues setPLContent(Name name, About about, int id, String key) {
        ContentValues values = new ContentValues();
        values.put(key, id);
        values.put(RoutesLanguage.language, Constants.Language.pl.toString());

        if (name.getFull() != null) {
            values.put(RoutesLanguage.name, name.getFull().getPl());
        } else {
            values.put(RoutesLanguage.name, "");
        }

        if (about.getFull() != null) {
            values.put(RoutesLanguage.description, about.getFull().getPl());
        } else {
            values.put(RoutesLanguage.description, "");
        }
        return values;
    }

    private static ContentValues setLTContent(Name name, About about, int id, String key) {
        ContentValues values = new ContentValues();
        values.put(key, id);
        values.put(RoutesLanguage.language, Constants.Language.lt.toString());

        if (name.getFull() != null) {
            values.put(RoutesLanguage.name, name.getFull().getLt());
        } else {
            values.put(RoutesLanguage.name, "");
        }

        if (about.getFull() != null) {
            values.put(RoutesLanguage.description, about.getFull().getLt());
        } else {
            values.put(RoutesLanguage.description, "");
        }
        return values;
    }
}
