package com.grsu.guideapp.fragments.test;

import static com.grsu.guideapp.utils.Crypto.decodeL;
import static com.grsu.guideapp.utils.Crypto.decodeP;

import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.fragments.test.TestContract.TestInteractor.OnFinishedListener;
import com.grsu.guideapp.fragments.test.TestContract.TestViews;
import com.grsu.guideapp.models.Line;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

public class TestPresenter extends BasePresenterImpl<TestViews> implements OnFinishedListener,
        TestContract.TestPresenter, Runnable {

    private static final String TAG = TestPresenter.class.getSimpleName();

    private List<GeoPoint> points = new ArrayList<>();
    private List<Marker> markers = new ArrayList<>();
    private final Handler mHandler = new Handler();
    private static final int ANIMATE_SPEEED = 500;
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
       /* points.add(marker);
        startAnimation();*/

        return false;
    }

    @Override
    public void onFinished(List<Line> encodePolylines) {
        try {
            Line line = null;
            for (Line encodePolyline : encodePolylines) {
                points.addAll(decodeL(encodePolyline.getPolyline()));
                testViews.setPolyline(decodeL(encodePolyline.getPolyline()));
                markers.add(testViews.setPoints(decodeP(encodePolyline.getStartPoint())));
                line = encodePolyline;
            }
            testViews.setPoints(decodeP(line.getEndPoint()));
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
        testViews.animateTo(newPosition);
        updatePolyLine(newPosition);

        if (t < 1) {
            mHandler.postDelayed(this, 16);
        } else {
            /*Logs.e(TAG, "Move to next marker.... current = " + currentIndex + " and size = "
                    + points.size());*/
            if (currentIndex < points.size() - 2) {
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
        if (points.size() > 0) {
            initialize();
            endLatLng = getEndLatLng();
            start = SystemClock.uptimeMillis();
            run();
        }

    }

    /*@Override
    public void setMarkers(Marker marker) {
        points.add(marker);
    }*/

    private void reset() {
        currentIndex = 0;
        /*endLatLng = getEndLatLng();*/
        beginLatLng = getBeginLatLng();

    }

    private void initialize() {
        reset();

        highLightMarker(0);

        polyLine = testViews.initializePolyLine(points.get(0));
        trackingMarker = testViews.setTrackerMarker(points.get(0));

    }

    private void highLightMarker(int index) {
        //Logs.e(TAG, "new GeoPoint(" + points.get(index).toString()+")");
        //SystemClock.sleep(3000);
        /*Marker marker = markers.get(index);
        marker = testViews.highLightMarker(marker);
        markers.set(index, marker);*/
        //testViews.invalidate();
    }

    private void updatePolyLine(GeoPoint geoPoint) {
        List<GeoPoint> points = polyLine.getPoints();
        points.add(geoPoint);
        polyLine.setPoints(points);
    }

    void stop() {
        testViews.removeMarker(trackingMarker);
        mHandler.removeCallbacks(this);
        testViews.removePolyline(polyLine);
        testViews.invalidate();
        /*for (Marker marker : points) {
            testViews.removeMarker(marker);
        }*/

        points.clear();
    }

    private void pause() {
        beginLatLng = endLatLng;
    }

    private GeoPoint getEndLatLng() {
        return points.get(currentIndex + 1);
    }

    private GeoPoint getBeginLatLng() {
        return points.get(currentIndex);
    }


    private Location convertLatLngToLocation(GeoPoint latLng) {
        Location loc = new Location("someLoc");
        loc.setLatitude(latLng.getLatitude());
        loc.setLongitude(latLng.getLongitude());
        return loc;
    }

    private float bearingBetweenLatLngs(GeoPoint begin,GeoPoint end) {
        Location beginL= convertLatLngToLocation(begin);
        Location endL= convertLatLngToLocation(end);
        return beginL.bearingTo(endL);
    }
}

//For one marker: call marker.closeInfoWindow()
//For all points: call InfoWindow.closeAllInfoWindowsOn(mapView)