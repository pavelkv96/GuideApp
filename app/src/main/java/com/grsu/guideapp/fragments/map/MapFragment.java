package com.grsu.guideapp.fragments.map;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.base.BaseMapFragment;
import com.grsu.guideapp.database.CacheDBHelper;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.fragments.map.MapContract.MapViews;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.models.Tag;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.utils.CheckSelfPermission;
import com.grsu.guideapp.utils.DataUtils;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.MessageViewer.MySnackbar;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import com.grsu.guideapp.views.dialogs.CustomMultiChoiceItemsDialogFragment;
import com.grsu.guideapp.views.dialogs.CustomMultiChoiceItemsDialogFragment.OnMultiChoiceListDialogFragment;
import com.grsu.guideapp.views.dialogs.CustomSingleChoiceItemsDialogFragment;
import com.grsu.guideapp.views.dialogs.CustomSingleChoiceItemsDialogFragment.OnChoiceItemListener;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends BaseMapFragment<MapPresenter, RouteActivity>
        implements OnChoiceItemListener, MapViews, OnMultiChoiceListDialogFragment {

    private static final String TAG = MapFragment.class.getSimpleName();
    //private RouteActivity getActivity = null;
    private List<Marker> nearPoi = new ArrayList<>();
    private List<LatLng> myMovement;//deleting
    public static final String BR_ACTION = MapFragment.class.getName();
    private Menu menu;


    private Marker marker;//deleting
    private Integer choiceItem;
    private Marker current;
    int i = 0;
    private BroadcastReceiver br;

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
    protected int getFragment() {
        return R.id.fragment_map_map;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setHasOptionsMenu(true);
        Integer route = -1;

        if (getArguments() != null) {
            route = getArguments().getInt(Constants.KEY_ID_ROUTE);
        }

        if (route != -1) {
            mPresenter.getId(route);
            choiceItem = 1000;
            mPresenter.setRadius(choiceItem);
            distanceTextView.setText(String.valueOf(choiceItem));
            //openDialogViews();
            br = new MyBroadcastReceiver();
        } else {
            if (getActivity != null) {
                Toasts.makeL(getActivity, "Empty data, please refresh your database");
                getActivity.getSupportFragmentManager().popBackStack();
                getActivity.finish();
            }
        }
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMovement = DataUtils.getList();
        super.onMapReady(googleMap);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterListeners();
        Logs.e(TAG, "onStop");
    }

    //-------------------------------------------
    //	Summary: implements Contracts
    //-------------------------------------------

    @Override
    public void setPolyline(List<LatLng> geoPointList, int id) {
        PolylineOptions polylineOptions = new PolylineOptions()
                .color(Color.BLACK)
                .zIndex(1)
                .addAll(geoPointList);
        mMap.addPolyline(polylineOptions).setTag(id);
    }

    @Override
    public void setCurrentPoint(LatLng latLng) {
        if (current == null) {
            MarkerOptions markerOptions = new MarkerOptions().position(latLng);
            current = mMap.addMarker(markerOptions);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        current.setPosition(latLng);
    }

    @Override
    public void setPointsTurn(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.a_marker));
        mMap.addMarker(markerOptions);
    }

    @Override
    public void setPoi(Poi poi) {
        int icon = MapUtils.getIconByType(poi.getType());
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(poi.getLatitude(), poi.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(icon))
                .draggable(true);
        Marker e = mMap.addMarker(markerOptions);
        e.setTag(new Tag(true, poi.getId()));
        nearPoi.add(e);
    }

    @Override
    public void removePoi() {
        for (Marker marker : nearPoi) {
            marker.remove();
        }
        nearPoi.clear();
    }

    @Override
    public void setStartMarker(LatLng startMarker) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(startMarker)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_play));
        mMap.addMarker(markerOptions);
    }

    @Override
    public void setEndMarker(LatLng endMarker) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(endMarker)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_pause));
        mMap.addMarker(markerOptions);
    }

    @Override
    public void openDialogViews() {
        CustomSingleChoiceItemsDialogFragment.newInstance(choiceItem)
                .show(getChildFragmentManager(), "CustomSingleChoiceItemsDialogFragment");

        CustomMultiChoiceItemsDialogFragment.newInstance(mPresenter.getType())
                .show(getChildFragmentManager(), "CustomMultiChoiceItemsDialogFragment");
    }

    @Override
    public void onOk(long[] arrayList) {
        if (arrayList.length != 0) {
            menu.findItem(R.id.menu_fragment_map_get_all).setEnabled(true);
        } else {
            menu.findItem(R.id.menu_fragment_map_get_all).setEnabled(false);
            menu.findItem(R.id.menu_fragment_map_get_all).setChecked(false);
            removePoi();
        }

        mPresenter.setType(arrayList);
        mPresenter.getPoi();
        mPresenter.getAllPoi();
    }

    @Override
    public void choiceItem(String itemValue) {
        choiceItem = Integer.valueOf(itemValue);
        distanceTextView.setText(itemValue);
        mPresenter.setRadius(choiceItem);
        mPresenter.getPoi();
        mPresenter.getAllPoi();
    }

    private void unregisterListeners() {
        if (br != null) {
            try {
                getActivity.unregisterReceiver(br);
            } catch (IllegalArgumentException ignore) {
            } finally {
                getActivity.stopService(new Intent(getActivity, MyService.class));
            }
        }
    }

    //-------------------------------------------
    //	Summary: implements OnClicks
    //-------------------------------------------

    @OnClick(R.id.btn_fragment_map_stop)
    public void stopService(View view) {
        unregisterListeners();
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.btn_fragment_map_start)
    public void startService(View view) {
        if (br != null && !CheckSelfPermission.getAccessLocationIsGranted(getContext())) {
            getActivity.registerReceiver(br, new IntentFilter(BR_ACTION));
            getActivity.startService(new Intent(getContext(), MyService.class));
            mMap.setMyLocationEnabled(true);
        } else {
            MySnackbar.makeL(view, R.string.error_do_not_have_permission, getActivity);
        }
    }

    @OnClick(R.id.btn_fragment_map_next)
    public void next(View view) {
        if (i < myMovement.size() - 1) {

            LatLng position = myMovement.get(i);
            if (i == 0) {
                MarkerOptions markerOptions = new MarkerOptions().position(position);
                marker = mMap.addMarker(markerOptions);
                current = mMap.addMarker(markerOptions);
            }
            marker.setPosition(position);
            mPresenter.getProjectionLocation(MapUtils.toLocation(position));
            i++;
        } else {
            mPresenter.getProjectionLocation(
                    MapUtils.toLocation(myMovement.get(myMovement.size() - 1)));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        getActivity.getMenuInflater().inflate(R.menu.menu_fragment_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_fragment_map_settings:
                CustomMultiChoiceItemsDialogFragment.newInstance(mPresenter.getType())
                        .show(getChildFragmentManager(), "CustomMultiChoiceItemsDialogFragment");
                break;
            case R.id.menu_fragment_map_distance:
                CustomSingleChoiceItemsDialogFragment.newInstance(choiceItem)
                        .show(getChildFragmentManager(), "CustomSingleChoiceItemsDialogFragment");
                break;
            case R.id.menu_fragment_map_get_all:
                boolean checked = !item.isChecked();
                item.setChecked(checked);
                mPresenter.setAllPoi(checked);
                break;
            case R.id.menu_fragment_map_update_tiles:
                overlay.clearTileCache();
                break;
            case R.id.menu_fragment_map_clear_cache:
                CacheDBHelper.clearCache();
                overlay.clearTileCache();
                break;
            case R.id.menu_fragment_map_clear:
                CacheDBHelper.clearCache();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mPresenter.onMarkerClick(getActivity, marker);
        return super.onMarkerClick(marker);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(Constants.KEY_GEO_POINT);
            Logs.e(TAG, String.valueOf(location));

            mPresenter.getProjectionLocation(location);
        }
    }

    public static MapFragment newInstance(Integer id_route) {
        Bundle args = new Bundle();
        args.putInt(Constants.KEY_ID_ROUTE, id_route);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
