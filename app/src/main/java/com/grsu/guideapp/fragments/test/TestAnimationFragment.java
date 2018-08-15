package com.grsu.guideapp.fragments.test;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.fragments.test.TestContract.TestViews;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.views.infowindows.CustomMarkerInfoWindow;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.StorageUtils;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class TestAnimationFragment extends BaseFragment<TestPresenter> implements TestViews,
        MapEventsReceiver {

    private static final String TAG = TestAnimationFragment.class.getSimpleName();

    private List<Marker> markers = new ArrayList<>();
    private final Handler mHandler = new Handler();
    private Animator animator = new Animator();

    @BindView(R.id.mv_fragment_test_animation)
    MapView mapView;

    @NonNull
    @Override
    protected TestPresenter getPresenterInstance() {
        return new TestPresenter(this, new TestInteractor(new DatabaseHelper(getContext())));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_test_animation;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapViewSettings();
        mPresenter.getId(1);
    }

    @OnClick(R.id.btn_clearmarker)
    public void ResetMarker(View view) {
        animator.stop();
        for (Marker marker : markers) {
            marker.remove(mapView);
        }

        markers.clear();
        invalidate();
    }

    @OnClick(R.id.btn_play)
    public void start(View view) {
        animator.startAnimation(true);
        animator.run();
    }

    @OnClick(R.id.btn_stop)
    public void stop(View view) {
        animator.stop();
    }

    protected void addMarkerToMap(GeoPoint latLng) {
        Marker marker = new Marker(mapView);
        marker.setPosition(latLng);
        marker.setInfoWindow(new CustomMarkerInfoWindow(mapView, false));
        mapView.getOverlays().add(marker);
        markers.add(marker);
        invalidate();
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint point) {
        addMarkerToMap(point);
        animator.startAnimation(false);
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }

    @Override
    public void mapViewSettings() {
        Context ctx = getActivity();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setOsmdroidBasePath(StorageUtils.getStorage());

        mapView.setMaxZoomLevel(20.0);
        mapView.setMinZoomLevel(13.0);
        mapView.getController().setZoom(13.0);
        mapView.setBuiltInZoomControls(true);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setUseDataConnection(true);
        mapView.setScrollableAreaLimitDouble(new BoundingBox(53.7597, 23.9845, 53.5986, 23.7099));
        mapView.getOverlays().add(new MapEventsOverlay(this));
        new MyLocationNewOverlay(mapView);
    }

    @Override
    public void setPolyline(List<GeoPoint> geoPointList) {

    }

    @Override
    public void invalidate() {
        mapView.invalidate();
    }

    @Override
    public void removePolyline(Polyline polyline) {
        mapView.getOverlays().remove(polyline);
    }

    public class Animator implements Runnable {

        private static final int ANIMATE_SPEEED = 1500;
        private final Interpolator interpolator = new LinearInterpolator();
        int currentIndex = 0;
        long start = SystemClock.uptimeMillis();
        GeoPoint endLatLng = null;
        GeoPoint beginLatLng = null;
        boolean showPolyline = false;
        private Marker trackingMarker;

        void reset() {
            for (Marker marker : markers) {
                marker.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.a_marker));
            }

            start = SystemClock.uptimeMillis();
            currentIndex = 0;
            endLatLng = getEndLatLng();
            beginLatLng = getBeginLatLng();

        }

        void stop() {
            trackingMarker.remove(mapView);
            mHandler.removeCallbacks(animator);
            removePolyline(polyLine);
            invalidate();
        }

        private void highLightMarker(int index) {
            Marker marker = markers.get(index);
            marker.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.c_marker));
            marker.setInfoWindow(new CustomMarkerInfoWindow(mapView, false));
            markers.set(index, marker);
            invalidate();
        }

        void initialize(boolean showPolyLine) {
            reset();

            this.showPolyline = showPolyLine;
            highLightMarker(0);
            if (showPolyLine) {
                polyLine = initializePolyLine();
                setupCameraPositionForMovement(markers.get(0).getPosition());
            }
        }

        private void setupCameraPositionForMovement(GeoPoint markerPos) {
            Marker marker = new Marker(mapView);
            marker.setPosition(markerPos);
            marker.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.b_marker));
            marker.setDraggable(true);
            marker.setInfoWindow(new CustomMarkerInfoWindow(mapView, false));
            trackingMarker = marker;
            mapView.getOverlays().add(trackingMarker);
        }

        Polyline polyLine;

        private Polyline initializePolyLine() {
            polyLine = new Polyline(mapView);
            polyLine.addPoint(markers.get(0).getPosition());
            mapView.getOverlays().add(polyLine);

            return polyLine;
        }

        private void updatePolyLine(GeoPoint latLng) {
            List<GeoPoint> points = polyLine.getPoints();
            points.add(latLng);
            polyLine.setPoints(points);
        }

        void startAnimation(boolean showPolyLine) {
            if (markers.size() > 2) {
                animator.initialize(showPolyLine);
            }
        }

        @Override
        public void run() {
            long elapsed = SystemClock.uptimeMillis() - start;
            double t = interpolator.getInterpolation((float) elapsed / ANIMATE_SPEEED);
            double lat = t * endLatLng.getLatitude() + (1 - t) * beginLatLng.getLatitude();
            double lng = t * endLatLng.getLongitude() + (1 - t) * beginLatLng.getLongitude();
            GeoPoint newPosition = new GeoPoint(lat, lng);

            trackingMarker.setPosition(newPosition);
            if (showPolyline) {
                updatePolyLine(newPosition);
            }

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
                    mHandler.postDelayed(animator, 16);
                } else {
                    currentIndex++;
                    highLightMarker(currentIndex);
                    stop();
                }
            }
            invalidate();
        }

        private GeoPoint getEndLatLng() {
            return markers.get(currentIndex + 1).getPosition();
        }

        private GeoPoint getBeginLatLng() {
            return markers.get(currentIndex).getPosition();
        }
    }

}