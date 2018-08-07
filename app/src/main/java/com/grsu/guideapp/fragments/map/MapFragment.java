package com.grsu.guideapp.fragments.map;

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
import com.grsu.guideapp.utils.MarkerSingleton;
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
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class MapFragment extends BaseFragment<MapPresenter> implements MapEventsReceiver {

    Marker singletonValue = null;

    //TODO USING MapContract and MapPresenter

    @BindView(R.id.mv_fragment_map)
    MapView mapView;

    @Override
    public void onAttach(Context context) {
        if (context != null) {
            int permissionStatus = ContextCompat
                    .checkSelfPermission(context, permission.WRITE_EXTERNAL_STORAGE);
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        }
        super.onAttach(context);
    }

    @NonNull
    @Override
    protected MapPresenter getPresenterInstance() {
        return new MapPresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_map;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context ctx = getActivity();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setOsmdroidBasePath(StorageUtils.getStorage());

        mapView.setMaxZoomLevel(20.0);
        mapView.setMinZoomLevel(13.0);
        mapView.setExpectedCenter(new GeoPoint(48.8583, 2.2944));
        mapView.getController().setZoom(13.0);
        mapView.setBuiltInZoomControls(true);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setUseDataConnection(true);
        mapView.setScrollableAreaLimitDouble(
                new BoundingBox(53.7597, 23.9845, 53.5986, 23.7099));

        //TODO geoPointList, geoPointList1
        List<GeoPoint> geoPointList = new ArrayList<>();
        geoPointList.add(new GeoPoint(48.8583, 2.2944));
        geoPointList.add(new GeoPoint(47.8583, 2.2944));
        geoPointList.add(new GeoPoint(46.8583, 2.2944));
        geoPointList.add(new GeoPoint(45.8583, 2.2944));

        List<GeoPoint> geoPointList1 = new ArrayList<>();
        geoPointList1.add(new GeoPoint(48.8583, 2.2944));
        geoPointList1.add(new GeoPoint(48.8583, 1.2944));
        geoPointList1.add(new GeoPoint(48.8583, 0.2944));
        geoPointList1.add(new GeoPoint(48.8583, -1.2944));

        PolylineSingleton polylineSingleton = PolylineSingleton.INSTANCE;

        polylineSingleton.getValue(mapView, geoPointList);
        polylineSingleton.getValue(mapView, geoPointList1);

        MarkerSingleton markerSingleton = MarkerSingleton.INSTANCE;

        singletonValue = markerSingleton.getValue(mapView, new GeoPoint(48.8583, 2.2944));
        markerSingleton.getValue(mapView, new GeoPoint(47.8583, 2.2944));
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
    public boolean singleTapConfirmedHelper(GeoPoint p) {

        InfoWindow.closeAllInfoWindowsOn(mapView);
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }
}

//For one marker: call marker.closeInfoWindow()
//For all markers: call InfoWindow.closeAllInfoWindowsOn(mapView)