package com.grsu.guideapp.models;

import androidx.annotation.NonNull;

public class Tag {

    private boolean poi;
    private String id;

    public Tag(boolean type, String id) {
        this.poi = type;
        this.id = id;
    }

    public boolean isPoi() {
        return poi;
    }

    @NonNull
    public String getId() {
        return id;
    }
}
