package com.grsu.guideapp.utils;

import android.support.annotation.NonNull;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

public enum PolylineSingleton {

    INSTANCE;
    Polyline value;

    public void setValue(Polyline value) {
        this.value = value;
    }

    //@NonNull
    public void getValue(@NonNull MapView mapView, @NonNull List<GeoPoint> geoPointList) {
        Polyline polyline = new Polyline(mapView);
        polyline.setPoints(geoPointList);
        MarkerSingleton.setOverlaysView(mapView, polyline);
        //return polyline;
    }
}
