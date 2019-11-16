package com.grsu.guideapp.views.viewmodels;

import androidx.lifecycle.ViewModel;
import android.location.Location;
import com.google.android.gms.maps.model.CameraPosition;

public class TestViewModel extends ViewModel {

    private CameraPosition position;
    private Location myLocation;

    public void setMyLocation(Location myLocation) {
        this.myLocation = myLocation;
    }

    public Location getMyLocation() {
        return myLocation;
    }

    public void setPosition(CameraPosition position) {
        this.position = position;
    }

    public CameraPosition getPosition() {
        return position;
    }

    @Override
    protected void onCleared() {
        position = null;
        myLocation = null;
        super.onCleared();
    }
}
