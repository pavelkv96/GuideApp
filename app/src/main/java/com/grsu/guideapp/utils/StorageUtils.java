package com.grsu.guideapp.utils;

import android.os.Environment;
import java.io.File;

public class StorageUtils {

    public static File getStorage() {
        return Environment.getExternalStorageDirectory();
    }

    public static boolean removeDir(String path) {
        File dir = new File(path);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                new File(dir, aChildren).delete();
            }
            return dir.delete();
        }
        return false;
    }
}
