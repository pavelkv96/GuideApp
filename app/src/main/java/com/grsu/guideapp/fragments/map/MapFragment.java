package com.grsu.guideapp.fragments.map;

import static com.grsu.guideapp.utils.CheckSelfPermission.writeExternalStorageIsGranted;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import butterknife.BindView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.fragments.map.MapContract.MapViews;
import com.grsu.guideapp.utils.MarkerSingleton;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import com.grsu.guideapp.utils.PolylineSingleton;
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
import org.osmdroid.views.overlay.Marker.OnMarkerClickListener;

public class MapFragment extends BaseFragment<MapPresenter> implements MapEventsReceiver, MapViews,
        OnMarkerClickListener {

    PolylineSingleton polylineSingleton = PolylineSingleton.INSTANCE;
    MarkerSingleton markerSingleton = MarkerSingleton.INSTANCE;

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
        return new MapPresenter(this, new MapInteractor());
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_map;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapViewSettings();
        mapView.getOverlays().add(new MapEventsOverlay(this));
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
    public boolean singleTapConfirmedHelper(GeoPoint point) {
        return mPresenter.singleTapConfirmedHelper(point, mapView);
    }

    @Override
    public boolean longPressHelper(GeoPoint point) {
        return mPresenter.longPressHelper(point);
    }

    @Override
    public void mapViewSettings() {
        Context ctx = getActivity();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setOsmdroidBasePath(StorageUtils.getStorage());

        mapView.setMaxZoomLevel(20.0);
        mapView.setMinZoomLevel(13.0);
        mapView.setExpectedCenter(new GeoPoint(53.0805, 23.1373));
        mapView.getController().setZoom(13.0);
        mapView.setBuiltInZoomControls(true);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setUseDataConnection(false);
        mapView.setScrollableAreaLimitDouble(new BoundingBox(53.7597, 23.9845, 53.5986, 23.7099));
    }

    @Override
    public void setPolyline(List<GeoPoint> geoPointList) {
        Toasts.makeS(getContext(), String.valueOf(geoPointList.size()));
        polylineSingleton.getValue(mapView, geoPointList);
    }

    @Override
    public void setMarker(GeoPoint geoPoint) {
        markerSingleton.getValue(mapView, geoPoint);
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        return mPresenter.onMarkerClick(marker, mapView);
    }
}
