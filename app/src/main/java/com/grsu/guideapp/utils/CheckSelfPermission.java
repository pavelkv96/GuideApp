package com.grsu.guideapp.utils;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class CheckSelfPermission {

    public static final String[] groupExternalStorage = new String[]{
            permission.WRITE_EXTERNAL_STORAGE,
            permission.READ_EXTERNAL_STORAGE
    };

    public static boolean writeExternalStorageIsGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isAllGranted(int[] grantResults) {

        if (grantResults.length == 0) {
            return false;
        } else {
            for (int s : grantResults) {
                if (s != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void requestPermissions(Activity activity, String[] strings, int requestCode) {
        ActivityCompat.requestPermissions(activity, strings, requestCode);
    }

    public static boolean checkVersionSdk(int version) {
        return VERSION.SDK_INT >= version;
    }
}

