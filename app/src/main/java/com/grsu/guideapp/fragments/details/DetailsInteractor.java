package com.grsu.guideapp.fragments.details;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.models.InfoAboutPoi;
import com.grsu.guideapp.project_settings.Settings;
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
    public void getImageFromStorage(final OnSuccessListener<Bitmap> listener, final File file) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onSuccess(StorageUtils.getImageFromFile(file));
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
                    File file = new File(Settings.AUDIO_CONTENT, name + ".mp3");
                    if (file.exists()) {
                        listener.onSuccess(file);
                    } else {
                        throw new NullPointerException(
                                "Audio not found by path " + file.getAbsoluteFile());
                    }
                } catch (NullPointerException e) {
                    listener.onFailure(e);
                }
            }
        });
    }
}