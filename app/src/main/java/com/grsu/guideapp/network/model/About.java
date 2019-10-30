package com.grsu.guideapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class About implements Serializable {

    @SerializedName("full")
    @Expose
    private Language full;
    private final static long serialVersionUID = 3587333965453276249L;

    /**
     * No args constructor for use in serialization
     */
    public About() {
    }

    /**
     *
     * @param full
     */
    public About(Language full) {
        super();
        this.full = full;
    }

    public Language getFull() {
        return full;
    }
}
