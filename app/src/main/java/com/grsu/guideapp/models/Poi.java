package com.grsu.guideapp.models;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.Serializable;

public class Poi implements Serializable {

    private String id;
    private String name;
    private Bitmap icon;

    public Poi() {
    }

    public Poi(String id, String name, Bitmap icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public static Poi fromCursor(Cursor cursor) {
        byte[] blob = cursor.getBlob(2);
        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);

        return new Poi(cursor.getString(0), cursor.getString(1), bitmap);
    }
}
