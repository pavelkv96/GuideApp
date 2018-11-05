package com.grsu.guideapp.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class StorageUtils {

    private static final String TAG = StorageUtils.class.getSimpleName();

    public static File getStorage() {
        return Environment.getExternalStorageDirectory();
    }

    public static File getDatabasePath(Context context) {
        File file = new File(context.getApplicationInfo().dataDir, Constants.DATABASES);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static void removeDir(String path) {
        deleteRecursive(new File(path));
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

    public static void copyAssets(String fileName, String toFilePath, AssetManager assetManager) {
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

    public static void copyAssetsFolder(String assetsPath, File toFile, AssetManager assetManager) {
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

    public static boolean copyDatabase(@NonNull Context context) {
        String dbName = Settings.DATABASE_INFORMATION_NAME;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = context.getAssets().open(dbName);
            outputStream = new FileOutputStream(context.getDatabasePath(dbName));

            StreamUtils.copyFile(inputStream, outputStream);

            return true;
        } catch (Exception e) {
            e.getMessage();

            return false;
        } finally {
            StreamUtils.closeStream(inputStream);
            StreamUtils.closeStream(outputStream);
        }
    }

    public static boolean deleteDatabase(@NonNull Context context) {
        File database = context.getDatabasePath(Settings.DATABASE_INFORMATION_NAME);
        if (database.exists()) {
            return database.delete();
        }

        return false;
    }

    public static void unzip(File zipFile, File targetDirectory) throws IOException {
        FileInputStream stream = new FileInputStream(zipFile);
        BufferedInputStream in = new BufferedInputStream(stream);
        ZipInputStream zis = new ZipInputStream(in);
        try {
            ZipEntry ze;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs()) {
                    String s = "Failed to ensure directory: " + dir.getAbsolutePath();
                    throw new FileNotFoundException(s);
                }
                if (ze.isDirectory()) {
                    continue;
                }
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    StreamUtils.copyFile(zis, fout, buffer);
                } finally {
                    StreamUtils.closeStream(fout);
                }
            }
        } finally {
            StreamUtils.closeStream(zis);
            StreamUtils.closeStream(in);
            StreamUtils.closeStream(stream);
        }
    }

    @Nullable
    public static Bitmap getImageFromFile(File pathToImage) throws NullPointerException {
        if (pathToImage.exists()) {
            return BitmapFactory.decodeFile(pathToImage.getAbsolutePath());
        }

        throw new NullPointerException("Image not found by path " + pathToImage.getAbsoluteFile());
    }
}
