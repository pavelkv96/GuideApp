package com.grsu.guideapp.fragments.map;

import android.location.Location;
import com.grsu.guideapp.activities.route.RouteContract.RouteInteractor;
import com.grsu.guideapp.activities.route.RouteContract.RouteInteractor.OnFinishedListener;
import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

public interface MapContract {

    interface MapViews extends BaseView {

        void mapViewSettings();

        void setPolyline(List<GeoPoint> geoPointList, int id);

        Marker setPoints(GeoPoint geoPoint);

        void setGetPoints(Poi poi);

        void removeMarkers();
    }

    interface MapPresenter extends BasePresenter<MapViews> {

        void getId(Integer id);

        void getLocation(Location location);

        boolean singleTapConfirmedHelper(GeoPoint p);
    }

    interface MapInteractor {

        interface OnFinishedListener {

            void onFinished(List<Line> encodePolylines);

            void onFinished1(List<Poi> poiList);
        }

        void getRouteById(OnFinishedListener listener, Integer id);

        void getListPoi(OnFinishedListener listener, double latitude, double longitude, int radius,
                List<Integer> typesObjects);
    }
}
