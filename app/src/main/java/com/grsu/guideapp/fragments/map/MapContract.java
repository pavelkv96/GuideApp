package com.grsu.guideapp.fragments.map;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public interface MapContract {

    interface MapViews extends BaseView {

        void mapViewSettings();

        void setPolyline(List<GeoPoint> geoPointList);

        void setMarker(GeoPoint geoPoint);
    }

    interface MapPresenter extends BasePresenter<MapViews> {

        boolean singleTapConfirmedHelper(GeoPoint geoPoint, MapView mapView);

        boolean longPressHelper(GeoPoint p);

        boolean onMarkerClick(Marker marker, MapView mapView);
    }

    interface MapInteractor {

        interface OnFinishedListener {

            void onFinished(List<GeoPoint> geoPointList);

            void onFinished(GeoPoint geoPoint);
        }

        void findItems(OnFinishedListener listener);

        void findItems1(OnFinishedListener listener);

        void findMarker(OnFinishedListener listener);

        void findMarker1(OnFinishedListener listener);
    }
}
