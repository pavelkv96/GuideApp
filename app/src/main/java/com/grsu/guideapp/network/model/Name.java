package com.grsu.guideapp.network.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Name implements Serializable {

    @SerializedName("full")
    @Expose
    private Language full;
    @SerializedName("short")
    @Expose
    private Language shorted;
    private final static long serialVersionUID = 7759536043499944190L;

    /**
     * No args constructor for use in serialization
     */
    public Name() {
    }

    /**
     *
     * @param full
     * @param shorted
     */
    public Name(Language full, Language shorted) {
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
