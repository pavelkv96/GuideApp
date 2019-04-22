package com.grsu.guideapp.network.model;

import android.support.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Objects extends LatLong implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("categories")
    @Expose
    private List<Integer> categories;
    private final static long serialVersionUID = 1852842934375711334L;

    /**
     * No args constructor for use in serialization
     */
    public Objects(Integer id) {
        this.id = id;
    }

    /**
     *
     * @param id
     * @param image
     */
    public Objects(Integer id, String image, String x, String y, List<Integer> categories) {
        this.id = id;
        this.image = image;
        this.categories = categories;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getCategories() {
        return categories.get(0);
    }

    public void setCategories(List<Integer> categories) {
        this.categories = new ArrayList<>(categories);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Objects objects = (Objects) o;
        return id.equals(objects.getId());
    }

    @Override
    public int hashCode() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return "Objects{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", categories=" + categories.get(0) +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}