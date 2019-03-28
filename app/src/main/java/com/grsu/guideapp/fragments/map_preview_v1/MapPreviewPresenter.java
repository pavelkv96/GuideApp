package com.grsu.guideapp.fragments.map_preview_v1;

import android.content.Context;
import android.view.MenuItem;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.fragments.details.DetailsFragment;
import com.grsu.guideapp.fragments.map_preview_v1.MapPreviewContract.MapPreviewViews;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.utils.CryptoUtils;
import java.util.ArrayList;
import java.util.List;

public class MapPreviewPresenter extends BasePresenterImpl<MapPreviewViews> implements
        MapPreviewContract.MapPreviewPresenter, OnFinishedListener<List<Poi>> {

    private MapPreviewViews mapViews;
    private MapPreviewInteractor mapInteractor;

    public MapPreviewPresenter(MapPreviewViews mapViews, MapPreviewInteractor mapInteractor) {
        this.mapViews = mapViews;
        this.mapInteractor = mapInteractor;
    }

    private static final String TAG = MapPreviewPresenter.class.getSimpleName();

    protected int id;
    protected int radius;

    @Override
    public void getId(Integer id) {
        this.id = id;
        mView.showProgress(null, "Loading...");
        mapInteractor.getRouteById(new OnFinishedListener<List<Line>>() {
            @Override
            public void onFinished(List<Line> encodePolylines) {
                int s = encodePolylines.size() - 1;
                LatLng startPosition = CryptoUtils.decodeP(encodePolylines.get(0).getStartPoint());
                LatLng endPosition = CryptoUtils.decodeP(encodePolylines.get(s).getEndPoint());

                mapViews.initData();
                mapViews.setPointsTurn(startPosition, R.drawable.a_marker);
                mapViews.setPointsTurn(endPosition, R.drawable.b_marker);
                mapViews.setPolyline(getTurnsList(encodePolylines));

                mView.hideProgress();
            }
        }, id);
    }

    @Override
    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    @Override
    public void getAllPoi(final boolean getAll) {
        mapInteractor.getCountCheckedTypes(new OnFinishedListener<Integer>() {
            @Override
            public void onFinished(Integer count) {
                mapViews.removePoi();
                if (getAll && count != 0) {
                    mapInteractor.getListPoi(MapPreviewPresenter.this, id, null, radius);
                }
            }
        });

    }

    @Override
    public void onMarkerClick(Context context, Marker marker) {
        boolean isPoi = marker.getTag() != null ? (Boolean) marker.getTag() : false;
        if (isPoi) {
            String idPoint = CryptoUtils.encodeP(marker.getPosition());
            ((RouteActivity) context).onReplace(DetailsFragment.newInstance(idPoint));
        }
    }

    @Override
    public void onPrepareOptionsMenu(final MenuItem item) {
        mapInteractor.getCountCheckedTypes(new OnFinishedListener<Integer>() {
            @Override
            public void onFinished(Integer count) {
                if (count != 0) {
                    item.setEnabled(true);
                } else {
                    item.setEnabled(false);
                    item.setChecked(false);
                }
            }
        });
    }

    @Override
    public void onOk(final MenuItem item) {
        mapInteractor.getCountCheckedTypes(new OnFinishedListener<Integer>() {
            @Override
            public void onFinished(Integer count) {
                if (count != 0) {
                    getAllPoi(item.isChecked());
                } else {
                    mapViews.removePoi();
                }
            }
        });
    }

    @Override
    public void onFinished(List<Poi> poiList) {
        mapViews.removePoi();
        for (Poi poi : poiList) {
            mapViews.setPoi(poi);
        }
    }

    private List<LatLng> getTurnsList(List<Line> encodePolylines) {
        List<LatLng> latLngs = new ArrayList<>();
        for (Line decodeLine : encodePolylines) {
            latLngs.addAll(CryptoUtils.decodeL(decodeLine.getPolyline()));
        }

        return latLngs;
    }
}
