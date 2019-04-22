package com.grsu.guideapp.utils;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import org.mapsforge.core.model.Tile;

public class MapUtils {

    private static final String TAG = MapUtils.class.getSimpleName();

    private static int mMaxZoomLevel = 29;
    private static int mModulo = 1 << mMaxZoomLevel;

    public static Tile getTile(long pMapTileIndex, int tileSize) {
        return new Tile(getX(pMapTileIndex), getY(pMapTileIndex), getZoom(pMapTileIndex), tileSize);
    }

    public static long getTileIndex(final int pZoom, final int pX, final int pY) {
        return (((long) pZoom) << (mMaxZoomLevel * 2)) + (((long) pX) << mMaxZoomLevel) + (long) pY;
    }

    public static byte getZoom(final long pTileIndex) {
        return (byte) (pTileIndex >> (mMaxZoomLevel * 2));
    }

    public static int getX(final long pTileIndex) {
        return (int) ((pTileIndex >> mMaxZoomLevel) % mModulo);
    }

    public static int getY(final long pTileIndex) {
        return (int) (pTileIndex % mModulo);
    }

    private static String toString(final int pZoom, final int pX, final int pY) {
        return "https://tile.openstreetmap.org/" + pZoom + "/" + pX + "/" + pY + ".png";
    }

    public static String toString(final long pIndex) {
        return toString(getZoom(pIndex), getX(pIndex), getY(pIndex));
    }

    public static long getIndex(final long pX, final long pY, final long pZ) {
        return ((pZ << pZ) + pX << pZ) + pY;
    }

    public static long getIndex(final long pMapTileIndex) {
        return getIndex(getX(pMapTileIndex), getY(pMapTileIndex), getZoom(pMapTileIndex));
    }

    public static LatLng toLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static Location toLocation(LatLng position) {
        Location location = new Location("");
        location.setLatitude(position.latitude);
        location.setLongitude(position.longitude);
        return location;
    }

    public static float getDistanceBetween(Location startLocation, Location endLocation) {
        return startLocation.distanceTo(endLocation);
    }

    public static boolean isMoreDistance(float distance, Location currentLocation, LatLng latLng) {
        return distance > MapUtils.getDistanceBetween(currentLocation, MapUtils.toLocation(latLng));
    }

    public static LatLngBounds getBounds(LatLng latLng, LatLngBounds bounds, LatLngBounds border) throws NullPointerException{

        if (!border.contains(latLng)){
            throw new NullPointerException("");
        }

        return bounds.contains(latLng) ? bounds : bounds.including(latLng);
    }

    public static double distanceTo(LatLng from, LatLng to) {
        return  computeAngleBetween(from, to) * 6371009;
    }

    private static double computeAngleBetween(LatLng from, LatLng to) {
        return distanceRadians(Math.toRadians(from.latitude), Math.toRadians(from.longitude),
                Math.toRadians(to.latitude), Math.toRadians(to.longitude));
    }

    private static double distanceRadians(double lat1, double lng1, double lat2, double lng2) {
        return arcHav(havDistance(lat1, lat2, lng1 - lng2));
    }

    private static double arcHav(double x) {
        return 2 * Math.asin(Math.sqrt(x));
    }

    private static double havDistance(double lat1, double lat2, double dLng) {
        return hav(lat1 - lat2) + hav(dLng) * Math.cos(lat1) * Math.cos(lat2);
    }

    private static double hav(double x) {
        double sinHalf = Math.sin(x * 0.5);
        return sinHalf * sinHalf;
    }

}
