package com.grsu.guideapp;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.grsu.guideapp.network.APIService;
import com.grsu.guideapp.project_settings.Settings;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public enum AppExecutors {

    INSTANCE;

    private final Executor mDiskIO;
    private final APIService mNetworkIO;
    private final Executor mMainThread;

    AppExecutors() {
        Builder builder = new Retrofit.Builder();
        builder.baseUrl(Settings.BASE_URL);
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        this.mDiskIO = Executors.newFixedThreadPool(1);
        this.mNetworkIO = retrofit.create(APIService.class);
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