package com.grsu.guideapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Turn extends LatLong {

    @SerializedName("objects")
    @Expose
    private List<String> objects = null;
    private final static long serialVersionUID = 9098352641873441351L;

    /**
     * No args constructor for use in serialization
     */
    public Turn() {
    }

    /**
     *
     * @param objects
     * @param y
     * @param x
     */
    public Turn(String x, String y, List<String> objects) {
        super(x, y);
        this.objects = objects;
    }

    public List<String> getObjects() {
        return objects;
    }

    @Override
    public String toString() {
        return "Turn{" +
                "objects=" + objects +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                '}';
    }
}
