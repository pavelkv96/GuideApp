package com.grsu.guideapp.activities.splash;

import android.content.res.AssetManager;
import com.grsu.guideapp.App;
import com.grsu.guideapp.utils.StorageUtils;
import java.io.File;

import timber.log.Timber;

public class SplashInteractor implements SplashContract.SplashInteractor {

    private AssetManager manager;

    SplashInteractor(AssetManager manager) {
        this.manager = manager;
    }

    private void copyFile(File path, String name) {
        if (!path.exists()) {
            StorageUtils.copyAssets(path.getAbsolutePath(), name, manager);
        }
        Timber.e("Copy finished by %s", path.getAbsolutePath());
    }

    @Override
    public void copy(final OnUpdatedListener updated, final File path, final String name) {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                if (path.exists()){
                    path.delete();
                }
                copyFile(path, name);
                updated.onUpdated(50);
            }
        });
    }
}
