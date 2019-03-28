package com.grsu.guideapp.views.overlay;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grsu.guideapp.R;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.StorageUtils;

public class MyLocationOverlay {

    private Marker marker;
    private Circle circle;

    private MyLocationOverlay(GoogleMap mMap, Resources res) {
        LatLng latLng = new LatLng(0, 0);
        Bitmap bitmap = StorageUtils.getBitmap(res, R.drawable.my_location);
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(bitmap);

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.visible(false);
        circleOptions.zIndex(.1f);
        circleOptions.strokeWidth(1);
        circleOptions.fillColor(res.getColor(R.color.myLocation));
        circleOptions.strokeColor(res.getColor(R.color.myLocation1));
        circleOptions.center(latLng);

        MarkerOptions options = new MarkerOptions();
        options.visible(false);
        options.icon(descriptor);
        options.flat(true);
        options.anchor(.5f, .5f);
        options.position(latLng);

        marker = mMap.addMarker(options);
        circle = mMap.addCircle(circleOptions);
    }

    public void setShow(boolean isShow) {
        marker.setVisible(isShow);
        circle.setVisible(isShow);
    }

    public void setLocation(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        marker.setPosition(latLng);
        circle.setCenter(latLng);
        if (location.getAccuracy() != .0f) {
            circle.setRadius(location.getAccuracy());
        }
        setShow(true);
    }

    public Location getMyLocation(){
        return MapUtils.toLocation(marker.getPosition());
    }

    public static class Builder {

        private GoogleMap map;
        private Resources res;

        public Builder(GoogleMap map) {
            this.map = map;
        }

        public Builder addResource(Resources res) {
            this.res = res;
            return this;
        }

        public MyLocationOverlay build() {
            if (res == null) {
                throw new RuntimeException("The res cannot be null");
            }

            return new MyLocationOverlay(map, res);
        }
    }
}
