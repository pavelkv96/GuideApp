package com.grsu.guideapp.base;

//https://github.com/KucherenkoIhor/DelegationActivityTemplate

import android.view.View;

public interface BaseView {

    void showProgress(String title, String message);

    void hideProgress();

    View getContentView();

}
