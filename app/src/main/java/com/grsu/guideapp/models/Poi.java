package com.grsu.guideapp.models;

import java.io.Serializable;

public class Poi implements Serializable {

    public static final String ID = "id";

    public static final String LATITUDE = "Latitude";

    public static final String LONGITUDE = "Longitude";

    public static final String TYPE = "Type";

    public static final String SHORT_DESCRIPTION = "Short_description";

    public static final String PHOTO_REFERENCE = "Photo_reference";


    private Integer id;
    private Float latitude;
    private Float longitude;
    private Integer type;
    private String short_description;
    private String photo_reference;

    public Poi() {
    }

    public Poi(Integer id, Float latitude, Float longitude, Integer type,
            String short_description, String photo_reference) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.short_description = short_description;
        this.photo_reference = photo_reference;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getPhoto_reference() {
        return photo_reference;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }
}
