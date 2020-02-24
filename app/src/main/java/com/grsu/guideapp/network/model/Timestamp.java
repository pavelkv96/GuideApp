
package com.grsu.guideapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Date;

public class Timestamp implements Serializable
{

    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    private final static long serialVersionUID = 5610967999058676964L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Timestamp() {
    }

    /**
     * 
     * @param updatedAt
     * @param createdAt
     */
    public Timestamp(String createdAt, String updatedAt) {
        super();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return /*Converter.toDate(createdAt);*/new Date(0);
    }

    public Date getUpdatedAt() {
        return /*Converter.toDate(updatedAt);*/new Date(0);
    }
}
