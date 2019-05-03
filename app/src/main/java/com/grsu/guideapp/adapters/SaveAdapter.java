package com.grsu.guideapp.adapters;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import com.grsu.guideapp.database.Table.Types;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.CryptoUtils;
import com.grsu.guideapp.utils.StorageUtils;
import com.grsu.guideapp.utils.StreamUtils;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileOutputStream;

public class SaveAdapter {

    public static void saveImage(String url) {
        File file = new File(Settings.CONTENT);
        FileOutputStream ostream = null;
        try {
            Bitmap bitmap = Picasso.get().load(url).resize(200, 150).get();
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

    /*public static void saveIcon(Test helper, String url, long id) {
        try {
            Bitmap bitmap = Picasso.get().load(url).resize(64, 64).get();
            byte[] bytes = StorageUtils.toByteArray(bitmap);
            ContentValues values = new ContentValues();
            values.put(Types.id_type, id);
            values.put(Types.icon_type, bytes);
            values.put(Types.is_checked, 1);
            helper.getWritableDatabase().insertWithOnConflict(Types.icon_type, null, values, 4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static void saveIcon(Test helper, String url, long id) {

        byte[] bytes;
        try {
            Bitmap bitmap = Picasso.get().load(url).resize(64, 64).get();
            bytes = StorageUtils.toByteArray(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            bytes = null;
        }
        ContentValues values = new ContentValues();
        values.put(Types.id_type, id);

        if (bytes == null) {
            values.putNull(Types.icon_type);
        } else {
            values.put(Types.icon_type, bytes);
        }

        values.put(Types.is_checked, 1);
        helper.getWritableDatabase().insertWithOnConflict(Types.name_table, null, values, 4);
    }

    public static void saveIcon1(Test helper, String url, long id) {
        try {
            Bitmap bitmap = Picasso.get().load(url).resize(64, 64).get();
            byte[] bytes = StorageUtils.toByteArray(bitmap);
            ContentValues values = new ContentValues();
            values.put(Types.icon_type, bytes);
            helper.getWritableDatabase().update(Types.icon_type, values, "id_type = ?",
                    new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
