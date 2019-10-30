package com.grsu.guideapp.models;

import android.support.annotation.NonNull;
import java.io.Serializable;

public class Names implements Serializable {

    //POJO model
    private String name;
    private String description;
    private final static long serialVersionUID = -4448199341994382402L;

    public Names() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return "Names{" + ", name='" + name + '\'' + ", description='" + description + '\'' + '}';
    }
}