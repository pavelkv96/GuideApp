package com.grsu.guideapp.models;

import android.database.Cursor;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.CryptoUtils;
import java.io.File;
import java.io.Serializable;

public class DtoDetail implements Serializable {

    private Names name_locale;
    private String photoReference;
    private String link;
    private String address;
    private String email;
    private String phone;

    private final static long serialVersionUID = -6094752779573843057L;

    public DtoDetail() {
    }

    public Names getNameLocale() {
        return name_locale;
    }

    public void setNameLocale(Names name_locale) {
        this.name_locale = name_locale;
    }

    public File getPhotoReference() {
        String hash = CryptoUtils.hash(photoReference);
        if (hash != null) {
            return new File(Settings.CONTENT, hash);
        }
        return null;
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

    public String[] getAddress() {
        return address.split("\\|", -1);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static DtoDetail fromCursor(Cursor cur) {
        if (cur.isFirst()) {
            Names name = new Names();
            name.setName(cur.getString(0));
            name.setDescription(cur.getString(1));

            DtoDetail aboutPoi = new DtoDetail();
            aboutPoi.setNameLocale(name);
            aboutPoi.setPhotoReference(cur.getString(2));
            aboutPoi.setLink(cur.getString(3));
            aboutPoi.setAddress(cur.getString(4));
            aboutPoi.setEmail(cur.getString(5));
            aboutPoi.setPhone(cur.getString(6));
            return aboutPoi;
        } else {
            return null;
        }
    }
}
