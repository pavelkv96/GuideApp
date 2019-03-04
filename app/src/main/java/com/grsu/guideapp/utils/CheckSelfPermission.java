package com.grsu.guideapp.utils;

import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v4.content.ContextCompat;
import com.grsu.guideapp.project_settings.Constants;

public class CheckSelfPermission {

    public static final String[] groupExternalStorage = new String[]{
            permission.WRITE_EXTERNAL_STORAGE,
            permission.READ_EXTERNAL_STORAGE
    };

    public static final String[] groupAccessLocation = new String[]{
            permission.ACCESS_FINE_LOCATION,
            permission.ACCESS_COARSE_LOCATION
    };

    public static boolean writeExternalStorageIsGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }

    public static boolean getAccessLocationIsGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, permission.ACCESS_COARSE_LOCATION)
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

    public static boolean checkVersionSdk(int version) {
        return VERSION.SDK_INT >= version;
    }

    public static void settingsIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts(Constants.PACKAGE, Constants.PACKAGE_NAME, null));
        context.startActivity(intent);
    }
}

