package com.grsu.guideapp.models;

import android.database.Cursor;
import java.io.Serializable;

public class InfoAboutPoi implements Serializable {

    //POJO model
    private String type;
    private String name_locale;
    private String shortDescriptionPoint;
    private String audioReference;
    private String photoReference;
    private String link;

    private final static long serialVersionUID = -6094752779573843057L;

    /**
     * No args constructor for use in serialization
     */
    public InfoAboutPoi() {
    }

    /**
     * @param type
     * @param name_locale
     * @param shortDescriptionPoint
     * @param audioReference
     * @param photoReference
     * @param link
     */

    public InfoAboutPoi(String type, String name_locale, String shortDescriptionPoint,
            String audioReference, String photoReference, String link) {
        this.type = type;
        this.name_locale = name_locale;
        this.shortDescriptionPoint = shortDescriptionPoint;
        this.audioReference = audioReference;
        this.photoReference = photoReference;
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public String getNameLocale() {
        return name_locale;
    }

    public String getShortDescriptionPoint() {
        return shortDescriptionPoint;
    }

    public String getAudioReference() {
        return audioReference;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "InfoAboutPoi{" +
                "type='" + type + '\'' +
                ", name_locale='" + name_locale + '\'' +
                ", shortDescriptionPoint='" + shortDescriptionPoint + '\'' +
                ", audioReference='" + audioReference + '\'' +
                ", photoReference='" + photoReference + '\'' +
                ", link='" + link + "\'}";
    }

    public static InfoAboutPoi fromCursor(Cursor cursor) {
        return new InfoAboutPoi(
                cursor.getString(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5));
    }
}
