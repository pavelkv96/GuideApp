package com.grsu.guideapp.fragments.map;

import static com.grsu.guideapp.utils.constants.Constants.KEY_GEO_POINT;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.fragments.map.MapContract.MapViews;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.utils.MarkerSingleton;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import com.grsu.guideapp.utils.PolylineSingleton;
import com.grsu.guideapp.views.dialogs.CustomMultiChoiceItemsDialogFragment;
import com.grsu.guideapp.views.dialogs.CustomMultiChoiceItemsDialogFragment.OnMultiChoiceListDialogFragment;
import com.grsu.guideapp.views.dialogs.CustomSingleChoiceItemsDialogFragment;
import com.grsu.guideapp.views.dialogs.CustomSingleChoiceItemsDialogFragment.OnChoiceItemListener;
import java.util.ArrayList;
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

public class MapFragment extends BaseFragment<MapPresenter> implements OnChoiceItemListener,
        MapViews, OnMultiChoiceListDialogFragment {

    private static final String TAG = MapFragment.class.getSimpleName();
    public static final String BR_ACTION = MapFragment.class.getPackage().toString();
    private MarkerSingleton markerSingleton = MarkerSingleton.Marker;
    private PolylineSingleton polylineSingleton = PolylineSingleton.Polyline;

    private List<Marker> markers = new ArrayList<>();
    private List<Polyline> polylines = new ArrayList<>();
    private List<GeoPoint> geoPoints;

    private IMapController iMapController;
    private BroadcastReceiver br;
    private Marker marker;
    int i = 0;

    @BindView(R.id.mv_fragment_map)
    MapView mapView;

    @BindView(R.id.tv_fragment_map_distance)
    TextView distanceTextView;

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
        mPresenter.getId(3);

        mPresenter.setRadius("100");

        br = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Location location = intent.getParcelableExtra(KEY_GEO_POINT);
                Toasts.makeS(getContext(), String.valueOf(
                        (float) location.getLatitude() + "; " + (float) location.getLongitude()));
                mapView.getController()
                        .setCenter(new GeoPoint(location.getLatitude(), location.getLongitude()));

                if (marker == null) {
                    marker = markerSingleton.getMarker(mapView, MapPresenter.toGeoPoint(location));
                } else {
                    marker.setPosition(MapPresenter.toGeoPoint(location));
                }
                mPresenter.getLocation(location);
            }
        };

        openDialogViews();
    }

    @OnClick(R.id.btn_fragment_map_clearmarker)
    public void ResetMarker(View view) {
        Polyline polyline = polylineSingleton.getPolyline(mapView, geoPoints);
        polyline.setColor(Color.RED);
        //stopped();
    }

    @OnClick(R.id.btn_fragment_map_start)
    public void start(View view) {
        iMapController.setZoom(20f);
        iMapController.setCenter(new GeoPoint(53.684632, 23.839899));
        /*getActivity().registerReceiver(br, new IntentFilter(BR_ACTION));
        getActivity().startService(new Intent(getActivity(), MyService.class));*/
    }

    @OnClick(R.id.btn_fragment_map_next)
    public void next(View view) {
        if (i < geoPoints.size() - 1) {

            GeoPoint position = geoPoints.get(i);
            if (i == 0) {
                marker = markerSingleton.getMarker(mapView, position);
            }
            marker.setPosition(position);
            mapView.invalidate();
            mPresenter.getLocation(mPresenter.toLocation(position));
            i++;
        }
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
        //mapView.getOverlays().add(new MapEventsOverlay(this));
        mapView.setScrollableAreaLimitDouble(new BoundingBox(53.7597, 23.9845, 53.5986, 23.7099));
        iMapController = new MapController(mapView);
        geoPoints = getList();
    }

    @Override
    public void setPolyline(List<GeoPoint> geoPointList, int id) {
        polylineSingleton.getPolyline(mapView, geoPointList, id);
    }

    @Override
    public Polyline setPolyline(GeoPoint geoPointList) {
        List<GeoPoint> list = new ArrayList<>();
        list.add(geoPointList);
        return polylineSingleton.getPolyline(mapView, list);
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
    public void setGetPolyline(List<GeoPoint> geoPointList) {
        polylines.add(polylineSingleton.getPolyline(mapView, geoPointList));
    }

    @Override
    public void removeMarkers() {
        markerSingleton.removeMarkers(mapView, markers);
    }

    @Override
    public void stopped() {
        try {
            getActivity().unregisterReceiver(br);
            getActivity().stopService(new Intent(getActivity(), MyService.class));
        } catch (NullPointerException ignore) {
        } catch (IllegalArgumentException ignore) {
        }
    }

    @Override
    public void removePolylines() {
        polylineSingleton.removePolylines(mapView, polylines);
    }

    @Override
    public void openDialogViews() {
        if (this.getFragmentManager() != null) {
            CustomSingleChoiceItemsDialogFragment.newInstance(distanceTextView.getText())
                    .show(getChildFragmentManager(), "CustomSingleChoiceItemsDialogFragment");

            CustomMultiChoiceItemsDialogFragment
                    .newInstance((ArrayList<Integer>) mPresenter.getType())
                    .show(getChildFragmentManager(), "CustomMultiChoiceItemsDialogFragment");
        }
    }

    @Override
    public void onDestroyView() {
        stopped();
        super.onDestroyView();
    }


    private List<GeoPoint> getList() {
        List<GeoPoint> geoPointList = new ArrayList<>();
        geoPointList.add(new GeoPoint(53.684893333333335, 23.839983333333333, 0.0));
        geoPointList.add(new GeoPoint(53.68499, 23.84001166666667, 0.0));
        geoPointList.add(new GeoPoint(53.68506666666667, 23.840151666666664, 0.0));
        geoPointList.add(new GeoPoint(53.68512166666666, 23.84031, 0.0));
        geoPointList.add(new GeoPoint(53.685096666666674, 23.840480000000003, 0.0));
        geoPointList.add(new GeoPoint(53.68514333333333, 23.840648333333334, 0.0));
        geoPointList.add(new GeoPoint(53.685141666666674, 23.84080333333333, 0.0));
        geoPointList.add(new GeoPoint(53.68521333333333, 23.84091, 0.0));
        geoPointList.add(new GeoPoint(53.68531500000001, 23.840883333333334, 0.0));
        geoPointList.add(new GeoPoint(53.68542333333333, 23.840858333333337, 0.0));
        geoPointList.add(new GeoPoint(53.685525000000005, 23.84084833333333, 0.0));
        geoPointList.add(new GeoPoint(53.68562166666667, 23.84086166666667, 0.0));
        geoPointList.add(new GeoPoint(53.685718333333334, 23.840781666666665, 0.0));
        geoPointList.add(new GeoPoint(53.685714999999995, 23.84062666666667, 0.0));
        geoPointList.add(new GeoPoint(53.685681666666675, 23.840480000000003, 0.0));
        geoPointList.add(new GeoPoint(53.68578833333333, 23.84042833333333, 0.0));
        geoPointList.add(new GeoPoint(53.68588, 23.84045833333333, 0.0));
        geoPointList.add(new GeoPoint(53.68597666666666, 23.840468333333334, 0.0));
        geoPointList.add(new GeoPoint(53.68607, 23.84047333333333, 0.0));
        geoPointList.add(new GeoPoint(53.68616166666667, 23.840475, 0.0));
        geoPointList.add(new GeoPoint(53.68626166666666, 23.84046, 0.0));
        geoPointList.add(new GeoPoint(53.68635833333333, 23.840421666666664, 0.0));
        geoPointList.add(new GeoPoint(53.68646333333333, 23.840393333333335, 0.0));
        geoPointList.add(new GeoPoint(53.68656166666666, 23.840376666666664, 0.0));
        geoPointList.add(new GeoPoint(53.68665166666666, 23.840334999999996, 0.0));
        geoPointList.add(new GeoPoint(53.68674666666667, 23.84031333333333, 0.0));
        geoPointList.add(new GeoPoint(53.68683666666667, 23.84028, 0.0));
        geoPointList.add(new GeoPoint(53.68692833333333, 23.84028, 0.0));
        geoPointList.add(new GeoPoint(53.68701833333333, 23.84023666666667, 0.0));
        geoPointList.add(new GeoPoint(53.68712333333333, 23.840198333333333, 0.0));
        geoPointList.add(new GeoPoint(53.68722666666666, 23.840166666666665, 0.0));
        geoPointList.add(new GeoPoint(53.687311666666666, 23.840111666666665, 0.0));
        geoPointList.add(new GeoPoint(53.68730333333333, 23.839946666666666, 0.0));
        geoPointList.add(new GeoPoint(53.68738499999999, 23.839883333333333, 0.0));
        geoPointList.add(new GeoPoint(53.68748333333333, 23.839913333333335, 0.0));
        geoPointList.add(new GeoPoint(53.68757000000001, 23.839861666666668, 0.0));
        geoPointList.add(new GeoPoint(53.68767166666667, 23.839911666666662, 0.0));
        geoPointList.add(new GeoPoint(53.68776166666667, 23.839959999999998, 0.0));
        geoPointList.add(new GeoPoint(53.68786, 23.839971666666663, 0.0));
        geoPointList.add(new GeoPoint(53.687954999999995, 23.839895000000002, 0.0));
        geoPointList.add(new GeoPoint(53.68804166666667, 23.839826666666667, 0.0));
        geoPointList.add(new GeoPoint(53.68812333333334, 23.839748333333333, 0.0));
        geoPointList.add(new GeoPoint(53.68824166666666, 23.83973, 0.0));
        geoPointList.add(new GeoPoint(53.688328333333324, 23.839690000000004, 0.0));
        geoPointList.add(new GeoPoint(53.68843666666667, 23.839668333333332, 0.0));
        geoPointList.add(new GeoPoint(53.68853166666666, 23.839618333333338, 0.0));
        geoPointList.add(new GeoPoint(53.68862666666667, 23.839678333333335, 0.0));
        geoPointList.add(new GeoPoint(53.68871166666666, 23.839735000000005, 0.0));
        geoPointList.add(new GeoPoint(53.688813333333336, 23.839703333333333, 0.0));
        geoPointList.add(new GeoPoint(53.688921666666666, 23.839655, 0.0));
        geoPointList.add(new GeoPoint(53.689015000000005, 23.839634999999998, 0.0));
        geoPointList.add(new GeoPoint(53.689105000000005, 23.839556666666667, 0.0));
        geoPointList.add(new GeoPoint(53.68914499999999, 23.839386666666663, 0.0));
        geoPointList.add(new GeoPoint(53.68916666666667, 23.83921666666667, 0.0));
        geoPointList.add(new GeoPoint(53.689146666666666, 23.839063333333335, 0.0));
        geoPointList.add(new GeoPoint(53.68915833333334, 23.83889166666667, 0.0));
        geoPointList.add(new GeoPoint(53.68912666666667, 23.838716666666663, 0.0));
        geoPointList.add(new GeoPoint(53.689093333333325, 23.838563333333333, 0.0));
        geoPointList.add(new GeoPoint(53.68909999999999, 23.838393333333336, 0.0));
        geoPointList.add(new GeoPoint(53.689135, 23.838251666666665, 0.0));
        geoPointList.add(new GeoPoint(53.689150000000005, 23.8381, 0.0));
        geoPointList.add(new GeoPoint(53.68911666666666, 23.837938333333334, 0.0));
        geoPointList.add(new GeoPoint(53.68911833333333, 23.83778333333333, 0.0));
        geoPointList.add(new GeoPoint(53.689139999999995, 23.837608333333332, 0.0));
        geoPointList.add(new GeoPoint(53.68917666666666, 23.837460000000004, 0.0));
        geoPointList.add(new GeoPoint(53.68914333333334, 23.83731833333333, 0.0));
        geoPointList.add(new GeoPoint(53.68913666666667, 23.837163333333333, 0.0));
        geoPointList.add(new GeoPoint(53.68912333333333, 23.83700666666667, 0.0));
        geoPointList.add(new GeoPoint(53.68908666666666, 23.836836666666667, 0.0));
        geoPointList.add(new GeoPoint(53.68909, 23.836675, 0.0));
        geoPointList.add(new GeoPoint(53.68899666666666, 23.836591666666664, 0.0));
        geoPointList.add(new GeoPoint(53.688916666666664, 23.836516666666665, 0.0));
        geoPointList.add(new GeoPoint(53.688823333333325, 23.836455, 0.0));
        geoPointList.add(new GeoPoint(53.68872666666666, 23.836388333333336, 0.0));
        geoPointList.add(new GeoPoint(53.688644999999994, 23.83627333333333, 0.0));
        geoPointList.add(new GeoPoint(53.688561666666665, 23.83618333333333, 0.0));
        geoPointList.add(new GeoPoint(53.688471666666665, 23.83612833333333, 0.0));
        geoPointList.add(new GeoPoint(53.68838666666667, 23.836063333333332, 0.0));
        geoPointList.add(new GeoPoint(53.68828666666666, 23.83599, 0.0));
        geoPointList.add(new GeoPoint(53.688193333333324, 23.835950000000004, 0.0));
        geoPointList.add(new GeoPoint(53.68810666666666, 23.835776666666668, 0.0));
        geoPointList.add(new GeoPoint(53.688023333333334, 23.835684999999998, 0.0));
        geoPointList.add(new GeoPoint(53.687945000000006, 23.83560166666667, 0.0));
        geoPointList.add(new GeoPoint(53.68793000000001, 23.835758333333334, 0.0));
        geoPointList.add(new GeoPoint(53.687886666666664, 23.83560333333333, 0.0));
        geoPointList.add(new GeoPoint(53.68781166666666, 23.835496666666668, 0.0));
        geoPointList.add(new GeoPoint(53.68774333333334, 23.835395000000002, 0.0));
        geoPointList.add(new GeoPoint(53.68765833333334, 23.835311666666666, 0.0));
        geoPointList.add(new GeoPoint(53.68756166666667, 23.835268333333335, 0.0));
        geoPointList.add(new GeoPoint(53.68747166666667, 23.83526, 0.0));
        geoPointList.add(new GeoPoint(53.68736833333334, 23.83522833333333, 0.0));
        geoPointList.add(new GeoPoint(53.687268333333336, 23.835279999999997, 0.0));
        geoPointList.add(new GeoPoint(53.68716166666666, 23.835268333333335, 0.0));
        geoPointList.add(new GeoPoint(53.68706, 23.835341666666668, 0.0));
        geoPointList.add(new GeoPoint(53.686975, 23.835395000000002, 0.0));
        geoPointList.add(new GeoPoint(53.68687833333333, 23.835354999999996, 0.0));
        geoPointList.add(new GeoPoint(53.68675833333334, 23.835350000000002, 0.0));
        geoPointList.add(new GeoPoint(53.686641666666674, 23.835345, 0.0));
        geoPointList.add(new GeoPoint(53.68651833333333, 23.835298333333338, 0.0));
        geoPointList.add(new GeoPoint(53.68643, 23.835351666666664, 0.0));
        geoPointList.add(new GeoPoint(53.686344999999996, 23.83546666666667, 0.0));
        geoPointList.add(new GeoPoint(53.686231666666664, 23.835429999999995, 0.0));
        geoPointList.add(new GeoPoint(53.686139999999995, 23.83538666666667, 0.0));
        geoPointList.add(new GeoPoint(53.68604666666667, 23.835339999999995, 0.0));
        geoPointList.add(new GeoPoint(53.68597333333334, 23.835451666666668, 0.0));
        geoPointList.add(new GeoPoint(53.68587833333333, 23.835486666666664, 0.0));
        geoPointList.add(new GeoPoint(53.685775, 23.835501666666666, 0.0));
        geoPointList.add(new GeoPoint(53.68569166666666, 23.83557166666667, 0.0));
        geoPointList.add(new GeoPoint(53.68560333333333, 23.835635000000003, 0.0));
        geoPointList.add(new GeoPoint(53.685505, 23.835675, 0.0));
        geoPointList.add(new GeoPoint(53.68541833333334, 23.83572, 0.0));
        geoPointList.add(new GeoPoint(53.68532833333334, 23.835676666666668, 0.0));
        geoPointList.add(new GeoPoint(53.685245, 23.835590000000003, 0.0));
        geoPointList.add(new GeoPoint(53.68513666666666, 23.83557166666667, 0.0));
        geoPointList.add(new GeoPoint(53.685026666666666, 23.83561666666667, 0.0));
        geoPointList.add(new GeoPoint(53.68494166666666, 23.835669999999997, 0.0));
        geoPointList.add(new GeoPoint(53.68484166666667, 23.83570333333333, 0.0));
        geoPointList.add(new GeoPoint(53.68474833333333, 23.83571833333333, 0.0));
        geoPointList.add(new GeoPoint(53.68465666666666, 23.835773333333336, 0.0));
        geoPointList.add(new GeoPoint(53.684525, 23.83572, 0.0));
        geoPointList.add(new GeoPoint(53.68442, 23.835714999999997, 0.0));
        geoPointList.add(new GeoPoint(53.68432666666666, 23.835676666666668, 0.0));
        geoPointList.add(new GeoPoint(53.68422333333333, 23.835681666666666, 0.0));
        geoPointList.add(new GeoPoint(53.684126666666664, 23.835698333333333, 0.0));
        geoPointList.add(new GeoPoint(53.68403833333333, 23.835669999999997, 0.0));
        geoPointList.add(new GeoPoint(53.68394333333334, 23.835654999999996, 0.0));
        geoPointList.add(new GeoPoint(53.68383666666667, 23.835665000000002, 0.0));
        geoPointList.add(new GeoPoint(53.68373, 23.835643333333337, 0.0));
        geoPointList.add(new GeoPoint(53.68364499999999, 23.835699999999996, 0.0));
        geoPointList.add(new GeoPoint(53.68367, 23.835879999999996, 0.0));
        geoPointList.add(new GeoPoint(53.683685, 23.83604833333333, 0.0));
        geoPointList.add(new GeoPoint(53.68373499999999, 23.836208333333335, 0.0));
        geoPointList.add(new GeoPoint(53.68376833333333, 23.836371666666665, 0.0));
        geoPointList.add(new GeoPoint(53.683801666666675, 23.836521666666666, 0.0));
        geoPointList.add(new GeoPoint(53.68381833333333, 23.83667833333333, 0.0));
        geoPointList.add(new GeoPoint(53.68387333333333, 23.83684166666667, 0.0));
        geoPointList.add(new GeoPoint(53.68390666666666, 23.837029999999995, 0.0));
        geoPointList.add(new GeoPoint(53.683933333333336, 23.837233333333334, 0.0));
        geoPointList.add(new GeoPoint(53.68392666666667, 23.83739166666667, 0.0));
        geoPointList.add(new GeoPoint(53.68396500000001, 23.837556666666668, 0.0));
        geoPointList.add(new GeoPoint(53.68399333333333, 23.83771166666667, 0.0));
        geoPointList.add(new GeoPoint(53.684025000000005, 23.83787, 0.0));
        geoPointList.add(new GeoPoint(53.68406333333334, 23.83803833333333, 0.0));
        geoPointList.add(new GeoPoint(53.684070000000006, 23.838195000000002, 0.0));
        geoPointList.add(new GeoPoint(53.684085, 23.838365, 0.0));
        geoPointList.add(new GeoPoint(53.68413166666666, 23.83851666666667, 0.0));
        geoPointList.add(new GeoPoint(53.684175, 23.83866666666667, 0.0));
        geoPointList.add(new GeoPoint(53.68424166666667, 23.838805, 0.0));
        geoPointList.add(new GeoPoint(53.68428000000001, 23.838956666666665, 0.0));
        geoPointList.add(new GeoPoint(53.68431833333334, 23.83911333333333, 0.0));
        geoPointList.add(new GeoPoint(53.68434333333333, 23.83926166666667, 0.0));
        geoPointList.add(new GeoPoint(53.68437833333334, 23.839401666666664, 0.0));
        geoPointList.add(new GeoPoint(53.68440166666666, 23.839553333333335, 0.0));
        geoPointList.add(new GeoPoint(53.684439999999995, 23.839691666666667, 0.0));
        geoPointList.add(new GeoPoint(53.68452166666666, 23.839816666666668, 0.0));
        geoPointList.add(new GeoPoint(53.684580000000004, 23.83993666666667, 0.0));
        geoPointList.add(new GeoPoint(53.684643333333334, 23.840081666666666, 0.0));
        geoPointList.add(new GeoPoint(53.68469333333334, 23.840235, 0.0));

        return geoPointList;
    }

    @Override
    public void onOk(ArrayList<Integer> arrayList) {
        mPresenter.setType(arrayList);
        mPresenter.getMarkers();
        mapView.invalidate();
    }

    @Override
    public void choiceItem(String itemValue) {
        distanceTextView.setText(itemValue);
        mPresenter.setRadius(itemValue);
        mPresenter.getMarkers();
        mapView.invalidate();
    }

    @OnClick(R.id.btn_fragment_map_settings)
    void openSettings(View view) {
        CustomMultiChoiceItemsDialogFragment.newInstance((ArrayList<Integer>) mPresenter.getType())
                .show(getChildFragmentManager(), "CustomMultiChoiceItemsDialogFragment");
    }

    @OnClick(R.id.tv_fragment_map_distance)
    void openDistanceSettings(View view) {
        CustomSingleChoiceItemsDialogFragment.newInstance(distanceTextView.getText())
                .show(getChildFragmentManager(), "CustomSingleChoiceItemsDialogFragment");
    }
}
