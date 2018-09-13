package com.grsu.guideapp.utils;

import android.content.Context;
import android.content.res.AssetManager;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class StreamUtils {

    private static final String TAG = StreamUtils.class.getSimpleName();

    public static void copyAssets(String fileName, String toFilePath, Context context) {
        AssetManager assetManager = context.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fileName);
            File outFile = new File(toFilePath);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
        } catch (IOException e) {
            Logs.e(TAG, "Failed to copy asset file: " + fileName, e);
        } finally {
            closeStream(in);
            closeStream(out);
        }
    }

    public static void copyAssetsFolder(String assetsPath, File toFile, Context context) {
        AssetManager assetManager = context.getAssets();
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
                    copyFile(in, out);
                } catch (IOException e) {
                    Logs.e(TAG, "Failed to copy asset file: " + filename, e);
                } finally {
                    closeStream(in);
                    closeStream(out);
                }
            }
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ignore) {
            }
        }
    }

    public static void unzip(File zipFile, File targetDirectory) throws IOException {
        FileInputStream stream = new FileInputStream(zipFile);
        BufferedInputStream in = new BufferedInputStream(stream);
        ZipInputStream zis = new ZipInputStream(in);
        try {
            ZipEntry ze;
            int count;
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
                    while ((count = zis.read(buffer)) != -1) {
                        fout.write(buffer, 0, count);
                    }
                } finally {
                    closeStream(fout);
                }
            }
        } finally {
            closeStream(stream);
            closeStream(in);
            closeStream(zis);
        }
    }
}
