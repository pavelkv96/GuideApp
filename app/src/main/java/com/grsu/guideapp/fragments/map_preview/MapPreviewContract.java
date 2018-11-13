package com.grsu.guideapp.fragments.map_preview;

import android.content.Context;
import android.location.Location;
import android.support.annotation.DrawableRes;
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

        void setPolyline(List<LatLng> geoPointList, int id);

        void setPointsTurn(LatLng geoPoint, @DrawableRes int icon);

        void setPoi(Poi poi);

        void removePoi();
    }

    interface MapPreviewPresenter extends BasePresenter<MapPreviewViews> {

        void getId(Integer id);

        void setRadius(Integer radius);

        void setType(long[] typesObjects);

        void getAllPoi(boolean getAll);

        void onMarkerClick(Context context, Marker marker);
    }

    interface MapPreviewInteractor {

        void getRouteById(OnFinishedListener<List<Line>> listener, Integer id);

        void getListPoi(OnFinishedListener<List<Poi>> listener, Integer id, long[] typesObjects);
    }
}
