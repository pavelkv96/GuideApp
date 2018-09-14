package com.grsu.guideapp.activities.route;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Tile;
import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.base.listeners.OnFinishedTileListener;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import java.util.List;

public interface RouteContract extends OnFinishedListener, OnFinishedTileListener {

    interface RouteView extends BaseView {

        void mapViewSettings(GoogleMap googleMap);

        void openDialogViews();

        void setPolyline(List<LatLng> geoPointList, int id);

        void setCurrentPoint(LatLng geoPointList);

        void setPointsTurn(LatLng geoPoint);

        void setPoi(Poi poi);

        void removePoi();
    }


    interface RoutePresenter extends BasePresenter<RouteView> {

        void getId(Integer id);

        void getProjectionLocation(Location location);

        void setRadius(String radius);

        void setType(List<Integer> typesObjects);

        List<Integer> getType();

        void setAllPoi(boolean getAll);

        void getAllPoi();

        void getPoi();

        Tile getTile(int x, int y, int zoom, String provider);

        void onMapReady(GoogleMap googleMap);

        Intent onMarkerClick(Context context, Marker marker);
    }


    interface RouteInteractor {

        Tile getTile(OnFinishedTileListener<Tile> listener, long index, String provider);

        void getRouteById(OnFinishedListener<List<Line>> listener, Integer id);

        void getListPoi(OnFinishedListener<List<Poi>> listener, double latitude, double longitude,
                int radius, List<Integer> typesObjects);

        void getListPoi(OnFinishedListener<List<Poi>> listener, Integer id,
                List<Integer> typesObjects);
    }
}
