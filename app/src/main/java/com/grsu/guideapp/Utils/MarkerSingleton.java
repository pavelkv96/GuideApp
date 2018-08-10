package com.grsu.guideapp.utils;

import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.views.infowindows.CustomMarkerInfoWindow;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

public enum MarkerSingleton {
    Marker;

    public Marker getValue(@NonNull MapView mapView, @NonNull GeoPoint geoPoint) {
        Marker marker = new Marker(mapView);
        marker.setPosition(geoPoint);
        //marker.setTitle("TitleTitleTitleTitleTitleTitleTitleTitleTitle");
        //marker.setSnippet("SnippetSnippetSnippetSnippetSnippetSnippet");
        //marker.setSubDescription("SubDescriptionSubDescriptionSubDescriptionSubDescription");

        marker.setInfoWindow(new CustomMarkerInfoWindow(mapView, false));
        //marker.setInfoWindow(new CustomMarkerInfoWindow(mapView));
        setOverlaysView(mapView, marker);

        return marker;
    }

    public Marker getValue(@NonNull MapView mapView, @NonNull Poi poi) {
        Marker marker = new Marker(mapView);
        marker.setPosition(new GeoPoint(poi.getLatitude(), poi.getLongitude()));
        //marker.setTitle("TitleTitleTitleTitleTitleTitleTitleTitleTitle");
        //marker.setSnippet("SnippetSnippetSnippetSnippetSnippetSnippet");
        //marker.setSubDescription("SubDescriptionSubDescriptionSubDescriptionSubDescription");

        marker = setIconByType(marker, poi.getType());
        marker.setInfoWindow(new CustomMarkerInfoWindow(mapView, false));
        //marker.setInfoWindow(new CustomMarkerInfoWindow(mapView));
        setOverlaysView(mapView, marker);

        return marker;
    }

    public static void setOverlaysView(@NonNull MapView mapView, @NonNull Overlay overlay) {
        mapView.getOverlays().add(overlay);
        mapView.invalidate();
    }

    public void removeMarkers(@NonNull MapView mapView, @NonNull List<Marker> markers) {
        for (Marker marker : markers) {
            marker.remove(mapView);
        }
    }

    private Marker setIconByType(Marker marker, Integer type) {
        Context context = ContextHolder.getContext();
        switch (type) {
            case 1:
                marker.setIcon(ContextCompat.getDrawable(context, R.drawable.a_marker));
                break;
            case 2:
                marker.setIcon(ContextCompat.getDrawable(context, R.drawable.b_marker));
                break;
            case 3:
                marker.setIcon(ContextCompat.getDrawable(context, R.drawable.c_marker));
                break;
            case 4:
                marker.setIcon(ContextCompat.getDrawable(context, R.drawable.d_marker));
                break;
        }

        return marker;
    }
}
