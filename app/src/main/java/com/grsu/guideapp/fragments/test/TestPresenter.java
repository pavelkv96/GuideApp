package com.grsu.guideapp.fragments.test;

import static com.grsu.guideapp.utils.Crypto.decodeL;
import static com.grsu.guideapp.utils.Crypto.decodeP;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.fragments.test.TestContract.TestInteractor.OnFinishedListener;
import com.grsu.guideapp.fragments.test.TestContract.TestViews;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.views.infowindows.CustomMarkerInfoWindow;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

public class TestPresenter extends BasePresenterImpl<TestViews> implements
        TestContract.TestPresenter,
        OnFinishedListener, Runnable {

    private static final String TAG = TestPresenter.class.getSimpleName();

    private List<Marker> markers = new ArrayList<>();
    private final Handler mHandler = new Handler();
    private static final int ANIMATE_SPEEED = 1500;
    private final Interpolator interpolator = new LinearInterpolator();
    private int currentIndex = 0;
    private long start = SystemClock.uptimeMillis();
    private GeoPoint endLatLng = null;
    private GeoPoint beginLatLng = null;
    private Marker trackingMarker;
    private Polyline polyLine;


    private TestViews testViews;
    private TestInteractor testInteractor;

    public TestPresenter(TestViews testViews, TestInteractor testInteractor) {
        this.testViews = testViews;
        this.testInteractor = testInteractor;
    }

    @Override
    public void getId(Integer id) {
        testInteractor.getRouteById(this, id);
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        markers.add(marker);
        startAnimation();

        return false;
    }

    @Override
    public void onFinished(List<Line> encodePolylines) {
        List<GeoPoint> geoPointList = new ArrayList<>();

        try {
            for (Line encodePolyline : encodePolylines) {
                geoPointList.addAll(decodeL(encodePolyline.getPolyline()));
            }
            testViews.setPolyline(geoPointList);
        } catch (NullPointerException ignored) {
        }
        mView.hideProgress();
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void run() {
        long elapsed = SystemClock.uptimeMillis() - start;
        double t = interpolator.getInterpolation((float) elapsed / ANIMATE_SPEEED);
        double lat = t * endLatLng.getLatitude() + (1 - t) * beginLatLng.getLatitude();
        double lng = t * endLatLng.getLongitude() + (1 - t) * beginLatLng.getLongitude();
        GeoPoint newPosition = new GeoPoint(lat, lng);

        trackingMarker.setPosition(newPosition);
        updatePolyLine(newPosition);

        if (t < 1) {
            mHandler.postDelayed(this, 16);
        } else {
            Logs.e(TAG, "Move to next marker.... current = " + currentIndex + " and size = "
                    + markers.size());
            if (currentIndex < markers.size() - 2) {
                currentIndex++;
                endLatLng = getEndLatLng();
                beginLatLng = getBeginLatLng();
                start = SystemClock.uptimeMillis();
                highLightMarker(currentIndex);
                start = SystemClock.uptimeMillis();
                mHandler.postDelayed(this, 16);
                //pause();
            } else {
                currentIndex++;
                highLightMarker(currentIndex);
                //stop();
                pause();
            }
        }
        testViews.invalidate();
    }

    @Override
    public void startAnimation() {
        if (markers.size() == 1) {
            initialize();
        } else {
            if (markers.size() > 0) {
                endLatLng = getEndLatLng();
                start = SystemClock.uptimeMillis();
                run();
            }
        }
    }

    @Override
    public void setMarkers(Marker marker) {
        markers.add(marker);
    }

    private void reset() {
        currentIndex = 0;
        /*endLatLng = getEndLatLng();*/
        beginLatLng = getBeginLatLng();

    }

    private void initialize() {
        reset();

        highLightMarker(0);

        polyLine = testViews.initializePolyLine(markers.get(0).getPosition());
        setupCameraPositionForMovement(markers.get(0));

    }

    private void highLightMarker(int index) {
        Marker marker = markers.get(index);
        marker = testViews.highLightMarker(marker);
        markers.set(index, marker);
        testViews.invalidate();
    }

    private void setupCameraPositionForMovement(Marker markerPos) {

        trackingMarker = testViews.setTrackerMarker(markerPos);
    }


    private void updatePolyLine(GeoPoint latLng) {
        List<GeoPoint> points = polyLine.getPoints();
        points.add(latLng);
        polyLine.setPoints(points);
    }

    void stop() {
        testViews.removeMarker(trackingMarker);
        mHandler.removeCallbacks(this);
        testViews.removePolyline(polyLine);
        testViews.invalidate();
        for (Marker marker : markers) {
            testViews.removeMarker(marker);
        }

        markers.clear();
    }

    private void pause() {
        beginLatLng = endLatLng;
    }

    public void paused() {
        mHandler.removeCallbacks(this);
    }

    public void resumed() {
        mHandler.postDelayed(this, 16);
    }

    private GeoPoint getEndLatLng() {
        return markers.get(currentIndex + 1).getPosition();
    }

    private GeoPoint getBeginLatLng() {
        return markers.get(currentIndex).getPosition();
    }
}

//For one marker: call marker.closeInfoWindow()
//For all markers: call InfoWindow.closeAllInfoWindowsOn(mapView)