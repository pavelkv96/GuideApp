package com.grsu.guideapp.utils;

import android.support.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class CryptoUtils {

    @NonNull
    public static List<LatLng> decodeL(String encodedString) {
        return decode(encodedString);
    }

    @NonNull
    public static LatLng decodeP(String encodedString) {
        return decode(encodedString).get(0);
    }

    @NonNull
    private static List<LatLng> decode(final String encodedString) {
        int length = encodedString.length();

        final List<LatLng> path = new ArrayList<>();
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

            path.add(new LatLng(latitude * 1e-5, longitude * 1e-5));
        }

        return path;
    }

    @NonNull
    public static String encodeL(List<LatLng> geoPointList) {
        return encode(geoPointList);
    }

    @NonNull
    public static String encodeP(LatLng geoPoint) {
        List<LatLng> list = new ArrayList<>();
        list.add(geoPoint);
        return encode(list);
    }

    @NonNull
    private static String encode(final List<LatLng> path) {
        long lastLat = 0;
        long lastLng = 0;

        final StringBuffer result = new StringBuffer();

        for (final LatLng point : path) {
            long lat = Math.round(point.latitude * 1e5);
            long lng = Math.round(point.longitude * 1e5);

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

    public static String hash(String value) {
        try {
            byte[] array = MessageDigest.getInstance("MD5").digest(value.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | NullPointerException e) {
            e.printStackTrace();
        }
        return value;
    }
}
