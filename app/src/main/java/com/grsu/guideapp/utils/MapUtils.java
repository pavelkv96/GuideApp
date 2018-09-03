package com.grsu.guideapp.utils;

import static com.grsu.guideapp.mf.MapsForgeTileSource.loadTile;

import android.support.annotation.NonNull;
import com.grsu.guideapp.activities.splash.SplashActivity;
import com.grsu.guideapp.models.XYTile;
import com.grsu.guideapp.models.XYTileBounds;

public class MapUtils {

    private static final String TAG = MapUtils.class.getSimpleName();

    private static int mMaxZoomLevel = 29;
    private static int mModulo = 1 << mMaxZoomLevel;


    public static void getTileInRange(final double northWestLatitude,
            final double northWestLongitude, final double southEastLatitude,
            final double southEastLongitude, final int minZoom, final int maxZoom) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int zoom = minZoom; zoom <= maxZoom; zoom++) {

                    XYTile northWest = getTileNumber(northWestLatitude, northWestLongitude, zoom);
                    XYTile southEast = getTileNumber(southEastLatitude, southEastLongitude, zoom);

                    XYTileBounds XYTileBounds = new XYTileBounds(northWest, southEast);

                    XYTile northWestXYTile = XYTileBounds.getNorthWestXYTile();
                    XYTile southEastXYTile = XYTileBounds.getSouthEastXYTile();

                    for (int xTile = northWestXYTile.getXTile(); xTile <= southEastXYTile.getXTile();
                            xTile++) {
                        for (int yTile = northWestXYTile.getYTile(); yTile <= southEastXYTile.getYTile();
                                yTile++) {
                            loadTile(getTileIndex(zoom, xTile, yTile));
                        }
                    }
                }

                SplashActivity.show();
            }
        }).start();

    }


    @NonNull
    public static XYTile getTileNumber(final double lat, final double lon, final int zoom) {
        int xTile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
        int yTile = (int) Math.floor((1
                - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat)))
                / Math.PI) / 2 * (1 << zoom));
        if (xTile < 0) {
            xTile = 0;
        }
        if (xTile >= (1 << zoom)) {
            xTile = ((1 << zoom) - 1);
        }
        if (yTile < 0) {
            yTile = 0;
        }
        if (yTile >= (1 << zoom)) {
            yTile = ((1 << zoom) - 1);
        }
        return new XYTile(xTile, yTile);
    }

    private static long getTileIndex(final int pZoom, final int pX, final int pY) {
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

    @NonNull
    public static String toString(final long pIndex) {
        return toString(getZoom(pIndex), getX(pIndex), getY(pIndex));
    }

    public static long getIndex(final long pX, final long pY, final long pZ) {
        return ((pZ << pZ) + pX << pZ) + pY;
    }

    public static long getIndex(final long pMapTileIndex) {
        return getIndex(getX(pMapTileIndex), getY(pMapTileIndex), getZoom(pMapTileIndex));
    }
}
