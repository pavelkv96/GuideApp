package com.grsu.guideapp.activities.splash;

import android.content.res.AssetManager;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.StorageUtils;
import java.io.File;
import java.io.IOException;

public class SplashInteractor implements SplashContract.SplashInteractor {

    private static final String TAG = SplashInteractor.class.getSimpleName();
    private AssetManager manager;

    public SplashInteractor(AssetManager manager) {
        this.manager = manager;
    }

    @Override
    public void deleteAll(final OnFinishedListener finished, final OnUpdatedListener updated,
            final File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                StorageUtils.deleteRecursive(file);
                updated.onUpdated(10);
                finished.onFinished();
            }
        }).start();
    }

    @Override
    public void copyAndUnZip(final OnSuccessListener<String> success,
            final OnUpdatedListener updated, final File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    updated.onUpdated(7);
                    copyFile(file);
                    updated.onUpdated(10);
                    unzipFile(file);
                    updated.onUpdated(27);

                    success.onSuccess("OK");
                } catch (IOException | NullPointerException e) {
                    Logs.e(TAG, e.getMessage(), e);
                    success.onFailure(e);
                }
            }
        }).start();
    }

    private void copyFile(File file) {
        if (!file.exists()) {
            file.mkdirs();
            StorageUtils.copyAssetsFolder(file.getName(), file, manager);
        }
        Logs.e(TAG, "Copy finished by " + file.getAbsolutePath());
    }

    private void unzipFile(File file) throws IOException {
        for (File currentFile : file.listFiles()) {
            StorageUtils.unzip(currentFile, file);
            currentFile.delete();
        }
        Logs.e(TAG, "Unzip finished by " + file.getAbsolutePath());
    }
}
