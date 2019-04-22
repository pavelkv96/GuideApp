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

    /**
     * @param type
     * @param name_locale
     * @param audioReference
     * @param photoReference
     * @param link
     */

    public InfoAboutPoi(String type, Names name_locale, String audioReference,
            String photoReference, String link, long last_update) {
        this.type = type;
        this.name_locale = name_locale;
        this.audioReference = audioReference;
        this.photoReference = photoReference;
        this.link = link;
        this.last_update = last_update;
    }

    public String getType() {
        return type;
    }

    public Names getNameLocale() {
        return name_locale;
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

    public String getLast_update() {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(new Date(last_update*1000));
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

    public static InfoAboutPoi fromCursor(Cursor cursor, Names names) {
        return new InfoAboutPoi(
                cursor.getString(1),
                names,
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getLong(5)
        );
    }
}
