package com.grsu.guideapp.activities.splash;

import com.grsu.guideapp.activities.splash.SplashContract.SplashInteractor.OnFinishedListener;
import com.grsu.guideapp.activities.splash.SplashContract.SplashInteractor.OnUpdatedListener;
import com.grsu.guideapp.activities.splash.SplashContract.SplashView;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.project_settings.Settings;
import java.io.File;

public class SplashPresenter extends BasePresenterImpl<SplashView> implements OnFinishedListener,
        SplashContract.SplashPresenter, OnSuccessListener<String>, OnUpdatedListener {

    private static final String TAG = SplashPresenter.class.getSimpleName();
    private int completedThreads;
    private int needCountResults;

    private SplashView splashView;
    private SplashInteractor splashInteractor;

    public SplashPresenter(SplashView splashView, SplashInteractor splashInteractor) {
        this.splashView = splashView;
        this.splashInteractor = splashInteractor;
    }

    @Override
    public void getNewProgress() {
        completedThreads = 0;
        needCountResults = 2;
        File audioContent = new File(Settings.AUDIO_CONTENT);
        File photoContent = new File(Settings.PHOTO_CONTENT);

        splashInteractor.copyAndUnZip(this, this, audioContent);
        splashInteractor.copyAndUnZip(this, this, photoContent);
    }

    @Override
    public void delNewProgress() {
        completedThreads = 0;
        needCountResults = 1;
        File content = new File(Settings.CONTENT);

        splashInteractor.deleteAll(this, this, content);
    }

    @Override
    public void onFinished() {
        getNewProgress();
    }

    @Override
    public void onSuccess(String s) {
        synchronized (TAG) {
            completedThreads++;
            if (completedThreads == needCountResults) {
                splashView.writeInSharedPreference(true);
                splashView.showMessage(s);
            }
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        splashView.writeInSharedPreference(false);
        splashView.showMessage(throwable.getMessage());
    }

    @Override
    public void onUpdated(int progress) {
        splashView.updateViewProgress(progress);
    }
}