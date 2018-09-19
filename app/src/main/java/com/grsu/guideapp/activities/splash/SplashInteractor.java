package com.grsu.guideapp.activities.splash;

import android.content.Context;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.StorageUtils;
import java.io.File;
import java.io.IOException;

public class SplashInteractor implements SplashContract.SplashInteractor {

    private static final String TAG = SplashInteractor.class.getSimpleName();
    private Context mContext;

    public SplashInteractor(Context context) {
        this.mContext = context;
    }

    @Override
    public void copyContentFromAssets(final OnFinishedListener listener, final File file) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!file.exists()) {
                    Logs.e(TAG, "THIS " + file.mkdirs());
                    StorageUtils.copyAssetsFolder(file.getName(), file, mContext);
                }
                listener.onFinished();
            }
        }).start();
    }

    @Override
    public void deleteAll(final OnFinishedListener listener, final File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StorageUtils.deleteRecursive(file);
                listener.onFinished();
            }
        }).start();
    }

    @Override
    public void upZipContent(final OnFinishedListener listener, final File rootFolder) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (File currentFile : rootFolder.listFiles()) {
                        StorageUtils.unzip(currentFile, rootFolder);
                        currentFile.delete();
                    }
                } catch (IOException e) {
                    Logs.e(TAG, e.getMessage(), e);
                } finally {
                    listener.onFinished();
                }
            }
        }).start();
    }
}
