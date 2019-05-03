package com.grsu.guideapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.content.ContextCompat;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.project_settings.SharedPref;
import com.grsu.guideapp.utils.StorageUtils;
import java.util.Locale;

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

    public static void setLocale(SharedPreferences pref, Resources res) {
        Editor editor = pref.edit();
        if (!pref.contains(SharedPref.KEY_LANGUAGE)) {
            editor.putString(SharedPref.KEY_LANGUAGE, res.getString(R.string.locale)).apply();
        }
        Configuration conf = res.getConfiguration();

        String loc = pref.getString(SharedPref.KEY_LANGUAGE, res.getString(R.string.locale));
        Locale locale = new Locale(loc);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            conf.setLocale(locale);
        } else {
            conf.locale = locale;
        }

        res.updateConfiguration(conf, res.getDisplayMetrics());
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