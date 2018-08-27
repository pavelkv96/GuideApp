package com.grsu.guideapp.fragments.map;

import android.location.Location;
import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

public interface MapContract {

    interface MapViews extends BaseView {

        void mapViewSettings();

        void setPolyline(List<GeoPoint> geoPointList, int id);

        Polyline setPolyline(GeoPoint geoPointList);

        Marker setPoints(GeoPoint geoPoint);

        void setGetPoints(Poi poi);

        void removeMarkers();

        void stopped();
    }

    interface MapPresenter extends BasePresenter<MapViews> {

        void getId(Integer id);

        void getLocation(Location location);
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
