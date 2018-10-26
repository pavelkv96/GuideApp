package com.grsu.guideapp.databases;

import android.support.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.utils.CryptoUtils;

public class DatabaseUtils {

    @NonNull
    static String getPlaceWithRadius(double cLatitude, double cLongitude, int radius,
            long[] typesObjects) {
        double lat = radius * Constants.ONE_METER_LAT;
        double lng = radius * Constants.ONE_METER_LNG;

        String rightDownLan = String.valueOf(cLatitude - lat);
        String leftUpLan = String.valueOf(cLatitude + lat);
        String rightDownLng = String.valueOf(cLongitude - lng);
        String leftUpLng = String.valueOf(cLongitude + lng);

        return getPlace() + getByRadius(rightDownLan, leftUpLan, rightDownLng, leftUpLng)
                + getByTypes(typesObjects) + getByIdPoint(cLatitude, cLongitude);
    }

    @NonNull
    static String getPlace(Integer id) {
        return "select c2.* from `list_poi` c1, `poi` c2, (" + getPointByIdRoute(id) + ") z1 " +
                "where c1.id_poi=c2.id_poi" + " AND (c1.id_point = z1.end_point)";
    }

    @NonNull
    static String getPlace() {
        return "select c2.* from `list_poi` c1, `poi` c2 where c1.id_poi=c2.id_poi";
    }

    @NonNull
    static String getByRadius(String rightDownLan, String leftUpLan, String rightDownLng,
            String leftUpLng) {
        return " AND (c2.latitude BETWEEN " + rightDownLan + " AND " + leftUpLan
                + ") AND (c2.longitude BETWEEN " + rightDownLng + " AND " + leftUpLng + ")";
    }

    @NonNull
    static String getByTypes(long[] typesObjects) {
        StringBuilder stringBuilder = new StringBuilder();
        for (long l : typesObjects) {
            stringBuilder.append(l).append(",");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        return " AND type IN(" + stringBuilder + ")";
    }

    @NonNull
    static String getByIdPoint(double cLatitude, double cLongitude) {
        return " AND (c1.id_point = '" + CryptoUtils.encodeP(new LatLng(cLatitude, cLongitude))
                + "')";
    }

    @NonNull
    static String getPointByIdRoute(Integer id) {
        return "SELECT c2.end_point FROM list_lines c1, lines c2 WHERE c1.id_line=c2.id_line"
                + " AND c1.id_route = " + id;
    }
}
