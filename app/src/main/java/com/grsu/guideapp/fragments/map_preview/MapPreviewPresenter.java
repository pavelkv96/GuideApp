package com.grsu.guideapp.fragments.map_preview;

import android.content.Context;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.fragments.details.DetailsFragment;
import com.grsu.guideapp.fragments.map_preview.MapPreviewContract.MapPreviewViews;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import com.grsu.guideapp.models.Point;
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
    protected long[] types;

    @Override
    public void getId(Integer id) {
        this.id = id;
        mView.showProgress(null, "Loading...");
        mapInteractor.getRouteById(new OnFinishedListener<List<Line>>() {
            @Override
            public void onFinished(List<Line> encodePolylines) {

                List<Point> turnsList = getTurnsList(encodePolylines);
                int s = turnsList.size() - 1;

                mapViews.setPointsTurn(turnsList.get(0).getPosition(), R.drawable.ic_action_play);
                for (int i = 1; i < s; i++) {
                    mapViews.setPointsTurn(turnsList.get(i).getPosition(), R.drawable.a_marker);
                }
                mapViews.setPointsTurn(turnsList.get(s).getPosition(), R.drawable.ic_action_pause);

                for (Line line : encodePolylines) {
                    mapViews.setPolyline(CryptoUtils.decodeL(line.getPolyline()), line.getIdLine());
                }
                mView.hideProgress();
            }
        }, id);
    }

    @Override
    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    @Override
    public void setType(long[] typesObjects) {
        types = typesObjects;
    }

    @Override
    public void getAllPoi(boolean getAll) {
        if (types == null) {
            return;
        }

        mapViews.removePoi();
        if (getAll && types.length != 0) {
            mapInteractor.getListPoi(this, id, radius, types);
        }
    }

    @Override
    public void onMarkerClick(Context context, Marker marker) {
        Boolean isPoi = marker.getTag() != null ? (Boolean) marker.getTag() : false;
        if (isPoi) {
            String idPoint = CryptoUtils.encodeP(marker.getPosition());
            ((RouteActivity) context).onReplace(DetailsFragment.newInstance(idPoint));
        }
    }

    @Override
    public void onFinished(List<Poi> poiList) {
        mapViews.removePoi();
        for (Poi poi : poiList) {
            mapViews.setPoi(poi);
        }
    }

    private List<Point> getTurnsList(List<Line> encodePolylines) {
        List<Point> latLngs = new ArrayList<>();
        for (Line decodeLine : encodePolylines) {
            if (latLngs.isEmpty()) {
                LatLng position = CryptoUtils.decodeP(decodeLine.getStartPoint());
                latLngs.add(new Point(decodeLine.getIdLine(), position, -1f));
            }
            LatLng position = CryptoUtils.decodeP(decodeLine.getEndPoint());
            latLngs.add(new Point(decodeLine.getIdLine(), position, -1f));
        }

        return latLngs;
    }
}
