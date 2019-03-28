package com.grsu.guideapp.network.model;

import com.grsu.guideapp.network.model.LatLong;
import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Objects extends LatLong implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("image")
    @Expose
    private String image;
    private final static long serialVersionUID = 1852842934375711334L;

    /**
     * No args constructor for use in serialization
     */
    public Objects() {
    }

    /**
     *
     * @param id
     * @param image
     */
    public Objects(String id, String image, String x, String y) {
        super(x, y);
        this.id = id;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
