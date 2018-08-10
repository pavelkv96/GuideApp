package com.grsu.guideapp.utils;

import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.util.GeoPoint;

public class Crypto {

    @NonNull
    public static List<GeoPoint> decodeL(String encodedString) {
        return decode(encodedString);
    }

    @NonNull
    public static GeoPoint decodeP(String encodedString) {
        return decode(encodedString).get(0);
    }

    @NonNull
    private static List<GeoPoint> decode(final String encodedString) {
        int length = encodedString.length();

        final List<GeoPoint> path = new ArrayList<>();
        int index = 0;
        int latitude = 0;
        int longitude = 0;

        while (index < length) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedString.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            latitude += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedString.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            longitude += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new GeoPoint(latitude * 1e-5, longitude * 1e-5));
        }

        return path;
    }

    @NonNull
    public static String encodeL(List<GeoPoint> geoPointList) {
        return encode(geoPointList);
    }

    @NonNull
    public static String encodeP(GeoPoint geoPoint) {
        List<GeoPoint> list = new ArrayList<GeoPoint>();
        list.add(geoPoint);
        return encode(list);
    }

    @NonNull
    private static String encode(final List<GeoPoint> path) {
        long lastLat = 0;
        long lastLng = 0;

        final StringBuffer result = new StringBuffer();

        for (final GeoPoint point : path) {
            long lat = Math.round(point.getLatitude() * 1e5);
            long lng = Math.round(point.getLongitude() * 1e5);

            long dLat = lat - lastLat;
            long dLng = lng - lastLng;

            encode(dLat, result);
            encode(dLng, result);

            lastLat = lat;
            lastLng = lng;
        }
        return result.toString();
    }

    private static void encode(long v, StringBuffer result) {
        v = v < 0 ? ~(v << 1) : v << 1;
        while (v >= 0x20) {
            result.append(Character.toChars((int) ((0x20 | (v & 0x1f)) + 63)));
            v >>= 5;
        }
        result.append(Character.toChars((int) (v + 63)));
    }
}
