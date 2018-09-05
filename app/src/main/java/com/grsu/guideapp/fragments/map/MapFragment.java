package com.grsu.guideapp.fragments.map;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NONE;
import static com.grsu.guideapp.utils.constants.Constants.PROVIDER_MAPSFORGE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.database.CacheDBHelper;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.fragments.map.MapContract.MapViews;
import com.grsu.guideapp.mf.MapsForgeTileSource;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import com.grsu.guideapp.views.dialogs.CustomMultiChoiceItemsDialogFragment;
import com.grsu.guideapp.views.dialogs.CustomMultiChoiceItemsDialogFragment.OnMultiChoiceListDialogFragment;
import com.grsu.guideapp.views.dialogs.CustomSingleChoiceItemsDialogFragment;
import com.grsu.guideapp.views.dialogs.CustomSingleChoiceItemsDialogFragment.OnChoiceItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

public class MapFragment extends BaseFragment<MapPresenter> implements TileProvider,
        OnMapReadyCallback, MapViews, OnChoiceItemListener, OnMultiChoiceListDialogFragment,
        OnMyLocationChangeListener {

    private List<Marker> markers = new ArrayList<>();
    private List<LatLng> geoPoints;

    private Marker marker;
    private Marker current;
    int i = 0;

    private GoogleMap mMap;
    @BindView(R.id.tv_fragment_test_distance)
    TextView distanceTextView;

    @NonNull
    @Override
    protected MapPresenter getPresenterInstance() {
        return new MapPresenter(this, new MapInteractor(new DatabaseHelper(getContext())));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_test_animation;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context cxt = getContext();
        AndroidGraphicFactory.createInstance(getActivity().getApplication());
        new CacheDBHelper(cxt);

        File file = getActivity().getDatabasePath("KA.map");

        Toasts.makeL(cxt, "Loaded map file " + file.exists());

        if (file.exists()) {
            MapsForgeTileSource.createFromFiles(new File[]{file}, null, PROVIDER_MAPSFORGE);
        }

        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            mPresenter.getId(3);

            mPresenter.setRadius("100");

            openDialogViews();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        geoPoints = getList();
        mPresenter.onMapReady(googleMap);
    }


    @Override
    public Tile getTile(int x, int y, int zoom) {
        return mPresenter.getTile(x, y, zoom, PROVIDER_MAPSFORGE);
    }

    @Override
    public void onDestroyView() {
        CacheDBHelper.refreshDb();
        MapsForgeTileSource.dispose();
        AndroidGraphicFactory.clearResourceMemoryCache();
        super.onDestroyView();
    }

    @Override
    public void mapViewSettings(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(MAP_TYPE_NONE);

        LatLng sydney = new LatLng(53.899045, 23.624377);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 11));

        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(this));
    }

    @Override
    public void setPolyline(List<LatLng> geoPointList, int id) {
        PolylineOptions polylineOptions = new PolylineOptions()
                .color(Color.RED)
                .zIndex(1)
                .addAll(geoPointList);
        mMap.addPolyline(polylineOptions).setTag(id);
    }

    @Override
    public void setCurrentPoint(LatLng geoPointList) {
        current.setPosition(geoPointList);
    }

    @Override
    public void setPointsTurn(LatLng geoPoint) {
        MarkerOptions markerOptions = new MarkerOptions().position(geoPoint).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.a_marker));
        mMap.addMarker(markerOptions);
    }

    @Override
    public void setPoi(Poi poi) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(poi.getLatitude(), poi.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(MapUtils.getIconByType(poi.getType())))
                .snippet(poi.getId());
        markers.add(mMap.addMarker(markerOptions));
    }

    @Override
    public void removeMarkers() {
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
    }

    @Override
    public void openDialogViews() {
        CustomSingleChoiceItemsDialogFragment.newInstance(distanceTextView.getText())
                .show(getChildFragmentManager(), "CustomSingleChoiceItemsDialogFragment");

        CustomMultiChoiceItemsDialogFragment
                .newInstance((ArrayList<Integer>) mPresenter.getType())
                .show(getChildFragmentManager(), "CustomMultiChoiceItemsDialogFragment");
    }

    @OnClick(R.id.btn_fragment_test_settings)
    void openSettings(View view) {
        CustomMultiChoiceItemsDialogFragment.newInstance((ArrayList<Integer>) mPresenter.getType())
                .show(getChildFragmentManager(), "CustomMultiChoiceItemsDialogFragment");
    }

    @OnClick(R.id.tv_fragment_test_distance)
    void openDistanceSettings(View view) {
        CustomSingleChoiceItemsDialogFragment.newInstance(distanceTextView.getText())
                .show(getChildFragmentManager(), "CustomSingleChoiceItemsDialogFragment");
    }


    @OnClick(R.id.btn_fragment_test_clearmarker)
    public void ResetMarker(View view) {
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(geoPoints).zIndex(1)
                .color(Color.RED);
        mMap.addPolyline(polylineOptions);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.btn_fragment_test_start)
    public void start(View view) {
//        mMap.setOnMyLocationChangeListener(this);
        mMap.setMyLocationEnabled(true);
    }

    @OnClick(R.id.btn_fragment_test_next)
    public void next(View view) {
        if (i < geoPoints.size() - 1) {

            LatLng position = geoPoints.get(i);
            if (i == 0) {
                MarkerOptions markerOptions = new MarkerOptions().position(position);
                marker = mMap.addMarker(markerOptions);
                current = mMap.addMarker(markerOptions);
            }
            marker.setPosition(position);
            mPresenter.getLocation(MapUtils.toLocation(position));
            i++;
        } else {
            mPresenter.getLocation(MapUtils.toLocation(geoPoints.get(geoPoints.size() - 1)));
        }
    }

    @Override
    public void onOk(ArrayList<Integer> arrayList) {
        mPresenter.setType(arrayList);
        mPresenter.getMarkers();
    }

    @Override
    public void choiceItem(String itemValue) {
        distanceTextView.setText(itemValue);
        mPresenter.setRadius(itemValue);
        mPresenter.getMarkers();
    }

    private List<LatLng> getList() {
        List<LatLng> geoPointList = new ArrayList<>();
        geoPointList.add(new LatLng(53.684893333333335, 23.839983333333333));
        geoPointList.add(new LatLng(53.68499, 23.84001166666667));
        geoPointList.add(new LatLng(53.68506666666667, 23.840151666666664));
        geoPointList.add(new LatLng(53.68512166666666, 23.84031));
        geoPointList.add(new LatLng(53.685096666666674, 23.840480000000003));
        geoPointList.add(new LatLng(53.68514333333333, 23.840648333333334));
        geoPointList.add(new LatLng(53.685141666666674, 23.84080333333333));
        geoPointList.add(new LatLng(53.68521333333333, 23.84091));
        geoPointList.add(new LatLng(53.68531500000001, 23.840883333333334));
        geoPointList.add(new LatLng(53.68542333333333, 23.840858333333337));
        geoPointList.add(new LatLng(53.685525000000005, 23.84084833333333));
        geoPointList.add(new LatLng(53.68562166666667, 23.84086166666667));
        geoPointList.add(new LatLng(53.685718333333334, 23.840781666666665));
        geoPointList.add(new LatLng(53.685714999999995, 23.84062666666667));
        geoPointList.add(new LatLng(53.685681666666675, 23.840480000000003));
        geoPointList.add(new LatLng(53.68578833333333, 23.84042833333333));
        geoPointList.add(new LatLng(53.68588, 23.84045833333333));
        geoPointList.add(new LatLng(53.68597666666666, 23.840468333333334));
        geoPointList.add(new LatLng(53.68607, 23.84047333333333));
        geoPointList.add(new LatLng(53.68616166666667, 23.840475));
        geoPointList.add(new LatLng(53.68626166666666, 23.84046));
        geoPointList.add(new LatLng(53.68635833333333, 23.840421666666664));
        geoPointList.add(new LatLng(53.68646333333333, 23.840393333333335));
        geoPointList.add(new LatLng(53.68656166666666, 23.840376666666664));
        geoPointList.add(new LatLng(53.68665166666666, 23.840334999999996));
        geoPointList.add(new LatLng(53.68674666666667, 23.84031333333333));
        geoPointList.add(new LatLng(53.68683666666667, 23.84028));
        geoPointList.add(new LatLng(53.68692833333333, 23.84028));
        geoPointList.add(new LatLng(53.68701833333333, 23.84023666666667));
        geoPointList.add(new LatLng(53.68712333333333, 23.840198333333333));
        geoPointList.add(new LatLng(53.68722666666666, 23.840166666666665));
        geoPointList.add(new LatLng(53.687311666666666, 23.840111666666665));
        geoPointList.add(new LatLng(53.68730333333333, 23.839946666666666));
        geoPointList.add(new LatLng(53.68738499999999, 23.839883333333333));
        geoPointList.add(new LatLng(53.68748333333333, 23.839913333333335));
        geoPointList.add(new LatLng(53.68757000000001, 23.839861666666668));
        geoPointList.add(new LatLng(53.68767166666667, 23.839911666666662));
        geoPointList.add(new LatLng(53.68776166666667, 23.839959999999998));
        geoPointList.add(new LatLng(53.68786, 23.839971666666663));
        geoPointList.add(new LatLng(53.687954999999995, 23.839895000000002));
        geoPointList.add(new LatLng(53.68804166666667, 23.839826666666667));
        geoPointList.add(new LatLng(53.68812333333334, 23.839748333333333));
        geoPointList.add(new LatLng(53.68824166666666, 23.83973));
        geoPointList.add(new LatLng(53.688328333333324, 23.839690000000004));
        geoPointList.add(new LatLng(53.68843666666667, 23.839668333333332));
        geoPointList.add(new LatLng(53.68853166666666, 23.839618333333338));
        geoPointList.add(new LatLng(53.68862666666667, 23.839678333333335));
        geoPointList.add(new LatLng(53.68871166666666, 23.839735000000005));
        geoPointList.add(new LatLng(53.688813333333336, 23.839703333333333));
        geoPointList.add(new LatLng(53.688921666666666, 23.839655));
        geoPointList.add(new LatLng(53.689015000000005, 23.839634999999998));
        geoPointList.add(new LatLng(53.689105000000005, 23.839556666666667));
        geoPointList.add(new LatLng(53.68914499999999, 23.839386666666663));
        geoPointList.add(new LatLng(53.68916666666667, 23.83921666666667));
        geoPointList.add(new LatLng(53.689146666666666, 23.839063333333335));
        geoPointList.add(new LatLng(53.68915833333334, 23.83889166666667));
        geoPointList.add(new LatLng(53.68912666666667, 23.838716666666663));
        geoPointList.add(new LatLng(53.689093333333325, 23.838563333333333));
        geoPointList.add(new LatLng(53.68909999999999, 23.838393333333336));
        geoPointList.add(new LatLng(53.689135, 23.838251666666665));
        geoPointList.add(new LatLng(53.689150000000005, 23.8381));
        geoPointList.add(new LatLng(53.68911666666666, 23.837938333333334));
        geoPointList.add(new LatLng(53.68911833333333, 23.83778333333333));
        geoPointList.add(new LatLng(53.689139999999995, 23.837608333333332));
        geoPointList.add(new LatLng(53.68917666666666, 23.837460000000004));
        geoPointList.add(new LatLng(53.68914333333334, 23.83731833333333));
        geoPointList.add(new LatLng(53.68913666666667, 23.837163333333333));
        geoPointList.add(new LatLng(53.68912333333333, 23.83700666666667));
        geoPointList.add(new LatLng(53.68908666666666, 23.836836666666667));
        geoPointList.add(new LatLng(53.68909, 23.836675));
        geoPointList.add(new LatLng(53.68899666666666, 23.836591666666664));
        geoPointList.add(new LatLng(53.688916666666664, 23.836516666666665));
        geoPointList.add(new LatLng(53.688823333333325, 23.836455));
        geoPointList.add(new LatLng(53.68872666666666, 23.836388333333336));
        geoPointList.add(new LatLng(53.688644999999994, 23.83627333333333));
        geoPointList.add(new LatLng(53.688561666666665, 23.83618333333333));
        geoPointList.add(new LatLng(53.688471666666665, 23.83612833333333));
        geoPointList.add(new LatLng(53.68838666666667, 23.836063333333332));
        geoPointList.add(new LatLng(53.68828666666666, 23.83599));
        geoPointList.add(new LatLng(53.688193333333324, 23.835950000000004));
        geoPointList.add(new LatLng(53.68810666666666, 23.835776666666668));
        geoPointList.add(new LatLng(53.688023333333334, 23.835684999999998));
        geoPointList.add(new LatLng(53.687945000000006, 23.83560166666667));
        geoPointList.add(new LatLng(53.68793000000001, 23.835758333333334));
        geoPointList.add(new LatLng(53.687886666666664, 23.83560333333333));
        geoPointList.add(new LatLng(53.68781166666666, 23.835496666666668));
        geoPointList.add(new LatLng(53.68774333333334, 23.835395000000002));
        geoPointList.add(new LatLng(53.68765833333334, 23.835311666666666));
        geoPointList.add(new LatLng(53.68756166666667, 23.835268333333335));
        geoPointList.add(new LatLng(53.68747166666667, 23.83526));
        geoPointList.add(new LatLng(53.68736833333334, 23.83522833333333));
        geoPointList.add(new LatLng(53.687268333333336, 23.835279999999997));
        geoPointList.add(new LatLng(53.68716166666666, 23.835268333333335));
        geoPointList.add(new LatLng(53.68706, 23.835341666666668));
        geoPointList.add(new LatLng(53.686975, 23.835395000000002));
        geoPointList.add(new LatLng(53.68687833333333, 23.835354999999996));
        geoPointList.add(new LatLng(53.68675833333334, 23.835350000000002));
        geoPointList.add(new LatLng(53.686641666666674, 23.835345));
        geoPointList.add(new LatLng(53.68651833333333, 23.835298333333338));
        geoPointList.add(new LatLng(53.68643, 23.835351666666664));
        geoPointList.add(new LatLng(53.686344999999996, 23.83546666666667));
        geoPointList.add(new LatLng(53.686231666666664, 23.835429999999995));
        geoPointList.add(new LatLng(53.686139999999995, 23.83538666666667));
        geoPointList.add(new LatLng(53.68604666666667, 23.835339999999995));
        geoPointList.add(new LatLng(53.68597333333334, 23.835451666666668));
        geoPointList.add(new LatLng(53.68587833333333, 23.835486666666664));
        geoPointList.add(new LatLng(53.685775, 23.835501666666666));
        geoPointList.add(new LatLng(53.68569166666666, 23.83557166666667));
        geoPointList.add(new LatLng(53.68560333333333, 23.835635000000003));
        geoPointList.add(new LatLng(53.685505, 23.835675));
        geoPointList.add(new LatLng(53.68541833333334, 23.83572));
        geoPointList.add(new LatLng(53.68532833333334, 23.835676666666668));
        geoPointList.add(new LatLng(53.685245, 23.835590000000003));
        geoPointList.add(new LatLng(53.68513666666666, 23.83557166666667));
        geoPointList.add(new LatLng(53.685026666666666, 23.83561666666667));
        geoPointList.add(new LatLng(53.68494166666666, 23.835669999999997));
        geoPointList.add(new LatLng(53.68484166666667, 23.83570333333333));
        geoPointList.add(new LatLng(53.68474833333333, 23.83571833333333));
        geoPointList.add(new LatLng(53.68465666666666, 23.835773333333336));
        geoPointList.add(new LatLng(53.684525, 23.83572));
        geoPointList.add(new LatLng(53.68442, 23.835714999999997));
        geoPointList.add(new LatLng(53.68432666666666, 23.835676666666668));
        geoPointList.add(new LatLng(53.68422333333333, 23.835681666666666));
        geoPointList.add(new LatLng(53.684126666666664, 23.835698333333333));
        geoPointList.add(new LatLng(53.68403833333333, 23.835669999999997));
        geoPointList.add(new LatLng(53.68394333333334, 23.835654999999996));
        geoPointList.add(new LatLng(53.68383666666667, 23.835665000000002));
        geoPointList.add(new LatLng(53.68373, 23.835643333333337));
        geoPointList.add(new LatLng(53.68364499999999, 23.835699999999996));
        geoPointList.add(new LatLng(53.68367, 23.835879999999996));
        geoPointList.add(new LatLng(53.683685, 23.83604833333333));
        geoPointList.add(new LatLng(53.68373499999999, 23.836208333333335));
        geoPointList.add(new LatLng(53.68376833333333, 23.836371666666665));
        geoPointList.add(new LatLng(53.683801666666675, 23.836521666666666));
        geoPointList.add(new LatLng(53.68381833333333, 23.83667833333333));
        geoPointList.add(new LatLng(53.68387333333333, 23.83684166666667));
        geoPointList.add(new LatLng(53.68390666666666, 23.837029999999995));
        geoPointList.add(new LatLng(53.683933333333336, 23.837233333333334));
        geoPointList.add(new LatLng(53.68392666666667, 23.83739166666667));
        geoPointList.add(new LatLng(53.68396500000001, 23.837556666666668));
        geoPointList.add(new LatLng(53.68399333333333, 23.83771166666667));
        geoPointList.add(new LatLng(53.684025000000005, 23.83787));
        geoPointList.add(new LatLng(53.68406333333334, 23.83803833333333));
        geoPointList.add(new LatLng(53.684070000000006, 23.838195000000002));
        geoPointList.add(new LatLng(53.684085, 23.838365));
        geoPointList.add(new LatLng(53.68413166666666, 23.83851666666667));
        geoPointList.add(new LatLng(53.684175, 23.83866666666667));
        geoPointList.add(new LatLng(53.68424166666667, 23.838805));
        geoPointList.add(new LatLng(53.68428000000001, 23.838956666666665));
        geoPointList.add(new LatLng(53.68431833333334, 23.83911333333333));
        geoPointList.add(new LatLng(53.68434333333333, 23.83926166666667));
        geoPointList.add(new LatLng(53.68437833333334, 23.839401666666664));
        geoPointList.add(new LatLng(53.68440166666666, 23.839553333333335));
        geoPointList.add(new LatLng(53.684439999999995, 23.839691666666667));
        geoPointList.add(new LatLng(53.68452166666666, 23.839816666666668));
        geoPointList.add(new LatLng(53.684580000000004, 23.83993666666667));
        geoPointList.add(new LatLng(53.684643333333334, 23.840081666666666));
        geoPointList.add(new LatLng(53.68469333333334, 23.840235));

        return geoPointList;
    }

    @Override
    public void onMyLocationChange(Location location) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MapUtils.toLatLng(location), 18));
        mPresenter.getLocation(location);
    }
}
