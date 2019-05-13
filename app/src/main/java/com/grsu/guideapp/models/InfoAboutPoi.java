package com.grsu.guideapp.models;

import android.database.Cursor;
import android.support.annotation.NonNull;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InfoAboutPoi implements Serializable {

    //POJO model
    private String type;
    private Names name_locale;
    private String audioReference;
    private String photoReference;
    private String link;
    private long last_update;

    private final static long serialVersionUID = -6094752779573843057L;

    /**
     * No args constructor for use in serialization
     */
    public InfoAboutPoi() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Names getNameLocale() {
        return name_locale;
    }

    public void setNameLocale(Names name_locale) {
        this.name_locale = name_locale;
    }

    public String getAudioReference() {
        return audioReference;
    }

    public void setAudioReference(String audioReference) {
        this.audioReference = audioReference;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLast_update() {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(new Date(last_update * 1000));
    }

    public void setLastUpdate(long last_update) {
        this.last_update = last_update;
    }

    @NonNull
    @Override
    public String toString() {
        return "InfoAboutPoi{" +
                "type='" + type + '\'' +
                ", name_locale='" + name_locale + '\'' +
                ", audioReference='" + audioReference + '\'' +
                ", photoReference='" + photoReference + '\'' +
                ", link='" + link + "\'}";
    }

    public static InfoAboutPoi fromCursor(Cursor cur) {
        Names name = new Names();
        name.setShortName(cur.getString(1));
        name.setFullName(cur.getString(2));
        name.setShortDescription(cur.getString(3));
        name.setFullDescription(cur.getString(4));

        InfoAboutPoi aboutPoi = new InfoAboutPoi();
        aboutPoi.setType(cur.getString(0));
        aboutPoi.setNameLocale(name);
        aboutPoi.setAudioReference(cur.getString(5));
        aboutPoi.setPhotoReference(cur.getString(6));
        aboutPoi.setLink(cur.getString(7));
        aboutPoi.setLastUpdate(cur.getLong(8));
        return aboutPoi;
    }
}
