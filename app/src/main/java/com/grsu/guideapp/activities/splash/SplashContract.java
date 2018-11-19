package com.grsu.guideapp.activities.splash;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import java.io.File;

public interface SplashContract {

    interface SplashView extends BaseView {

        void showMessage(String message);

        void writeInSharedPreference(boolean flag);

        void updateViewProgress(int progress);
    }

    interface SplashPresenter extends BasePresenter<SplashView> {
        void getNewProgress();

        void delNewProgress();
    }

    interface SplashInteractor {

        interface OnFinishedListener {

            void onFinished();
        }

        interface OnUpdatedListener {

            void onUpdated(int progress);
        }

        void deleteAll(OnFinishedListener finished, OnUpdatedListener updated, File file);

        void copyAndUnZip(OnSuccessListener<String> success, OnUpdatedListener updated, File file);
    }
}
