package com.grsu.guideapp.fragments.map;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grsu.guideapp.R;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.fragments.map.MapContract.MapViews;
import com.grsu.guideapp.fragments.map_preview.MapPreviewFragment;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.utils.CheckSelfPermission;
import com.grsu.guideapp.utils.DataUtils;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.MessageViewer.MySnackbar;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import java.util.List;

public class MapFragment extends MapPreviewFragment<MapPresenter> implements MapViews {

    private static final String TAG = MapFragment.class.getSimpleName();
    private List<LatLng> myMovement;//deleting
    public static final String BR_ACTION = MapFragment.class.getName();

    private Marker marker;//deleting
    private Marker current;
    int i = 0;
    private BroadcastReceiver br;

    @BindView(R.id.tv_fragment_map_distance)
    TextView distanceTextView;

    @BindView(R.id.fragment_map_indicator)
    ImageView found;

    @NonNull
    @Override
    protected MapPresenter getPresenterInstance() {
        return new MapPresenter(this, new MapInteractor(new DatabaseHelper(getContext())));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            route = getArguments().getInt(Constants.KEY_ID_ROUTE, -1);
        }

        super.onCreateView(inflater, container, savedInstanceState);
        if (route != -1) {
            br = new MyBroadcastReceiver();
        }
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMovement = DataUtils.getList2();
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
    public void setCurrentPoint(LatLng latLng) {
        if (current == null) {
            MarkerOptions markerOptions = new MarkerOptions().position(latLng);
            current = mMap.addMarker(markerOptions);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        current.setPosition(latLng);
    }

    @Override
    public void show() {
        found.setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        found.setVisibility(View.GONE);
    }

    @Override
    public void openDialogViews() {
        /*FragmentManager manager = getChildFragmentManager();
        SingleChoiceItemsDialogFragment.newInstance(choiceItem)
                .show(manager, SingleChoiceItemsDialogFragment.getTags());

        MultiChoiceItemsDialogFragment.newInstance(read("Key", long[].class))
                .show(manager, MultiChoiceItemsDialogFragment.getTags());*/
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
            Location currentLocation = MapUtils.toLocation(myMovement.get(myMovement.size() - 1));
            mPresenter.getProjectionLocation(currentLocation);
        }
    }

    @Override
    public void onOk() {
        super.onOk();
        mPresenter.getPoi();
    }

    @Override
    public void choiceItem(String itemValue) {
        super.choiceItem(itemValue);
        mPresenter.getPoi();
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(Constants.KEY_GEO_POINT);
            Logs.e(TAG, String.valueOf(location));

            mPresenter.getProjectionLocation(location);
        }
    }

    public void showT(String s) {
        Toasts.makeS(getActivity, s);
    }

    public static MapFragment newInstance(Integer id_route) {
        Bundle args = new Bundle();
        args.putInt(Constants.KEY_ID_ROUTE, id_route);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
