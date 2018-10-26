package com.grsu.guideapp.fragments.map;

import android.content.Context;
import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.models.Line;
import java.util.List;

public interface MapContract extends OnFinishedListener {

    interface MapViews extends BaseView {

        void openDialogViews();

        void setStartMarker(LatLng startMarker);

        void setPointsTurn(LatLng pointsTurn);

        void setEndMarker(LatLng endMarker);

        void setPolyline(List<LatLng> pointsList, int id);

        void setCurrentPoint(LatLng currentPoint);

        void setPoi(Poi poi);

        void removePoi();
    }

    interface MapPresenter extends BasePresenter<MapViews> {

        void getId(Integer id);

        void setRadius(Integer radius);

        void setType(long[] typesObjects);

        long[] getType();

        void setAllPoi(boolean getAll);

        void getAllPoi();

        void getPoi();

        void getProjectionLocation(Location location);

        void onMarkerClick(Context context, Marker marker);
    }

    interface MapInteractor {

        void getRouteById(OnFinishedListener<List<Line>> listener, Integer id);

        void getListPoi(OnFinishedListener<List<Poi>> listener, double latitude, double longitude,
                int radius, long[] typesObjects);

        void getListPoi(OnFinishedListener<List<Poi>> listener, Integer id, long[] typesObjects);
    }
}
