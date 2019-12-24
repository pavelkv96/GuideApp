package com.grsu.guideapp.models;

import android.database.Cursor;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.extensions.StringKt;

import java.io.File;
import java.io.Serializable;

public class DtoRoute implements Serializable {

    private Integer idRoute;
    private Names nameRoute;
    private Integer duration;
    private Integer distance;
    private String referencePhotoRoute;
    private String southwest;
    private String northeast;
    private Integer is_full;
    private final static long serialVersionUID = -4447199344994382402L;

    /**
     * No args constructor for use in serialization
     */
    public DtoRoute() {
    }

    public Integer getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(Integer idRoute) {
        this.idRoute = idRoute;
    }

    public Names getNameRoute() {
        return nameRoute;
    }

    public void setNameRoute(Names nameRoute) {
        this.nameRoute = nameRoute;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getReferencePhotoRoute() {
        return referencePhotoRoute;
    }

    public void setReferencePhotoRoute(String referencePhotoRoute) {
        this.referencePhotoRoute = referencePhotoRoute;
    }

    public String getSouthwest() {
        return southwest;
    }

    public void setSouthwest(String southwest) {
        this.southwest = southwest;
    }

    public String getNortheast() {
        return northeast;
    }

    public void setNortheast(String northeast) {
        this.northeast = northeast;
    }

    public void setIsFull(Integer is_full) {
        this.is_full = is_full;
    }

    public Integer getIsFull() {
        return is_full;
    }

    public File getPhotoPath() {
        return new File(Settings.CONTENT, StringKt.toMD5(referencePhotoRoute));
    }

    public static DtoRoute fromCursor(Cursor cur) {
        Names name = new Names();
        name.setName(cur.getString(1));
        name.setDescription(cur.getString(2));

        DtoRoute route = new DtoRoute();

        route.setIdRoute(cur.getInt(0));
        route.setNameRoute(name);
        route.setDuration(cur.getInt(3));
        route.setDistance(cur.getInt(4));
        route.setReferencePhotoRoute(cur.getString(5));
        route.setSouthwest(cur.getString(6));
        route.setNortheast(cur.getString(7));
        route.setIsFull(cur.getInt(8));
        return route;
    }
}