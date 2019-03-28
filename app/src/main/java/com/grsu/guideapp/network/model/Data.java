package com.grsu.guideapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {

    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("about")
    @Expose
    private About about;
    @SerializedName("route")
    @Expose
    private Route route;
    private final static long serialVersionUID = -2713204178998794488L;

    /**
     * No args constructor for use in serialization
     */
    public Data() {
    }

    /**
     *
     * @param route
     * @param name
     * @param about
     * @param images
     */
    public Data(Name name, List<Image> images, About about, Route route) {
        super();
        this.name = name;
        this.images = images;
        this.about = about;
        this.route = route;
    }

    public Name getName() {
        return name;
    }

    public List<Image> getImages() {
        return images;
    }

    public About getAbout() {
        return about;
    }

    public Route getRoute() {
        return route;
    }
}
