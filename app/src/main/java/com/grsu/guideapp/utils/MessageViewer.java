package com.grsu.guideapp.utils;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.StringRes;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.grsu.guideapp.App;
import com.grsu.guideapp.R;

@SuppressWarnings("unused")
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

    public static class MySnackbar {

        public static void makeL(View view, String s) {
            abstractSnackbar(view, s, null, null);
        }

        public static void makeL(View view, String s, Context context) {
            abstractSnackbar(view, s, null, getListener(context));
        }

        public static void makeL(View view, @StringRes int s, Context context) {
            abstractSnackbar(view, null, s, getListener(context));
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

            snackbar.setAction(R.string.error_snackbar_open_settings, listener);
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }

        private static OnClickListener getListener(final Context context) {
            return new OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckPermission.settingsIntent(context);
                }
            };
        }
    }
}
