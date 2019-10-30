package com.grsu.guideapp.activities.splash;

import com.grsu.guideapp.App;
import com.grsu.guideapp.activities.splash.SplashContract.SplashInteractor.OnFinishedListener;
import com.grsu.guideapp.activities.splash.SplashContract.SplashInteractor.OnUpdatedListener;
import com.grsu.guideapp.activities.splash.SplashContract.SplashView;
import com.grsu.guideapp.base.BasePresenterImpl;
import java.io.File;

public class SplashPresenter extends BasePresenterImpl<SplashView> implements OnFinishedListener,
        SplashContract.SplashPresenter, OnUpdatedListener {

    private SplashView splashView;
    private SplashInteractor splashInteractor;
    private int progress = 0;

    SplashPresenter(SplashView splashView, SplashInteractor splashInteractor) {
        this.splashView = splashView;
        this.splashInteractor = splashInteractor;
    }

    @Override
    public void copyFromAssets(File path, String name) {
        splashInteractor.copy(this, path, name);
    }

    @Override
    public void onUpdated(int i) {
        synchronized (this) {
            progress += i;
            splashView.updateViewProgress(progress);
            if (progress == 100) {
                App.getThread().mainThread(new Runnable() {
                    @Override
                    public void run() {
                        onFinished();
                    }
                });
            }
        }
    }

    @Override
    public void onFinished() {
        splashView.openActivity();
    }
}