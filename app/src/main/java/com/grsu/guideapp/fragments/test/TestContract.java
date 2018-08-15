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

        void invalidate();

        void removePolyline(Polyline polyline);

//
//        void setMarker(GeoPoint geoPoint);
//
//        void animateMarker(final Marker marker, final GeoPoint toPosition);
    }

    interface TestPresenter extends BasePresenter<TestViews> {

        void getId(Integer id);

        boolean onMarkerClick(Marker marker, MapView mapView);
    }

    interface TestInteractor {

        interface OnFinishedListener {

            void onFinished(List<Line> encodePolylines);
        }

        void getRouteById(OnFinishedListener listener, Integer id);
    }
}
