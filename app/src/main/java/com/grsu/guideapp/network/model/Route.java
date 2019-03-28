package com.grsu.guideapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Route implements Serializable {

    @SerializedName("timestamp")
    @Expose
    private Timestamp timestamp;
    @SerializedName("value")
    @Expose
    private Value value;
    @SerializedName("localization")
    @Expose
    private Object localization;

    private final static long serialVersionUID = -414917604689127668L;

    /**
     * No args constructor for use in serialization
     */
    public Route() {
    }

    /**
     *
     * @param timestamp
     * @param value
     */
    public Route(Timestamp timestamp, Value value) {
        super();
        this.timestamp = timestamp;
        this.value = value;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Value getValue() {
        return value;
    }
}
