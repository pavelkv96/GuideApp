package com.grsu.guideapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.grsu.guideapp.project_settings.Settings;
import java.io.Serializable;

public class Image implements Serializable {

    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("isMain")
    @Expose
    private Boolean isMain;
    private final static long serialVersionUID = 7925938243165924688L;

    /**
     * No args constructor for use in serialization
     */
    public Image() {
    }

    /**
     *
     * @param isMain
     * @param href
     */
    public Image(String href, Boolean isMain) {
        super();
        this.href = href;
        this.isMain = isMain;
    }

    public String getHref() {
        return href;
    }

    public Boolean getIsMain() {
        return isMain;
    }

}
