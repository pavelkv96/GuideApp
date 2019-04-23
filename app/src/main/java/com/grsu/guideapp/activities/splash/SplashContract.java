package com.grsu.guideapp.activities.splash;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import java.io.File;

public interface SplashContract {

    interface SplashView extends BaseView {

        void updateViewProgress(int progress);

        void openActivity();
    }

    interface SplashPresenter extends BasePresenter<SplashView> {
        void copyFromAssets(File path, String name);
    }

    interface SplashInteractor {

        interface OnFinishedListener {

            void onFinished();
        }

        interface OnUpdatedListener {

            void onUpdated(int progress);
        }

        void copy(OnUpdatedListener updated, File path, String name);
    }
}
