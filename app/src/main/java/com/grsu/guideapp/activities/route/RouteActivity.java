package com.grsu.guideapp.activities.route;

import static com.grsu.guideapp.utils.ContextHolder.getContext;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import butterknife.BindView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteContract.RouteView;
import com.grsu.guideapp.base.BaseActivity;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.utils.Constants;
import com.grsu.guideapp.utils.MarkerSingleton;
import com.grsu.guideapp.utils.PolylineSingleton;
import java.util.List;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.StorageUtils;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class RouteActivity extends BaseActivity<RoutePresenter> implements RouteView {

    PolylineSingleton polylineSingleton = PolylineSingleton.Polyline;
    MarkerSingleton markerSingleton = MarkerSingleton.Marker;

    @BindView(R.id.mv_activity_route)
    MapView mapView;

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

        Integer route = (Integer) getIntent().getExtras().getSerializable(Constants.ROUTE);
        if (route != null) {
            mPresenter.getID(route);
            //mapView.setExpectedCenter(decodeP(mProductList.get(0).getPolyline()));
        }
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
        mapView.setUseDataConnection(false);
    }

    @Override
    public void setPolyLine(List<GeoPoint> geoPointList) {
        polylineSingleton.getValue(mapView, geoPointList);
    }

    @Override
    public void setPoints(GeoPoint geoPoint) {
        markerSingleton.getValue(mapView, geoPoint);
    }
}
