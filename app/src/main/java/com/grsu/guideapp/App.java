package com.grsu.guideapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.preference.PreferenceManager;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.project_settings.SharedPref;
import java.util.Locale;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
        setLocale(PreferenceManager.getDefaultSharedPreferences(this), getResources());
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
}