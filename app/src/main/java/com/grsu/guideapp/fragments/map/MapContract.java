package com.grsu.guideapp.fragments.map;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.fragments.map_preview.MapPreviewContract.MapPreviewInteractor;
import com.grsu.guideapp.fragments.map_preview.MapPreviewContract.MapPreviewPresenter;
import com.grsu.guideapp.fragments.map_preview.MapPreviewContract.MapPreviewViews;
import com.grsu.guideapp.models.Poi;
import java.util.List;

public interface MapContract extends OnFinishedListener {

    interface MapViews extends MapPreviewViews {

        void openDialogViews();

        void setCurrentPoint(LatLng geoPointList);

        void show();

        void hide();

        void showT(String s);
    }

    interface MapPresenter extends MapPreviewPresenter {

        void getProjectionLocation(Location location);

        void getPoi();
    }

    interface MapInteractor extends MapPreviewInteractor {

        void getListPoi(OnFinishedListener<List<Poi>> listener, double latitude, double longitude,
                int radius, long[] typesObjects);
    }
}
