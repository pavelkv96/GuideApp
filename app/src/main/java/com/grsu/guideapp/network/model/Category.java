package com.grsu.guideapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Category implements Serializable {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    private final static long serialVersionUID = 2853086861039255112L;

    /**
     * No args constructor for use in serialization
     */
    public Category() {
    }

    /**
     *
     * @param data
     */
    public Category(List<Datum> data) {
        super();
        this.data = data;
    }

    public List<Datum> getData() {
        return data;
    }
}
