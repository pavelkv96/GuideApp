package com.grsu.guideapp.utils;

import android.support.annotation.NonNull;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

public enum MarkerSingleton {
    INSTANCE;

    private static final String TAG = "MarkerSingleton";

    //@NonNull
    public Marker getValue(@NonNull MapView mapView, @NonNull GeoPoint geoPoint) {
        Marker marker = new Marker(mapView);
        marker.setPosition(geoPoint);
        marker.setDraggable(true);
        //marker.setTitle("TitleTitleTitleTitleTitleTitleTitleTitleTitle");
        //marker.setSnippet("SnippetSnippetSnippetSnippetSnippetSnippet");
        //marker.setSubDescription("SubDescriptionSubDescriptionSubDescriptionSubDescription");

        marker.setInfoWindow(new CustomMarkerInfoWindow(mapView));
        setOverlaysView(mapView, marker);

        //mapView.invalidate();
        return marker;
    }

    public static void setOverlaysView(@NonNull MapView mapView, @NonNull Overlay overlay) {
        mapView.getOverlays().add(overlay);
    }
}
