package com.grsu.guideapp.models;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.android.gms.maps.model.LatLng;
import com.grsu.guideapp.utils.CryptoUtils;
import java.io.Serializable;

public class Poi implements Serializable {

    private Integer id;
    private String location;
    private Bitmap icon;

    public Poi(Integer id, String location, Bitmap icon) {
        this.id = id;
        this.location = location;
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }

    public LatLng getLocation() {
        return CryptoUtils.decodeP(location);
    }

    public Bitmap getIcon() {
        return icon;
    }

    public static Poi fromCursor(Cursor cursor) {
        byte[] blob = cursor.getBlob(2);
        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);

        return new Poi(cursor.getInt(0), cursor.getString(1), bitmap);
    }
}
