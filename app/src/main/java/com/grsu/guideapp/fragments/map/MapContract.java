package com.grsu.guideapp.fragments.map;

import com.grsu.guideapp.activities.route.RouteContract;
import com.grsu.guideapp.activities.route.RouteContract.RouteInteractor;
import com.grsu.guideapp.activities.route.RouteContract.RouteInteractor.OnFinishedListener;
import com.grsu.guideapp.activities.route.RouteContract.RouteView;
import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

public interface MapContract {

    interface MapViews extends BaseView {

        void mapViewSettings();

        void setPolyline(List<GeoPoint> geoPointList);

        void setMarker(GeoPoint geoPoint);

        void animateMarker(final Marker marker, final GeoPoint toPosition);
    }

    interface MapPresenter extends BasePresenter<MapViews> {

        void getId(Integer id);

        boolean onMarkerClick(Marker marker, MapView mapView);
    }

    interface MapInteractor {

        interface OnFinishedListener {

            void onFinished(List<Line> encodePolylines);
        }

        void getRouteById(OnFinishedListener listener, Integer id);
    }
}
