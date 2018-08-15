package com.grsu.guideapp.fragments.map;

import static com.grsu.guideapp.utils.CheckSelfPermission.writeExternalStorageIsGranted;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import butterknife.BindView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.fragments.map.MapContract.MapViews;
import com.grsu.guideapp.utils.MarkerSingleton;
import com.grsu.guideapp.utils.PolylineSingleton;
import java.util.List;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.StorageUtils;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Marker.OnMarkerClickListener;


public class MapFragment extends BaseFragment<MapPresenter> implements MapViews,
        OnMarkerClickListener {

    private static final String TAG = MapFragment.class.getSimpleName();

    private PolylineSingleton polylineSingleton = PolylineSingleton.Polyline;
    private MarkerSingleton markerSingleton = MarkerSingleton.Marker;

    @BindView(R.id.mv_fragment_map)
    MapView mapView;

    @Override
    public void onAttach(@NonNull Context context) {
        if (writeExternalStorageIsGranted(context)) {
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
        super.onAttach(context);
    }

    @NonNull
    @Override
    protected MapPresenter getPresenterInstance() {
        return new MapPresenter(this, new MapInteractor(new DatabaseHelper(getContext())));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_map;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapViewSettings();
        //TODO hardcode value
        mPresenter.getId(1);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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
    }

    @Override
    public void setPolyline(List<GeoPoint> geoPointList) {
        polylineSingleton.getPolyline(mapView, geoPointList);
    }

    @Override
    public void setMarker(GeoPoint geoPoint) {
        markerSingleton.getMarker(mapView, geoPoint).setOnMarkerClickListener(this);
    }

    @Override
    public void animateMarker(final Marker marker, final GeoPoint toPosition) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mapView.getProjection();
        Point startPoint = proj.toPixels(marker.getPosition(), null);
        final IGeoPoint startGeoPoint = proj.fromPixels(startPoint.x, startPoint.y);
        final long duration = 50000;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * toPosition.getLongitude() + (1 - t) * startGeoPoint.getLongitude();
                double lat = t * toPosition.getLatitude() + (1 - t) * startGeoPoint.getLatitude();
                marker.setPosition(new GeoPoint(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 15);
                }
                mapView.postInvalidate();
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        return mPresenter.onMarkerClick(marker, mapView);
    }
}
