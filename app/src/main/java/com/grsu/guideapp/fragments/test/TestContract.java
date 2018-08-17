package com.grsu.guideapp.fragments.test;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.models.Line;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

public interface TestContract {

    interface TestViews extends BaseView {

        void mapViewSettings();

        void setPolyline(List<GeoPoint> geoPointList);

        Marker setPoints(GeoPoint geoPoint);

        Marker setTrackerMarker(GeoPoint geoPoint);

        void invalidate();

        void removePolyline(Polyline polyline);

        void removeMarker(Marker marker);

        Polyline initializePolyLine(GeoPoint position);

        Marker highLightMarker(Marker marker);

        void animateTo(GeoPoint geoPoint);
    }

    interface TestPresenter extends BasePresenter<TestViews> {

        void getId(Integer id);

        void startAnimation();

        boolean onMarkerClick(Marker marker, MapView mapView);

        void getLocation(GeoPoint point);
    }

    interface TestInteractor {

        interface OnFinishedListener {

            void onFinished(List<Line> encodePolylines);
        }

        interface OnChange {

            void OnChangeLocationListener(GeoPoint currentPosition);
        }

        void getRouteById(OnFinishedListener listener, Integer id);
    }
}
