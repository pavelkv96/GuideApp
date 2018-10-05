package com.grsu.guideapp.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {

    private static final String TAG = StreamUtils.class.getSimpleName();

    static void copyFile(InputStream in, OutputStream out) throws IOException {
        copyFile(in, out, new byte[1024]);
    }

    static void copyFile(InputStream in, OutputStream out, byte[] buffer) throws IOException {
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
}
