
package com.grsu.guideapp.network.model;

import com.google.android.gms.maps.model.LatLng;
import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatLong implements Serializable {

    @SerializedName("x")
    @Expose
    protected String x;
    @SerializedName("y")
    @Expose
    protected String y;
    private final static long serialVersionUID = 9098352641873441350L;

    /**
     * No args constructor for use in serialization
     */
    public LatLong() {
    }

    /**
     *
     * @param y
     * @param x
     */
    public LatLong(String x, String y) {
        super();
        this.x = x;
        this.y = y;
    }

    public LatLng getLatLng() {
        double lat = Double.valueOf(x);
        double lng = Double.valueOf(y);
        return new LatLng(lat, lng);
    }
}
