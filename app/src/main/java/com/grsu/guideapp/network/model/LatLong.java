
package com.grsu.guideapp.network.model;

import androidx.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.grsu.guideapp.utils.CryptoUtils;
import java.io.Serializable;
import java.util.List;

public class LatLong implements Serializable {

    @SerializedName("lat")
    @Expose
    protected Double lat;
    @SerializedName("lng")
    @Expose
    protected Double lng;

    private final static long serialVersionUID = 9098352641873441350L;

    /**
     * No args constructor for use in serialization
     */
    public LatLong() {
    }

    public LatLong(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }

    public String getCryptoLatLng() {
        return CryptoUtils.encodeP(new LatLng(lat, lng));
    }

    @NonNull
    @Override
    public String toString() {
        return "LatLong{lat=" + lat + ", lng=" + lng + '}';
    }
}
