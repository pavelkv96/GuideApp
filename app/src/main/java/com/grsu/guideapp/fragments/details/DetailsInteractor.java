package com.grsu.guideapp.fragments.details;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.models.InfoAboutPoi;
import com.grsu.guideapp.utils.StorageUtils;
import java.io.File;

public class DetailsInteractor implements DetailsContract.DetailsInteractor {

    private DatabaseHelper helper;

    public DetailsInteractor(@NonNull DatabaseHelper pDbHelper) {
        helper = pDbHelper;
    }

    @Override
    public void getInfoById(final OnFinishedListener<InfoAboutPoi> listener, final String id) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(helper.getInfoById(id));
            }
        });
    }

    @Override
    public void getImageFromStorage(final OnSuccessListener<Bitmap> listener, final String name) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onSuccess(StorageUtils.getImageFromFile(name));
                } catch (NullPointerException e) {
                    listener.onFailure(e);
                }
            }
        });
    }

    @Override
    public void getAudioFromStorage(final OnSuccessListener<File> listener, final String name) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onSuccess(StorageUtils.getAudioFile(name));
                } catch (NullPointerException e) {
                    listener.onFailure(e);
                }
            }
        });
    }
}