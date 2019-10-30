package com.grsu.guideapp.base;

//https://github.com/KucherenkoIhor/DelegationActivityTemplate

import android.support.annotation.StringRes;
import android.view.View;

public interface BaseView {

    void showProgress(String title, String message);

    void showToast(String message);

    void showToast(@StringRes int message);

    void changeProgress(int progress);

    void hideProgress();

    View getContentView();

}
