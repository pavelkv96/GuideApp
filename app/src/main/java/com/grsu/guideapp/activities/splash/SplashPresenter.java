package com.grsu.guideapp.activities.splash;

import com.grsu.guideapp.activities.splash.SplashContract.SplashInteractor.OnFinishedListener;
import com.grsu.guideapp.activities.splash.SplashContract.SplashView;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.project_settings.Settings;
import java.io.File;

public class SplashPresenter extends BasePresenterImpl<SplashView> implements OnFinishedListener,
        SplashContract.SplashPresenter {

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
    public void getProgress() {
        completedThreads = 0;
        needCountResults = 2;
        File photoContent = new File(Settings.PHOTO_CONTENT);
        File audioContent = new File(Settings.AUDIO_CONTENT);

        splashView.showProgress(null, "Loading...");
        splashInteractor.copyContentFromAssets(this, photoContent);
        splashInteractor.copyContentFromAssets(this, audioContent);
    }

    @Override
    public void delProgress() {
        completedThreads = 0;
        needCountResults = 1;
        File content = new File(Settings.CONTENT);

        splashView.showProgress(null, "Deleting...");
        splashInteractor.deleteAll(this, content);
    }

    @Override
    public void unZipProgress() {
        completedThreads = 0;
        needCountResults = 2;
        File photoContent = new File(Settings.PHOTO_CONTENT);
        File audioContent = new File(Settings.AUDIO_CONTENT);

        splashView.showProgress(null, "Un zipping...");
        splashInteractor.upZipContent(this, photoContent);
        splashInteractor.upZipContent(this, audioContent);
    }

    @Override
    public void onFinished() {
        synchronized (TAG) {
            completedThreads++;
            if (completedThreads == needCountResults) {
                splashView.hideProgress();
            }
        }
    }
}