package com.grsu.guideapp.fragments.test;

import static com.grsu.guideapp.utils.Crypto.decodeL;
import static com.grsu.guideapp.utils.Crypto.decodeP;

import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.fragments.test.TestContract.TestInteractor.OnFinishedListener;
import com.grsu.guideapp.fragments.test.TestContract.TestViews;
import com.grsu.guideapp.models.Line;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class TestPresenter extends BasePresenterImpl<TestViews> implements TestContract.TestPresenter,
        OnFinishedListener {

    private TestViews mapViews;
    private TestInteractor mapInteractor;

    public TestPresenter(TestViews mapViews, TestInteractor mapInteractor) {
        this.mapViews = mapViews;
        this.mapInteractor = mapInteractor;
    }

    @Override
    public void getId(Integer id) {
        mapInteractor.getRouteById(this, id);
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        return false;
    }

    @Override
    public void onFinished(List<Line> encodePolylines) {
        List<GeoPoint> geoPointList = new ArrayList<>();

        try {
            for (Line encodePolyline : encodePolylines) {
                geoPointList.addAll(decodeL(encodePolyline.getPolyline()));
            }
            mapViews.setPolyline(geoPointList);
        } catch (NullPointerException ignored) {
        }
        mView.hideProgress();
    }
}

//For one marker: call marker.closeInfoWindow()
//For all markers: call InfoWindow.closeAllInfoWindowsOn(mapView)