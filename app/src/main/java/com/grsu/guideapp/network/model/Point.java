package com.grsu.guideapp.network.model;

import android.support.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.grsu.guideapp.utils.CryptoUtils;
import java.util.List;

public class Point extends LatLong {

    @SerializedName("objects")
    @Expose
    protected List<Integer> objects = null;

    private final static long serialVersionUID = 9094352641873441350L;

    /**
     * No args constructor for use in serialization
     */
    public Point() {
    }

    public Point(Double lat, Double lng, List<Integer> objects) {
        super(lat, lng);
        this.objects = objects;
    }

    public List<Integer> getObjects() {
        return objects;
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
        return "LatLong{lat=" + lat + ", lng=" + lng + ", objects=" + objects + '}';
    }
}
