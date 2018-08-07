package com.grsu.guideapp.utils;

import android.support.annotation.NonNull;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

public enum PolylineSingleton {

    INSTANCE;

    public void getValue(@NonNull MapView mapView, @NonNull List<GeoPoint> geoPointList) {
        Polyline polyline = new Polyline(mapView);
        polyline.setPoints(geoPointList);
        MarkerSingleton.setOverlaysView(mapView, polyline);
        mapView.invalidate();
        //return polyline;
    }
}
