package com.grsu.guideapp.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Toast;
import com.grsu.guideapp.BuildConfig;

public class MessageViewer {

    public static class Toasts {

        public static void makeL(Context context, String message) {
            makeToast(context, message, Toast.LENGTH_LONG);
        }

        public static void makeL(Context context, @StringRes int message) {
            makeToast(context, message, Toast.LENGTH_LONG);
        }

        public static void makeS(Context context, String message) {
            makeToast(context, message, Toast.LENGTH_SHORT);
        }

        public static void makeS(Context context, @StringRes int message) {
            makeToast(context, message, Toast.LENGTH_SHORT);
        }

        private static void makeToast(Context context, String message, int length) {
            Toast.makeText(context, message, length).show();
        }

        private static void makeToast(Context context, @StringRes int message, int length) {
            Toast.makeText(context, message, length).show();
        }
    }

    public static class Logs {

        public static void e(String tag, String message, Throwable t) {
            eAbstract(tag, message, t);
        }

        public static void e(String tag, String message) {
            eAbstract(tag, message, null);
        }

        private static void eAbstract(String tag, String message, Throwable t) {
            if (BuildConfig.DEBUG) {
                Log.e(tag, message, t);
            }
        }
    }
}