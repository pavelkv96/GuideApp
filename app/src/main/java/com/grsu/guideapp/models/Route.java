package com.grsu.guideapp.models;

import android.database.Cursor;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.CryptoUtils;
import java.io.File;
import java.io.Serializable;

public class Route implements Serializable {

    //POJO model
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
    public Route() {
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
        String photo = CryptoUtils.hash(referencePhotoRoute);
        return new File(Settings.CONTENT, photo);
    }

    public static Route fromCursor(Cursor cur) {
        Names name = new Names();
        name.setShortName(cur.getString(1));
        name.setFullName(cur.getString(2));
        name.setShortDescription(cur.getString(3));
        name.setFullDescription(cur.getString(4));

        Route route = new Route();

        route.setIdRoute(cur.getInt(0));
        route.setNameRoute(name);
        route.setDuration(cur.getInt(5));
        route.setDistance(cur.getInt(6));
        route.setReferencePhotoRoute(cur.getString(7));
        route.setSouthwest(cur.getString(8));
        route.setNortheast(cur.getString(9));
        route.setIsFull(cur.getInt(10));
        return route;
    }
}