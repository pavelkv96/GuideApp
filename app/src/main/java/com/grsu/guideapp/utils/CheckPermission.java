package com.grsu.guideapp.utils;

import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import com.grsu.guideapp.project_settings.Constants;

public class CheckPermission {

    public static final String[] groupStorage = new String[]{
            permission.WRITE_EXTERNAL_STORAGE,
            permission.READ_EXTERNAL_STORAGE
    };

    public static final String[] groupLocation = new String[]{
            permission.ACCESS_FINE_LOCATION,
    };

    public static final String[] groupStorageAndLocation = new String[]{
            permission.WRITE_EXTERNAL_STORAGE,
            permission.READ_EXTERNAL_STORAGE,
            permission.ACCESS_FINE_LOCATION,
    };

    public static boolean canWriteStorage(Context context) {
        return ContextCompat.checkSelfPermission(context, groupStorage[0]) == 0 &&
                ContextCompat.checkSelfPermission(context, groupStorage[1]) == 0;
    }

    public static boolean canGetLocation(Context context) {
        return ContextCompat.checkSelfPermission(context, groupLocation[0]) == 0;
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

    public static void settingsIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts(Constants.PACKAGE, Constants.PACKAGE_NAME, null));
        context.startActivity(intent);
    }
}

