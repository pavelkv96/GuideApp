package com.grsu.guideapp.activities.splash;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import java.io.File;

public interface SplashContract {

    interface SplashView extends BaseView {

    }


    interface SplashPresenter extends BasePresenter<SplashView> {

        void getProgress();

        void delProgress();

        void unZipProgress();
    }

    interface SplashInteractor {

        interface OnFinishedListener {

            void onFinished();
        }

        void copyContentFromAssets(OnFinishedListener listener, File file);

        void deleteAll(OnFinishedListener listener, File file);

        void upZipContent(OnFinishedListener listener, File file);
    }
}
