package com.grsu.guideapp.activities.route;

import static com.grsu.guideapp.utils.ContextHolder.getContext;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteContract.RouteView;
import com.grsu.guideapp.base.BaseActivity;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.utils.Constants;
import com.grsu.guideapp.utils.MarkerSingleton;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.PolylineSingleton;
import com.grsu.guideapp.views.dialogs.CustomMultiChoiceItemsDialogFragment;
import com.grsu.guideapp.views.dialogs.CustomMultiChoiceItemsDialogFragment.OnMultiChoiceListDialogFragment;
import com.grsu.guideapp.views.dialogs.CustomSingleChoiceItemsDialogFragment;
import com.grsu.guideapp.views.dialogs.CustomSingleChoiceItemsDialogFragment.OnChoiceItemListener;
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
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.Polyline.OnClickListener;

public class RouteActivity extends BaseActivity<RoutePresenter> implements OnMarkerClickListener,
        RouteView, OnClickListener, MapEventsReceiver, OnChoiceItemListener,
        OnMultiChoiceListDialogFragment {

    private static final String TAG = RouteActivity.class.getSimpleName();

    private PolylineSingleton polylineSingleton = PolylineSingleton.Polyline;
    private MarkerSingleton markerSingleton = MarkerSingleton.Marker;
    private List<Marker> markers = new ArrayList<>();
    private List<Polyline> polylines = new ArrayList<>();

    @BindView(R.id.mv_activity_route)
    MapView mapView;

    @BindView(R.id.tv_activity_route_distance)
    TextView distanceTextView;

    @NonNull
    @Override
    protected RoutePresenter getPresenterInstance() {
        return new RoutePresenter(this, new RouteInteractor(new DatabaseHelper(getContext())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        mapViewSettings();

        Integer route = (Integer) getIntent().getSerializableExtra(Constants.KEY_ID_ROUTE);
        if (route != null) {
            mPresenter.getId(route);
        }
        mPresenter.setRadius("100");
    }

    @Override
    public void mapViewSettings() {
        Context ctx = this;
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setOsmdroidBasePath(StorageUtils.getStorage());

        mapView.setMaxZoomLevel(20.0);
        mapView.setMinZoomLevel(13.0);
        mapView.setScrollableAreaLimitDouble(new BoundingBox(53.7597, 23.9845, 53.5986, 23.7099));
        mapView.getController().setZoom(13.0);
        mapView.setBuiltInZoomControls(true);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setUseDataConnection(true);
        mapView.getOverlays().add(new MapEventsOverlay(this));

        /*double latitude = 53.5986;
        double longitude = 23.7100;
        float distance;
        Location crntLocation = new Location("crntlocation");
        crntLocation.setLatitude(53.5986); //1km ~ 0.0090
        crntLocation.setLongitude(23.7100);//1km ~ 0.0150

        Location newLocation = new Location("newlocation");
        newLocation.setLatitude(latitude);
        newLocation.setLongitude(longitude);

        distance = crntLocation.distanceTo(newLocation) / 1000; // in km
        Logs.e("TAG", String.valueOf(distance));*/
    }

    @Override
    public void setPolyline(List<GeoPoint> geoPointList) {
        polylineSingleton.getValue(mapView, geoPointList).setOnClickListener(this);
    }

    @Override
    public void setPoints(GeoPoint geoPoint) {
        markerSingleton.getMarker(mapView, geoPoint).setOnMarkerClickListener(this);
    }

    @Override
    public void setGetPoints(Poi poi) {
        markers.add(markerSingleton.getMarkerWithBubble(mapView, poi));
    }

    @Override
    public void setGetPolyline(List<GeoPoint> geoPointList) {
        polylines.add(polylineSingleton.getValue(mapView, geoPointList));
    }

    @Override
    public void removeMarker() {
        markerSingleton.removeMarkers(mapView, markers);
    }

    @Override
    public void removePolylines() {
        polylineSingleton.removePolylines(mapView, polylines);
    }

    @Override
    public void openDialogFragment() {
        CustomSingleChoiceItemsDialogFragment.newInstance(distanceTextView.getText())
                .show(this.getSupportFragmentManager(), "CustomSingleChoiceItemsDialogFragment");
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        Logs.e(TAG, marker.getPosition().toString());
        //removeMarker();
        return mPresenter.onMarkerClick(marker, mapView);
    }

    @Override
    public boolean onClick(Polyline polyline, MapView mapView, GeoPoint eventPos) {
        return mPresenter.onClickPolyline(polyline, mapView, eventPos);
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
    public void choiceItem(String itemValue) {
        distanceTextView.setText(itemValue);
        mPresenter.setRadius(itemValue);
        mPresenter.getMarkers(null);
        mapView.invalidate();
    }

    @Override
    public void onOk(ArrayList<Integer> arrayList) {
        mPresenter.setType(arrayList);
        mPresenter.getMarkers(null);
        mapView.invalidate();
    }

    @OnClick(R.id.btn_activity_route_settings)
    void openSettings(View view) {
        CustomMultiChoiceItemsDialogFragment.newInstance((ArrayList<Integer>) mPresenter.getType())
                .show(this.getSupportFragmentManager(), "CustomMultiChoiceItemsDialogFragment");
    }

    @OnClick(R.id.tv_activity_route_distance)
    void openDistanceSettings(View view) {
        openDialogFragment();
    }
}
