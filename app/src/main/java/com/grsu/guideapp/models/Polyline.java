package com.grsu.guideapp.models;

import androidx.annotation.NonNull;

public class Polyline {

    private String start;
    private String end;
    private String polyline;

    public Polyline() {
    }

    public Polyline(String start, String end, String polyline) {
        this.start = start;
        this.end = end;
        this.polyline = polyline;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    @NonNull
    @Override
    public String toString() {
        return "Polyline{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", polyline='" + polyline + '\'' +
                '}';
    }
}

