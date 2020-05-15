package com.grsu.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

class MyReceiver extends BroadcastReceiver {

    private OnReceiverResponse listener;

    public MyReceiver(OnReceiverResponse listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(@NonNull Context context, @Nullable Intent intent) {
        if (intent != null) {
            listener.onReceiver(intent);
        }
    }
}

