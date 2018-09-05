package com.grsu.guideapp.fragments.test;

import android.location.Location;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Tile;
import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import java.util.List;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.Marker;

public interface TestContract {

    interface TestViews extends BaseView {

        void mapViewSettings(GoogleMap googleMap);

        void setPolyline(List<LatLng> geoPointList, int id);

        Polyline setPolyline(LatLng geoPointList);

        Marker setPoints(LatLng geoPoint);

        void setGetPoints(Poi poi);

        void setGetPolyline(List<LatLng> geoPointList);

        void removeMarkers();

        void stopped();

        void removePolylines();

        void openDialogViews();

    }

    interface TestPresenter extends BasePresenter<TestViews> {
        void getId(Integer id);

        void getLocation(Location location);

        void setRadius(String radius);

        void setType(List<Integer> typesObjects);

        List<Integer> getType();

        void getMarkers();

        Tile getTile(int x, int y, int zoom, String provider);

        void onMapReady(GoogleMap googleMap);
    }

    interface TestInteractor {

        interface OnFinishedListener {

            void onFinished(List<Line> encodePolylines);

            void onFinished1(List<Poi> poiList);

            Tile onFinished(byte[] tile);
        }

        Tile getTile(OnFinishedListener listener, long index, String provider);

        void getRouteById(OnFinishedListener listener, Integer id);

        void getListPoi(OnFinishedListener listener, double latitude,
                double longitude, int radius, List<Integer> typesObjects);
    }
}
