package com.grsu.guideapp.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.grsu.guideapp.project_settings.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import timber.log.Timber;

public class StorageUtils {

    public static File getStorage() {
        File parent = Environment.getExternalStoragePublicDirectory("Android/data");
        return new File(parent, Constants.PACKAGE_NAME);
    }

    public static File getDatabasePath(Context context) {
        File file = new File(context.getApplicationInfo().dataDir, Constants.DATABASES);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static void copyAssets(String toFilePath, String fileName, AssetManager assetManager) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fileName);
            File outFile = new File(toFilePath);
            out = new FileOutputStream(outFile);
            StreamUtils.copyFile(in, out);
        } catch (IOException e) {
            Timber.e(e, "Failed to copy asset file: %s", fileName);
        } finally {
            StreamUtils.closeStream(in);
            StreamUtils.closeStream(out);
        }
    }

    public static void copyAssetsFolder(File toFile, String assetsPath, AssetManager assetManager) {
        String[] files = null;
        try {
            files = assetManager.list(assetsPath);
        } catch (IOException e) {
            Timber.e(e, "Failed to get asset file list.");
        }
        if (files != null) {
            for (String filename : files) {
                Timber.e(filename);

                InputStream in = null;
                OutputStream out = null;
                try {
                    if (assetsPath.equals("")) {
                        in = assetManager.open(filename);
                    } else {
                        in = assetManager.open(assetsPath + "/" + filename);
                    }

                    File outFile = new File(toFile.getAbsolutePath(), filename);
                    out = new FileOutputStream(outFile);
                    StreamUtils.copyFile(in, out);
                } catch (IOException e) {
                    Timber.e(e, "Failed to copy asset file: %s", filename);
                } finally {
                    StreamUtils.closeStream(in);
                    StreamUtils.closeStream(out);
                }
            }
        }
    }

    public static Bitmap getBitmap(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap
                .createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                        Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
