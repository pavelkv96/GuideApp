package com.grsu.guideapp.fragments.map_preview;

import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.grsu.guideapp.App;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.details.DetailsActivity;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.base.BaseMapFragment;
import com.grsu.guideapp.fragments.map_preview.MapPreviewContract.MapPreviewViews;
import com.grsu.guideapp.models.DtoObject;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.utils.CryptoUtils;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import com.grsu.guideapp.views.dialogs.MultiChoiceItemsDialogFragment;
import com.grsu.guideapp.views.dialogs.MultiChoiceItemsDialogFragment.OnMultiChoiceItemsListener;
import com.grsu.guideapp.views.dialogs.SingleChoiceItemsDialogFragment;
import com.grsu.guideapp.views.dialogs.SingleChoiceItemsDialogFragment.OnChoiceItemListener;
import com.grsu.guideapp.views.viewmodels.TestViewModel;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public abstract class MapPreviewFragment<P extends MapPreviewPresenter> extends
        BaseMapFragment<P, RouteActivity>
        implements MapPreviewViews, OnChoiceItemListener, OnMultiChoiceItemsListener,
        OnMapClickListener {

    private List<Marker> nearPoi = new ArrayList<>();
    private List<Marker> turnPoint = new ArrayList<>();
    private TestViewModel model;

    protected int route = -1;
    protected LatLngBounds bounds;
    Menu menu;

//    @BindView(R.id.tv_fragment_map_distance)
    TextView distanceTextView;

//    @BindView(R.id.rl_map_info)
    RelativeLayout rlMapInfo;

//    @BindView(R.id.tv_map_name)
    TextView nameTextView;

//    @BindView(R.id.iv_map_image)
    ImageView image;
    private String name = null;

    @Override
    protected String getTitle() {
        return (name == null || name.isEmpty()) ? getString(R.string.map_fragment) : name;
    }

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

        Bundle bundle = getArguments();

        if (bundle != null) {
            route = bundle.getInt(Constants.KEY_ID_ROUTE, -1);
            name =  bundle.getString(Constants.KEY_NAME_ROUTE);
            LatLng southwest = CryptoUtils.decodeP(bundle.getString(Constants.KEY_SOUTHWEST));
            LatLng northeast = CryptoUtils.decodeP(bundle.getString(Constants.KEY_NORTHEAST));
            bounds = new LatLngBounds(southwest, northeast);
        }

        model = ViewModelProviders.of(this).get(TestViewModel.class);

        setHasOptionsMenu(true);

        if (route != -1) {
            getActivity.setTitleToolbar(getTitle());
            mPresenter.getId(route);
            Integer choiceItem = read(Constants.KEY_SINGLE_CHOICE_ITEM, Integer.class);
            mPresenter.setRadius(choiceItem);
            mPresenter.getAllPoi(true);
            distanceTextView.setText(getString(R.string.radius_in_meters, choiceItem));
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
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        mMap.setOnMapClickListener(this);
        if (model.getPosition() != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(model.getPosition()));
            myLocation.setLocation(model.getMyLocation());
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
            model.setPosition(mMap.getCameraPosition());
            model.setMyLocation(myLocation.getMyLocation(Location.class));
        }
    }

    @Override
    public void setPolyline(List<LatLng> geoPointList, int id) {
        PolylineOptions polylineOptions = new PolylineOptions()
                .color(Color.BLUE)
                .width(15)
                .zIndex(1)
                .startCap(new SquareCap()).endCap(new SquareCap())
                .addAll(geoPointList);
        mMap.addPolyline(polylineOptions).setTag(id);
    }

    @Override
    public void setPointsTurn(LatLng latLng, int icon) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.visible(false);
        if (icon != -1) {
            markerOptions.visible(true);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(icon));
        }
        turnPoint.add(mMap.addMarker(markerOptions));
    }

    @Override
    public void setPoi(Poi poi) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(poi.getLocation());
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(poi.getIcon()));
        Marker marker = mMap.addMarker(markerOptions);
        marker.setTag(poi.getId());
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
        MenuItem item = menu.findItem(R.id.menu_fragment_map_get_all);
        mPresenter.onOk(item);
    }

    @Override
    public void choiceItem(String itemValue) {
        Integer choiceItem = Integer.valueOf(itemValue);
        save(Constants.KEY_SINGLE_CHOICE_ITEM, choiceItem);
        distanceTextView.setText(getString(R.string.radius_in_meters, choiceItem));
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

            case R.id.menu_fragment_map_hide_turn_point: {
                boolean checkedTurn = !item.isChecked();
                item.setChecked(checkedTurn);
                mPresenter.hideTurn(checkedTurn);
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (model != null) {
            model.setPosition(mMap.getCameraPosition());
            model.setMyLocation(myLocation.getMyLocation(Location.class));
        }
        mPresenter.onMarkerClick(getActivity, marker);
        return true;
    }

    @Override
    public void showTurn(boolean visibility) {
        for (Marker marker : turnPoint) {
            marker.setVisible(visibility);
        }
    }

//    @OnClick(R.id.btn_map_go_to)
    void onClickGoTo() {
        if (rlMapInfo != null && rlMapInfo.getTag() != null) {
            if (App.isOnline()) {
                LatLng lL = ((DtoObject) rlMapInfo.getTag()).getLocation();
                String s = String
                        .format("google.navigation:q=%s,%s&mode=w", lL.latitude, lL.longitude);
                Uri gmmIntentUri = Uri.parse(s);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getActivity.getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            } else {
                showToast(R.string.no_internet_connection);
            }
        }
    }

//    @OnClick(R.id.btn_map_more)
    void onClickMore() {
        if (rlMapInfo != null && rlMapInfo.getTag() != null) {
            DetailsActivity.newInstance(getContext(), ((DtoObject) rlMapInfo.getTag()).getId());
        }
    }

//    @OnClick(R.id.btn_map_audio)
    void onClickAudio() {
        if (rlMapInfo != null && rlMapInfo.getTag() != null) {
            showToast("Находится в разработке");
        }
    }

    @Override
    public void setInfoData(DtoObject object) {
        if (rlMapInfo != null) {
            rlMapInfo.setTag(object);
        }
        if (image != null) {
            Picasso.get().load(object.getPhoto()).into(image);
        }
        if (nameTextView != null) {
            nameTextView.setText(object.getName());
        }
    }

    @Override
    public void showInfo() {
        if (rlMapInfo != null) {
            rlMapInfo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideInfo() {
        if (rlMapInfo != null) {
            rlMapInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        hideInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity.getWindow().addFlags(FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onPause() {
        getActivity.getWindow().clearFlags(FLAG_KEEP_SCREEN_ON);
        super.onPause();
    }
}
