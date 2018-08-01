package com.grsu.guideapp;

import android.app.Application;
import com.grsu.guideapp.utils.ContextHolder;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContextHolder.setContext(getApplicationContext());
    }
}
