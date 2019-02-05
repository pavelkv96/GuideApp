package com.grsu.guideapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.grsu.guideapp.project_settings.Constants;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
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
}