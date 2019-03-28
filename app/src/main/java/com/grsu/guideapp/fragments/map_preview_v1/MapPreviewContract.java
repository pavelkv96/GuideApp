package com.grsu.guideapp.fragments.map_preview_v1;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.MenuItem;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import java.util.List;

public interface MapPreviewContract extends OnFinishedListener {

    interface MapPreviewViews extends BaseView {

        void setPolyline(List<LatLng> geoPointList);

        void setPointsTurn(LatLng geoPoint, @DrawableRes int icon);

        void setPoi(Poi poi);

        void removePoi();

        void initData();
    }

    interface MapPreviewPresenter extends BasePresenter<MapPreviewViews> {

        void getId(Integer id);

        void setRadius(Integer radius);

        void getAllPoi(boolean getAll);

        void onMarkerClick(Context context, Marker marker);

        void onPrepareOptionsMenu(MenuItem item);

        void onOk(MenuItem item);
    }

    interface MapPreviewInteractor {

        void getRouteById(OnFinishedListener<List<Line>> listener, Integer id);

        void getListPoi(OnFinishedListener<List<Poi>> listener, Integer id, String point,
                int radius);

        void getCountCheckedTypes(OnFinishedListener<Integer> listener);
    }
}
