package com.grsu.guideapp.models;

import java.io.Serializable;

public class Poi implements Serializable {

    public static final String ID = "id";

    public static final String LATITUDE = "Latitude";

    public static final String LONGITUDE = "Longitude";

    public static final String TYPE = "Type";


    private String id;
    private Float latitude;
    private Float longitude;
    private Integer type;

    public Poi() {
    }

    public Poi(String id, Float latitude, Float longitude, Integer type) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
