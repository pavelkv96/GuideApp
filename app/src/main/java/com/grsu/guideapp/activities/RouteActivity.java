package com.grsu.guideapp.activities;

import static com.grsu.guideapp.utils.ContextHolder.getContext;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.grsu.guideapp.R;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.utils.Constants;
import com.grsu.guideapp.utils.PolylineSingleton;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.StorageUtils;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class RouteActivity extends AppCompatActivity {

    @BindView(R.id.mv_activity_route)
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        ButterKnife.bind(this);

        Context ctx = this;
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setOsmdroidBasePath(StorageUtils.getStorage());

        mapView.setMaxZoomLevel(20.0);
        mapView.setMinZoomLevel(11.0);
        //mapView.setExpectedCenter(new GeoPoint(53.669353, 23.813131));
        mapView.getController().setZoom(11.0);
        mapView.setBuiltInZoomControls(false);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setUseDataConnection(false);

        Integer route = (Integer) getIntent().getExtras().getSerializable(Constants.ROUTE);
        if (route != null) {
            DatabaseHelper mDBHelper = new DatabaseHelper(getContext());
            List<Line> mProductList = mDBHelper.getRouteById(route);

            PolylineSingleton polylineSingleton = PolylineSingleton.INSTANCE;

            for (Line s : mProductList) {
                polylineSingleton.getValue(mapView, decode(s.getPolyline()));
            }
            mapView.setExpectedCenter(decode(mProductList.get(0).getPolyline()).get(0));
        }
    }


    public static List<GeoPoint> decode(final String encodedPath) {
        int length = encodedPath.length();

        final List<GeoPoint> path = new ArrayList<>();
        int index = 0;
        int latitude = 0;
        int longitude = 0;

        while (index < length) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            latitude += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            longitude += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new GeoPoint(latitude * 1e-5, longitude * 1e-5));
        }

        return path;
    }
}
