package com.grsu.guideapp.models;

import android.database.Cursor;
import com.google.android.gms.maps.model.LatLng;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.extensions.StringKt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DtoObject {

    private int id;
    private String name;
    private String photo;
    private LatLng location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getPhoto() {
        return new File(Settings.CONTENT, StringKt.toMD5(photo));
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public static List<DtoObject> fromCursor(Cursor cursor) {
        List<DtoObject> objectList = new ArrayList<>(cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                DtoObject object = new DtoObject();
                object.setId(cursor.getInt(0));
                object.setName(cursor.getString(1));
                object.setPhoto(cursor.getString(2));
                objectList.add(object);
            } while (cursor.moveToNext());
        }

        return objectList;
    }
}
