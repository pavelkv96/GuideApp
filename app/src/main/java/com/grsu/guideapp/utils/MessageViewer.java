package com.grsu.guideapp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.grsu.guideapp.BuildConfig;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseActivity;

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

    public static class MySnackbar {

        public static void makeL(View view, String s) {
            abstractSnackbar(view, s, null, null);
        }

        public static <T extends BaseActivity> void makeL(View view, String s, final T activity) {
            abstractSnackbar(view, s, null, getListener(activity));
        }

        public static <T extends FragmentActivity> void makeL(View view, @StringRes int s,
                final T activity) {
            abstractSnackbar(view, null, s, getListener(activity));
        }

        public static void makeL(View view, String s, OnClickListener listener) {
            abstractSnackbar(view, s, null, listener);
        }

        public static void makeL(View view, @StringRes int resource, OnClickListener listener) {
            abstractSnackbar(view, null, resource, listener);
        }

        private static void abstractSnackbar(View view, String s, Integer resource,
                OnClickListener listener) {

            Snackbar snackbar;
            if (s != null) {
                snackbar = Snackbar.make(view, s, Snackbar.LENGTH_LONG);
            } else {
                snackbar = Snackbar.make(view, resource, Snackbar.LENGTH_LONG);
            }

            snackbar.setAction(R.string.error_snackbar_open_settings, listener)
                    .setActionTextColor(Color.RED)
                    .show();
        }

        private static <T extends FragmentActivity> OnClickListener getListener(final T activity) {
            return new OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckSelfPermission.settingsIntent(activity);
                }
            };
        }
    }
}
