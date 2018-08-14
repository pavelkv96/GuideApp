package com.grsu.guideapp.activities.route;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

public interface RouteContract {

    interface RouteView extends BaseView {

        void mapViewSettings();

        void setPolyline(List<GeoPoint> geoPointList);

        void setPoints(GeoPoint geoPoint);

        void setGetPoints(Poi poi);

        void setGetPolyline(List<GeoPoint> geoPointList);

        void removeMarker();

        void removePolylines();

        void openDialogFragment();
    }


    interface RoutePresenter extends BasePresenter<RouteView> {

        void getId(Integer id);

        boolean singleTapConfirmedHelper(GeoPoint geoPoint, MapView mapView);

        boolean longPressHelper(GeoPoint p);

        boolean onMarkerClick(Marker marker, MapView mapView);

        boolean onClickPolyline(Polyline polyline, MapView mapView, GeoPoint eventPos);

        void getMarkers(GeoPoint geoPoint);

        void setRadius(String radius);

        void setType(List<Integer> typesObjects);

        List<Integer> getType();

        void getMarkersWithSettings(GeoPoint geoPoint);
    }


    interface RouteInteractor {

        interface OnFinishedListener {

            void onFinished(List<Line> encodePolylines);

            void onFinished1(List<Poi> poiList);
        }

        void getRouteById(OnFinishedListener listener, Integer id);

        void getListPoi(OnFinishedListener listener, double latitude, double longitude, int radius,
                List<Integer> typesObjects);
    }
}
