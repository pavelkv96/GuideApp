package com.grsu.guideapp.fragments.map_preview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.base.BaseMapFragment;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.fragments.map_preview.MapPreviewContract.MapPreviewViews;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.utils.CryptoUtils;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import com.grsu.guideapp.views.dialogs.CustomMultiChoiceItemsDialogFragment;
import com.grsu.guideapp.views.dialogs.CustomMultiChoiceItemsDialogFragment.OnMultiChoiceListDialogFragment;
import com.grsu.guideapp.views.dialogs.CustomSingleChoiceItemsDialogFragment;
import com.grsu.guideapp.views.dialogs.CustomSingleChoiceItemsDialogFragment.OnChoiceItemListener;
import java.util.ArrayList;
import java.util.List;

public abstract class MapPreviewFragment<P extends MapPreviewPresenter> extends
        BaseMapFragment<P, RouteActivity>
        implements OnChoiceItemListener, MapPreviewViews, OnMultiChoiceListDialogFragment {

    private static final String TAG = MapPreviewFragment.class.getSimpleName();
    private List<Marker> nearPoi = new ArrayList<>();
    private List<Marker> turnPoint = new ArrayList<>();

    protected int route = -1;
    Menu menu;

    @BindView(R.id.tv_fragment_map_distance)
    TextView distanceTextView;

    @NonNull
    @Override
    protected P getPresenterInstance() {
        DatabaseHelper pDbHelper = new DatabaseHelper(getContext());
        return (P) new MapPreviewPresenter(this, new MapPreviewInteractor(pDbHelper));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_map;
    }

    @Override
    protected int getMap() {
        return R.id.fragment_map_map;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setHasOptionsMenu(true);

        if (route != -1) {
            mPresenter.getId(route);
            Integer choiceItem = read(Constants.KEY_SINGLE_CHOICE_ITEM, Integer.class);
            mPresenter.setType(read(Constants.KEY_MULTI_CHOICE_ITEMS, long[].class));
            mPresenter.setRadius(choiceItem);
            distanceTextView.setText(String.valueOf(choiceItem));
        } else {
            if (getActivity != null) {
                Toasts.makeL(getActivity, getString(R.string.error_please_refresh_your_database));
                getActivity.getSupportFragmentManager().popBackStack();
                getActivity.finish();
            }
        }
        return rootView;
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
    public void setPointsTurn(LatLng latLng, int icon) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(icon));
        turnPoint.add(mMap.addMarker(markerOptions));
    }

    @Override
    public void setPoi(Poi poi) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(CryptoUtils.decodeP(poi.getId()))
                .icon(BitmapDescriptorFactory.fromBitmap(poi.getIcon()));
        Marker marker = mMap.addMarker(markerOptions);
        marker.setTag(true);
        nearPoi.add(marker);
    }

    @Override
    public void removePoi() {
        for (Marker marker : nearPoi) {
            marker.remove();
        }
        nearPoi.clear();
    }

    @Override
    public void onOk(long[] arrayList) {
        mPresenter.setType(arrayList);

        if (arrayList.length != 0) {
            save(Constants.KEY_MULTI_CHOICE_ITEMS, arrayList);
            mPresenter.getAllPoi(menu.findItem(R.id.menu_fragment_map_get_all).isChecked());
        } else {
            remove(Constants.KEY_MULTI_CHOICE_ITEMS);
            removePoi();
        }
    }

    @Override
    public void choiceItem(String itemValue) {
        Integer choiceItem = Integer.valueOf(itemValue);
        save(Constants.KEY_SINGLE_CHOICE_ITEM, choiceItem);
        distanceTextView.setText(itemValue);
        mPresenter.setRadius(choiceItem);
        mPresenter.getAllPoi(menu.findItem(R.id.menu_fragment_map_get_all).isChecked());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        inflater.inflate(R.menu.menu_fragment_map, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.menu_fragment_map_get_all);
        long[] longs = read(Constants.KEY_MULTI_CHOICE_ITEMS, long[].class);
        if (longs != null && longs.length != 0) {
            item.setEnabled(true);
        } else {
            item.setEnabled(false);
            item.setChecked(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager manager = getChildFragmentManager();

        switch (item.getItemId()) {
            case R.id.menu_fragment_map_settings:
                CustomMultiChoiceItemsDialogFragment.newInstance(read(Constants.KEY_MULTI_CHOICE_ITEMS, long[].class))
                        .show(manager, CustomMultiChoiceItemsDialogFragment.getTags());
                break;
            case R.id.menu_fragment_map_distance:
                CustomSingleChoiceItemsDialogFragment.newInstance(read(Constants.KEY_SINGLE_CHOICE_ITEM, Integer.class))
                        .show(manager, CustomSingleChoiceItemsDialogFragment.getTags());
                break;
            case R.id.menu_fragment_map_get_all:
                boolean checked = !item.isChecked();
                item.setChecked(checked);
                mPresenter.getAllPoi(checked);
                break;
            case R.id.menu_fragment_map_hide_turn_point:
                boolean checkedTurn = !item.isChecked();
                item.setChecked(checkedTurn);
                mPresenter.hideTurn(checkedTurn);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mPresenter.onMarkerClick(getActivity, marker);
        return super.onMarkerClick(marker);
    }

    @Override
    public void moveToStart(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    @Override
    public void showTurn(boolean visibility){
        for (Marker marker : turnPoint) {
            marker.setVisible(visibility);
        }
    }
}
