package com.grsu.guideapp.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.MessageViewer.Logs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StorageUtils {

    private static final String TAG = StorageUtils.class.getSimpleName();

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

    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                Logs.e(TAG, child.getAbsolutePath());
                deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
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
            Logs.e(TAG, "Failed to copy asset file: " + fileName, e);
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
            Logs.e(TAG, "Failed to get asset file list.", e);
        }
        if (files != null) {
            for (String filename : files) {
                Logs.e(TAG, filename);

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
                    Logs.e(TAG, "Failed to copy asset file: " + filename, e);
                } finally {
                    StreamUtils.closeStream(in);
                    StreamUtils.closeStream(out);
                }
            }
        }
    }

    public static void copyDatabase(@NonNull Context context) {
        getDatabasePath(context);
        String dbName = Settings.DATABASE_INFORMATION_NAME;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = context.getAssets().open(dbName);
            outputStream = new FileOutputStream(context.getDatabasePath(dbName));

            StreamUtils.copyFile(inputStream, outputStream);

        } catch (Exception ignore) {
        } finally {
            StreamUtils.closeStream(inputStream);
            StreamUtils.closeStream(outputStream);
        }
    }

    public static boolean deleteDatabase(@NonNull Context context) {
        File database = context.getDatabasePath(Settings.DATABASE_INFORMATION_NAME);
        File journal = context.getDatabasePath(Settings.DATABASE_JOURNAL);
        journal.delete();
        if (database.exists()) {
            return database.delete();
        }

        return false;
    }

    @NonNull
    public static File getAudioFile(String name) throws NullPointerException {
        String child = String.format("%s.%s", name, Constants.MP3);
        File file = new File(Settings.AUDIO_CONTENT, child);
        if (file.exists()) {
            return file;
        }

        throw new NullPointerException("Audio not found by path " + file.getAbsoluteFile());
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
