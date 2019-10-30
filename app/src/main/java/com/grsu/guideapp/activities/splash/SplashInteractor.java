package com.grsu.guideapp.activities.splash;

import android.content.res.AssetManager;
import com.grsu.guideapp.App;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.StorageUtils;
import java.io.File;

public class SplashInteractor implements SplashContract.SplashInteractor {

    private static final String TAG = SplashInteractor.class.getSimpleName();
    private AssetManager manager;

    SplashInteractor(AssetManager manager) {
        this.manager = manager;
    }

    private void copyFile(File path, String name) {
        if (!path.exists()) {
            StorageUtils.copyAssets(path.getAbsolutePath(), name, manager);
        }
        Logs.e(TAG, "Copy finished by " + path.getAbsolutePath());
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
