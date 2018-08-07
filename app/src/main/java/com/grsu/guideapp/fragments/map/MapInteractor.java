package com.grsu.guideapp.fragments.map;

import android.os.Handler;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.util.GeoPoint;

public class MapInteractor implements MapContract.MapInteractor {

    @Override
    public void findItems(final OnFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(createArrayList());
            }
        }, 3000);
    }

    @Override
    public void findItems1(final OnFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(createArrayList1());
            }
        }, 5000);
    }

    @Override
    public void findMarker(final OnFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(new GeoPoint(53.6792, 23.8472));
            }
        }, 4000);
    }

    @Override
    public void findMarker1(final OnFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(new GeoPoint(53.6792, 23.8372));
            }
        }, 7000);
    }

    private List<GeoPoint> createArrayList() {
        List<GeoPoint> geoPointList = new ArrayList<>();
        geoPointList.add(new GeoPoint(53.6792, 23.8472));
        geoPointList.add(new GeoPoint(53.6692, 23.8472));
        geoPointList.add(new GeoPoint(53.6592, 23.8472));
        geoPointList.add(new GeoPoint(53.6492, 23.8472));

        return geoPointList;
    }

    private List<GeoPoint> createArrayList1() {
        List<GeoPoint> geoPointList = new ArrayList<>();
        geoPointList.add(new GeoPoint(53.6792, 23.8472));
        geoPointList.add(new GeoPoint(53.6792, 23.8372));
        geoPointList.add(new GeoPoint(53.6792, 23.8272));
        geoPointList.add(new GeoPoint(53.6792, 23.8172));

        return geoPointList;
    }
}
