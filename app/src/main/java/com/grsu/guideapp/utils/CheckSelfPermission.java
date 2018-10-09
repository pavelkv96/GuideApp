package com.grsu.guideapp.utils;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class CheckSelfPermission {

    public static boolean writeExternalStorageIsGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED;
    }

    public static boolean getAccessLocationIsGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED;
    }
}
