package com.grsu.guideapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class About implements Serializable {

    @SerializedName("full")
    @Expose
    private Language full;
    @SerializedName("short")
    @Expose
    private Language shorted;
    private final static long serialVersionUID = 3587333965453276249L;

    /**
     * No args constructor for use in serialization
     */
    public About() {
    }

    /**
     *
     * @param full
     * @param shorted
     */
    public About(Language full, Language shorted) {
        super();
        this.full = full;
        this.shorted = shorted;
    }

    public Language getFull() {
        return full;
    }

    public Language getShort() {
        return shorted;
    }
}
