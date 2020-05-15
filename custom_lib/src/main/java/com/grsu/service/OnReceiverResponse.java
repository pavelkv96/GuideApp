package com.grsu.service;

import android.content.Intent;
import android.support.annotation.NonNull;

interface OnReceiverResponse {
    void onReceiver(@NonNull Intent intent);
}
