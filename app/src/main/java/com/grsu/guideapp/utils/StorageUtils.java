package com.grsu.guideapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.Nullable;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.io.File;

public class StorageUtils {

    private static final String TAG = StorageUtils.class.getSimpleName();

    public static File getStorage() {
        return Environment.getExternalStorageDirectory();
    }

    public static boolean removeDir(String path) {
        return removeDirAbstract(new File(path));
    }

    public static boolean removeDir(File file) {
        return removeDirAbstract(file);
    }

    private static boolean removeDirAbstract(File path) {
        if (path.isDirectory()) {
            String[] children = path.list();
            for (String aChildren : children) {
                new File(path, aChildren).delete();
            }
            return path.delete();
        }
        return false;
    }

    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                Logs.e(TAG, child.getAbsolutePath());
                deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }


    @Nullable
    public static Bitmap getImageFromFile(File pathToImage) throws NullPointerException {
        if (pathToImage.exists()) {
            return BitmapFactory.decodeFile(pathToImage.getAbsolutePath());
        }

        throw new NullPointerException("Image not found by path " + pathToImage.getAbsoluteFile());
    }
}
