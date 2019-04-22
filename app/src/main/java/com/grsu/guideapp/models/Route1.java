package com.grsu.guideapp.models;

import android.database.Cursor;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.CryptoUtils;
import java.io.File;
import java.io.Serializable;

public class Route1 implements Serializable {

    //POJO model
    private Integer idRoute;
    private Names nameRoute;
    private Integer duration;
    private Integer distance;
    private String referencePhotoRoute;
    private String southwest;
    private String northeast;
    private Long last_update;
    private Integer is_full;
    private final static long serialVersionUID = -4448199344994382402L;

    /**
     * No args constructor for use in serialization
     */
    public Route1() {
    }

    /**
     * @param idRoute the identifier route
     * @param nameRoute the name route
     * @param duration the duration the route
     * @param distance the distance the route
     * @param referencePhotoRoute the link photo the route
     */
    public Route1(Integer idRoute, Names nameRoute, Integer duration, Integer distance,
            String referencePhotoRoute, String southwest, String northeast, long last_update,
            int is_full) {
        this.idRoute = idRoute;
        this.nameRoute = nameRoute;
        this.duration = duration;
        this.distance = distance;
        this.referencePhotoRoute = referencePhotoRoute;
        this.southwest = southwest;
        this.northeast = northeast;
        this.last_update = last_update;
        this.is_full = is_full;
    }

    public Integer getIdRoute() {
        return idRoute;
    }

    public Names getNameRoute() {
        return nameRoute;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getDistance() {
        return distance;
    }

    public String getReferencePhotoRoute() {
        return referencePhotoRoute;
    }

    public String getSouthwest() {
        return southwest;
    }

    public String getNortheast() {
        return northeast;
    }

    public Long getLastUpdate() {
        return last_update;
    }

    public void setIsFull(boolean is_full) {
        this.is_full = is_full ? 1 : 0;
    }

    public boolean getIsFull() {
        return is_full == 1;
    }

    public File getPhotoPath() {
        String photo = CryptoUtils.hash(referencePhotoRoute);
        return new File(Settings.CONTENT, photo);
    }

    public static Route1 fromCursor(Cursor cur, Names names) {
        cur.moveToFirst();
        return new Route1(
                cur.getInt(0),
                names,
                cur.getInt(1),
                cur.getInt(2),
                cur.getString(3),
                cur.getString(4),
                cur.getString(5),
                cur.getLong(6),
                cur.getInt(7));
    }
}