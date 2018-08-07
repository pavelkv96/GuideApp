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
}
