package com.grsu.guideapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Value implements Serializable {

    @SerializedName("lim_left")
    @Expose
    private LatLong limLeft;
    @SerializedName("lim_right")
    @Expose
    private LatLong limRight;
    @SerializedName("points")
    @Expose
    private List<Turn> points = null;
    @SerializedName("objects")
    @Expose
    private List<Objects> objects = null;
    private final static long serialVersionUID = 1820689508389648513L;

    /**
     * No args constructor for use in serialization
     */
    public Value() {
    }

    /**
     *
     * @param limRight
     * @param points
     * @param limLeft
     * @param objects
     */
    public Value(LatLong limLeft, LatLong limRight, List<Turn> points, List<Objects> objects) {
        super();
        this.limLeft = limLeft;
        this.limRight = limRight;
        this.points = points;
        this.objects = objects;
    }

    public LatLong getLimLeft() {
        return limLeft;
    }

    public LatLong getLimRight() {
        return limRight;
    }

    public List<Turn> getPoints() {
        return points;
    }

    public List<Objects> getObjects() {
        return objects;
    }
}
