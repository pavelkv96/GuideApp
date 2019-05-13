package com.grsu.guideapp.models;

import android.support.annotation.NonNull;
import java.io.Serializable;

public class Names implements Serializable {

    //POJO model
    private String short_name;
    private String full_name;
    private String short_desc;
    private String full_desc;
    private final static long serialVersionUID = -4448199341994382402L;

    public Names() {
    }

    public String getShortName() {
        return short_name;
    }

    public void setShortName(String short_name) {
        this.short_name = short_name;
    }

    public String getFullName() {
        return full_name;
    }

    public void setFullName(String full_name) {
        this.full_name = full_name;
    }

    public String getShortDescription() {
        return short_desc;
    }

    public void setShortDescription(String short_description) {
        this.short_desc = short_description;
    }

    public String getFullDescription() {
        return full_desc;
    }

    public void setFullDescription(String full_description) {
        this.full_desc = full_description;
    }

    @NonNull
    @Override
    public String toString() {
        return "Names{" +
                "short_name='" + short_name + '\'' +
                ", full_name='" + full_name + '\'' +
                ", short_desc='" + short_desc + '\'' +
                ", full_desc='" + full_desc + '\'' +
                '}';
    }
}