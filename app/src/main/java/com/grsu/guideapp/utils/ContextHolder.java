package com.grsu.guideapp.utils;

import android.content.Context;

public class ContextHolder {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context context) {
        ContextHolder.mContext = context;
    }
}
