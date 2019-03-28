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

}
