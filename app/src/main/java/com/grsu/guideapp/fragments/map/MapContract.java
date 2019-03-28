package com.grsu.guideapp.fragments.map;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.fragments.map_preview.MapPreviewContract.MapPreviewInteractor;
import com.grsu.guideapp.fragments.map_preview.MapPreviewContract.MapPreviewPresenter;
import com.grsu.guideapp.fragments.map_preview.MapPreviewContract.MapPreviewViews;

public interface MapContract extends OnFinishedListener {

    interface MapViews extends MapPreviewViews {

        void setCurrentPoint(LatLng geoPointList);

        void show();

        void hide();
    }

    interface MapsPresenter extends MapPreviewPresenter {

        void getProjectionLocation(Location location);

        void getPoi();
    }

    interface MapsInteractor extends MapPreviewInteractor {

    }
}
