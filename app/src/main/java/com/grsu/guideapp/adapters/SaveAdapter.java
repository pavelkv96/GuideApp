package com.grsu.guideapp.adapters;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.grsu.guideapp.R;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.CryptoUtils;
import com.grsu.guideapp.utils.StreamUtils;
import com.grsu.guideapp.utils.extensions.BitmapKt;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import timber.log.Timber;

public class SaveAdapter {

    public static void saveImage(String url) {
        File file = new File(Settings.CONTENT);
        FileOutputStream ostream = null;
        try {
            Bitmap bitmap = Picasso.get().load(url)/*.resize(200, 150)*/.get();
            if (!file.exists()) {
                file.mkdirs();
            }
            File file1 = new File(file, CryptoUtils.hash(url));
            file1.createNewFile();

            ostream = new FileOutputStream(file1);
            bitmap.compress(CompressFormat.PNG, 0, ostream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtils.closeStream(ostream);
        }
    }

    public static byte[] saveIcon(Test helper, String url) {

        byte[] bytes;
        Resources resources = helper.mContext.getResources();
        try {
            Bitmap bitmap = Picasso.get().load(url).resize(59, 75).get();
            bytes = BitmapKt.toByteArray(bitmap);
        } catch (Exception e) {
            Timber.e(e, "saveIcon: %s", e.getMessage());
            Drawable drawable = resources.getDrawable(R.drawable.noicon);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            bytes = BitmapKt.toByteArray(bitmap);
        }
        return bytes;
    }

    public static void saveAudio(String url) {
        if (url == null || url.isEmpty()) {
            return;
        }

        File file = new File(Settings.CONTENT);
        if (!file.exists()) {
            file.mkdirs();
        }

        BufferedSink sink = null;
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = new OkHttpClient().newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                File downloadedFile = new File(Settings.CONTENT, StringKt.toMD5(url));
                Timber.e("saveAudio: %s", StringKt.toMD5(url));
                sink = Okio.buffer(Okio.sink(downloadedFile));
                sink.writeAll(response.body().source());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtils.closeStream(sink);
        }
    }
}
