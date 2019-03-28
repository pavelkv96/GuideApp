package com.grsu.guideapp.fragments.map_preview_v1;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.base.BaseMapFragment;
import com.grsu.guideapp.fragments.map_preview_v1.MapPreviewContract.MapPreviewViews;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.utils.CryptoUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import com.grsu.guideapp.views.dialogs.MultiChoiceItemsDialogFragment;
import com.grsu.guideapp.views.dialogs.MultiChoiceItemsDialogFragment.OnMultiChoiceItemsListener;
import com.grsu.guideapp.views.dialogs.SingleChoiceItemsDialogFragment;
import com.grsu.guideapp.views.dialogs.SingleChoiceItemsDialogFragment.OnChoiceItemListener;
import java.util.ArrayList;
import java.util.List;

public abstract class MapPreviewFragment<P extends MapPreviewPresenter> extends
        BaseMapFragment<P, RouteActivity>
        implements OnChoiceItemListener, MapPreviewViews, OnMultiChoiceItemsListener,
        OnMapClickListener {

    private static final String TAG = MapPreviewFragment.class.getSimpleName();
    private List<Marker> nearPoi = new ArrayList<>();

    protected int route = -1;
    protected LatLngBounds bounds;
    protected Polyline polyline;
    Menu menu;

    @NonNull
    @Override
    protected P getPresenterInstance() {
        return (P) new MapPreviewPresenter(this, new MapPreviewInteractor(null));
    }

    @Override
    protected int getMap() {
        return R.id.map;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (route != -1) {
            mPresenter.getId(route);
        } else {
            if (getActivity != null) {
                Toasts.makeL(getActivity, getString(R.string.error_please_refresh_your_database));
                getActivity.getSupportFragmentManager().popBackStack();
                getActivity.finish();
            }
        }
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
    }

    //-------------------------------------------
    //	Summary: implements Contracts
    //-------------------------------------------

    @Override
    public void setPolyline(final List<LatLng> geoPointList) {
        PolylineOptions options = new PolylineOptions();
        options.color(Color.BLUE);
        options.width(20);
        options.zIndex(1);
        options.addAll(geoPointList);
        polyline = mMap.addPolyline(options);
    }

    @Override
    public void setPointsTurn(LatLng latLng, int icon) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.icon(BitmapDescriptorFactory.fromResource(icon));
        mMap.addMarker(options);
    }

    @Override
    public void setPoi(Poi poi) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(CryptoUtils.decodeP(poi.getId()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(poi.getIcon()));
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
    public void onOk() {
        mPresenter.onOk(menu.findItem(R.id.menu_fragment_map_get_all));
    }

    @Override
    public void choiceItem(String itemValue) {
        Integer choiceItem = Integer.valueOf(itemValue);
        save(Constants.KEY_SINGLE_CHOICE_ITEM, choiceItem);
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
        mPresenter.onPrepareOptionsMenu(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager manager = getChildFragmentManager();

        switch (item.getItemId()) {
            case R.id.menu_fragment_map_settings: {
                String tag = MultiChoiceItemsDialogFragment.getTags();
                MultiChoiceItemsDialogFragment.newInstance().show(manager, tag);
            }
            break;

            case R.id.menu_fragment_map_distance: {
                Integer read = read(Constants.KEY_SINGLE_CHOICE_ITEM, Integer.class);
                String tag = SingleChoiceItemsDialogFragment.getTags();
                SingleChoiceItemsDialogFragment.newInstance(read).show(manager, tag);
            }
            break;

            case R.id.menu_fragment_map_get_all: {
                boolean checked = !item.isChecked();
                item.setChecked(checked);
                mPresenter.getAllPoi(checked);
            }
            break;

            /*case R.id.menu_fragment_map_hide_turn_point: {
                boolean checkedTurn = !item.isChecked();
                item.setChecked(checkedTurn);
                mPresenter.hideTurn(checkedTurn);
            }
            break;*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mMap.getUiSettings().isTiltGesturesEnabled()) {
            mPresenter.onMarkerClick(getActivity, marker);
            return super.onMarkerClick(marker);
        }
        return true;
    }

    @Override
    public void initData() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
        Integer choiceItem = read(Constants.KEY_SINGLE_CHOICE_ITEM, Integer.class);
        mPresenter.setRadius(choiceItem);
        mPresenter.getAllPoi(true);
    }

    protected void setUI(boolean flag) {
        if (flag) {
            mMap.setOnMapClickListener(this);
        } else {
            mMap.setOnMapClickListener(null);
        }
        mMap.getUiSettings().setScrollGesturesEnabled(flag);
        mMap.getUiSettings().setAllGesturesEnabled(flag);
        mMap.getUiSettings().setTiltGesturesEnabled(flag);
    }

    @Override
    public void onDestroyView() {
        Logs.e(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onCameraIdle() {
        super.onCameraIdle();
        polyline.setWidth(mMap.getCameraPosition().zoom + 5);
    }
}
