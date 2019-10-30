package com.grsu.guideapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Name implements Serializable {

    @SerializedName("full")
    @Expose
    private Language full;
    private final static long serialVersionUID = 7759536043499944190L;

    /**
     * No args constructor for use in serialization
     */
    public Name() {
    }

    /**
     *
     * @param full
     */
    public Name(Language full) {
        super();
        this.full = full;
    }

    public Language getFull() {
        return full;
    }
}
