package com.grsu.guideapp.fragments.map;

import android.location.Location;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Tile;
import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import java.util.List;

public interface MapContract {

    interface MapViews extends BaseView {

        void mapViewSettings(GoogleMap googleMap);

        void openDialogViews();

        void setPolyline(List<LatLng> geoPointList, int id);

        void setCurrentPoint(LatLng geoPointList);

        void setPointsTurn(LatLng geoPoint);

        void setPoi(Poi poi);

        void removePoi();

    }

    interface MapPresenter extends BasePresenter<MapViews> {
        void getId(Integer id);

        void getProjectionLocation(Location location);

        void setRadius(String radius);

        void setType(List<Integer> typesObjects);

        List<Integer> getType();

        void getPoi();

        Tile getTile(int x, int y, int zoom, String provider);

        void onMapReady(GoogleMap googleMap);
    }

    interface MapInteractor {

        interface OnFinishedListener {

            void onFinished(List<Line> encodePolylines);

            void onFinished1(List<Poi> poiList);

            Tile onFinished(byte[] tile);
        }

        Tile getTile(OnFinishedListener listener, long index, String provider);

        void getRouteById(OnFinishedListener listener, Integer id);

        void getListPoi(OnFinishedListener listener, double latitude, double longitude, int radius,
                List<Integer> typesObjects);
    }
}
