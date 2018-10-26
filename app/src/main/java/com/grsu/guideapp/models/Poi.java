package com.grsu.guideapp.models;

import android.database.Cursor;
import java.io.Serializable;

public class Poi implements Serializable {

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

    public static Poi fromCursor(Cursor cursor) {
        return new Poi(cursor.getString(0), cursor.getFloat(1),
                cursor.getFloat(2), cursor.getInt(3));
    }
}
