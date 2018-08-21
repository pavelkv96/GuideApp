package com.grsu.guideapp.fragments.map;

import static com.grsu.guideapp.utils.Constants.KEY_GEO_POINT_1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
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
import com.grsu.guideapp.fragments.map.MapContract.MapViews;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.utils.MarkerSingleton;
import com.grsu.guideapp.utils.PolylineSingleton;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.StorageUtils;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Marker.OnMarkerClickListener;


public class MapFragment extends BaseFragment<MapPresenter> implements MapViews, MapEventsReceiver {

    private static final String TAG = MapFragment.class.getSimpleName();
    public final static String BR_ACTION = MapFragment.class.getPackage().toString();
    private MarkerSingleton markerSingleton = MarkerSingleton.Marker;
    private PolylineSingleton polylineSingleton = PolylineSingleton.Polyline;
    private List<Marker> markers = new ArrayList<>();
    private IMapController iMapController;
    private BroadcastReceiver br;

    @BindView(R.id.mv_fragment_map)
    MapView mapView;

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
        mPresenter.getId(1);

        br = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Location location = intent.getParcelableExtra(KEY_GEO_POINT_1);
                mapView.getController()
                        .setCenter(new GeoPoint(location.getLatitude(), location.getLongitude()));
                //mPresenter.getLocation(location);
            }
        };

        getActivity().registerReceiver(br, new IntentFilter(BR_ACTION));
    }

    @OnClick(R.id.btn_fragment_map_clearmarker)
    public void ResetMarker(View view) {
        getActivity().stopService(new Intent(getActivity(), MyService.class));
    }

    @OnClick(R.id.btn_fragment_map_start)
    public void start(View view) {
        iMapController.setZoom(20f);
        getActivity().startService(new Intent(getActivity(), MyService.class));
    }

    @OnClick(R.id.btn_fragment_map_next)
    public void next(View view) {

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
        mapView.getOverlays().add(new MapEventsOverlay(this));
        mapView.setScrollableAreaLimitDouble(new BoundingBox(53.7597, 23.9845, 53.5986, 23.7099));
        iMapController = new MapController(mapView);
    }

    @Override
    public void setPolyline(List<GeoPoint> geoPointList, int id) {
        polylineSingleton.getPolyline(mapView, geoPointList, id);
    }

    @Override
    public Marker setPoints(GeoPoint geoPoint) {
        Marker marker = markerSingleton.getMarker(mapView, geoPoint);
        marker.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.a_marker));
        return marker;
    }

    @Override
    public void setGetPoints(Poi poi) {
        markers.add(markerSingleton.getMarkerWithBubble(mapView, poi));
    }

    @Override
    public void removeMarkers() {
        markerSingleton.removeMarkers(mapView, markers);
    }

    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(br);
        getActivity().stopService(new Intent(getActivity(), MyService.class));
        super.onDestroyView();
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return mPresenter.singleTapConfirmedHelper(p);
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }
}
