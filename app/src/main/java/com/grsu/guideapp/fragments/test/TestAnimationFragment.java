package com.grsu.guideapp.fragments.test;

import static com.grsu.guideapp.utils.Constants.KEY_GEO_POINT;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import butterknife.BindView;
import butterknife.OnClick;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.fragments.test.TestContract.TestViews;
import com.grsu.guideapp.utils.MarkerSingleton;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.PolylineSingleton;
import com.grsu.guideapp.views.infowindows.CustomMarkerInfoWindow;
import java.util.List;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.StorageUtils;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

public class TestAnimationFragment extends BaseFragment<TestPresenter> implements TestViews {

    private static final String TAG = TestAnimationFragment.class.getSimpleName();
    public final static String BR_ACTION = TestAnimationFragment.class.getPackage().toString();
    private MarkerSingleton markerSingleton = MarkerSingleton.Marker;
    private PolylineSingleton polylineSingleton = PolylineSingleton.Polyline;
    private IMapController iMapController;
    private BroadcastReceiver br;

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

        br = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                GeoPoint geoPoint = intent.getParcelableExtra(KEY_GEO_POINT);
                Logs.e(TAG, "I'm");
                //mPresenter.getLocation(geoPoint);

                //TODO logic
            }
        };

        getActivity().registerReceiver(br, new IntentFilter(BR_ACTION));
    }

    @OnClick(R.id.btn_clearmarker)
    public void ResetMarker(View view) {
        mPresenter.stop();
        invalidate();
    }

    @OnClick(R.id.btn_start)
    public void start(View view) {
        iMapController.setZoom(20f);
        getActivity().startService(new Intent(getActivity(), MyService.class));
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
        iMapController = new MapController(mapView);
    }


    @Override
    public void setPolyline(List<GeoPoint> geoPointList) {
        polylineSingleton.getPolyline(mapView, geoPointList);
    }

    @Override
    public Marker setPoints(GeoPoint geoPoint) {
        Marker marker = markerSingleton.getMarker(mapView, geoPoint);
        marker.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.a_marker));
        return marker;
    }

    @Override
    public Marker setTrackerMarker(GeoPoint geoPoint) {
        Marker marker = new Marker(mapView);
        marker.setPosition(geoPoint);
        marker.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.b_marker));
        marker.setInfoWindow(new CustomMarkerInfoWindow(mapView, false));
        mapView.getOverlays().add(marker);
        return marker;
    }

    @Override
    public void invalidate() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mapView.invalidate();
            }
        });
    }

    @Override
    public void removePolyline(Polyline polyline) {
        mapView.getOverlays().remove(polyline);
    }

    @Override
    public void removeMarker(Marker marker) {
        marker.remove(mapView);
    }

    @Override
    public Polyline initializePolyLine(GeoPoint position) {
        Polyline polyLine = new Polyline(mapView);
        polyLine.addPoint(position);
        polyLine.setColor(Color.RED);
        mapView.getOverlays().add(polyLine);

        return polyLine;
    }

    @Override
    public Marker highLightMarker(Marker marker) {
        marker.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.c_marker));
        return marker;
    }

    @Override
    public void animateTo(final GeoPoint geoPoint) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                iMapController.setCenter(geoPoint);
            }
        });
    }

    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(br);
        getActivity().stopService(new Intent(getActivity(), MyService.class));
        super.onDestroyView();
    }
}