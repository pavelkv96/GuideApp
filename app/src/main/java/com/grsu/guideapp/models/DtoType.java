package com.grsu.guideapp.models;

import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class DtoType {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<DtoType> fromCursor(Cursor cursor) {
        List<DtoType> types = new ArrayList<>(cursor.getCount());

        if (cursor.getCount() > 1 && cursor.moveToFirst()) {
            cursor.moveToNext();
            do {
                DtoType type = new DtoType();
                type.setId(cursor.getInt(0));
                type.setName(cursor.getString(2));
                types.add(type);
            } while (cursor.moveToNext());
        }
        return types;
    }
}
