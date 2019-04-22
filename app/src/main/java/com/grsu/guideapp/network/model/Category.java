package com.grsu.guideapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Category implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private Language name;

    private final static long serialVersionUID = 2853086861039255112L;

    /**
     * No args constructor for use in serialization
     */
    public Category() {
    }

    /**
     * @param id
     * @param name
     */
    public Category(Integer id, Language name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public Language getName() {
        return name;
    }
}
