package com.grsu.guideapp;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.grsu.guideapp.network.Network;
import com.grsu.guideapp.network.APIService;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public enum AppExecutors {

    INSTANCE;

    private final Executor mDiskIO;
    private final APIService mNetworkIO;
    private final Executor mMainThread;

    AppExecutors() {
        this.mDiskIO = Executors.newFixedThreadPool(1);
        this.mNetworkIO = Network.INSTANCE.getApi();
        this.mMainThread = new MainExecutor();
    }

    public void diskIO(Runnable runnable) {
        mDiskIO.execute(runnable);
    }

    public APIService networkIO() {
        return mNetworkIO;
    }

    public void mainThread(Runnable runnable) {
        mMainThread.execute(runnable);
    }

    private static class MainExecutor implements Executor {

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}