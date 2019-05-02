package com.grsu.guideapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Root implements Serializable {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("part")
    @Expose
    private Integer part;
    @SerializedName("parts")
    @Expose
    private Integer parts;
    @SerializedName("in_parts")
    @Expose
    private Integer in_parts;
    @SerializedName("data")
    @Expose
    private List<Datum> datums;
    private final static long serialVersionUID = 805154024470119371L;

    public Root(Integer count, Integer part, Integer parts, Integer in_parts, List<Datum> datums) {
        this.count = count;
        this.part = part;
        this.parts = parts;
        this.in_parts = in_parts;
        this.datums = datums;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getPart() {
        return part;
    }

    public Integer getParts() {
        return parts;
    }

    public Integer getInParts() {
        return in_parts;
    }

    public List<Datum> getDatums() {
        return datums;
    }
}
