package com.grsu.guideapp.network.model;

import android.support.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Turn implements Serializable {

    @SerializedName("polyline")
    @Expose
    private String polyline;

    @SerializedName("start")
    @Expose
    private Point start;

    @SerializedName("end")
    @Expose
    private Point end;

    private final static long serialVersionUID = 9098352641873441351L;

    public Turn(String polyline, Point start, Point end) {
        this.polyline = polyline;
        this.start = start;
        this.end = end;
    }

    public String getPolyline() {
        return polyline;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    @NonNull
    @Override
    public String toString() {
        return "Turn1{" + "polyline='" + polyline + "\', start=" + start + ", end=" + end + '}';
    }
}
