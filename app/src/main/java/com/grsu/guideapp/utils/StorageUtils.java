package com.grsu.guideapp.utils;

import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StorageUtils {

    private static final String TAG = StorageUtils.class.getSimpleName();

    public static File getStorage() {
        return Environment.getExternalStorageDirectory();
    }

    public static void copyAssets(String fileName, String toFilePath, AssetManager assetManager) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fileName);
            File outFile = new File(toFilePath);
            out = new FileOutputStream(outFile);
            StreamUtils.copyFile(in, out);
        } catch (IOException e) {
            Log.e(TAG, "Failed to copy asset file: " + fileName, e);
        } finally {
            StreamUtils.closeStream(in);
            StreamUtils.closeStream(out);
        }
    }

    public static void removeDir(String path) {
        removeDir(new File(path));
    }

    public static void removeDir(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                Logs.e(TAG, child.getAbsolutePath());
                removeDir(child);
            }
        }

        fileOrDirectory.delete();
    }
}
