package com.grsu.guideapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Datum implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("timestamp")
    @Expose
    private Timestamp timestamp;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("data")
    @Expose
    private Data data;
    private final static long serialVersionUID = 805164024470119371L;

    /**
     * No args constructor for use in serialization
     */
    public Datum() {
    }

    /**
     *
     * @param timestamp
     * @param id
     * @param data
     * @param active
     */
    public Datum(Integer id, Timestamp timestamp, Boolean active, Data data) {
        super();
        this.id = id;
        this.timestamp = timestamp;
        this.active = active;
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Boolean getActive() {
        return active;
    }

    public Data getData() {
        return data;
    }
}
