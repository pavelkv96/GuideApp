package com.grsu.guideapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import androidx.core.content.ContextCompat;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.project_settings.SharedPref;
import com.grsu.guideapp.utils.StorageUtils;
import com.grsu.guideapp.utils.extensions.Locale;

public class App extends Application {

    private static App app;
    static AppExecutors executors;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        createNotificationChannel();
        createDBIfNeed();
    }

    public static App getInstance(){
        return app;
    }

    private void createNotificationChannel() {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    Constants.CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );

            NotificationManager service = getSystemService(NotificationManager.class);
            if (service != null) {
                service.createNotificationChannel(serviceChannel);
            }
        }
    }

    public Context setLocale(SharedPreferences pref, Context context) {
        Editor editor = pref.edit();
        if (!pref.contains(SharedPref.KEY_LANGUAGE)) {
            String locale = Locale.getCurrentLocale(java.util.Locale.getDefault());
            editor.putString(SharedPref.KEY_LANGUAGE, locale).apply();
        }
        Configuration conf = getResources().getConfiguration();

        String loc = pref.getString(SharedPref.KEY_LANGUAGE, getResources().getString(R.string.locale));
        java.util.Locale locale = new java.util.Locale(loc);
        if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(locale);
            return createConfigurationContext(conf);
        } else {
            conf.locale = locale;
            getResources().updateConfiguration(conf, getResources().getDisplayMetrics());
            return context;
        }
    }

    public static AppExecutors getThread() {
        if (executors == null) {
            executors = AppExecutors.INSTANCE;
        }

        return executors;
    }

    public static boolean isOnline() {
        ConnectivityManager mn = ContextCompat.getSystemService(app, ConnectivityManager.class);

        boolean isConnected = false;
        if (mn != null) {
            NetworkInfo activeNetwork = mn.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }

        return isConnected;
    }

    private void createDBIfNeed() {
        if (!getDatabasePath(Settings.DATABASE_INFORMATION_NAME).exists()) {
            StorageUtils.copyDatabase(this);
        }
    }
}