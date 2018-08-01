package com.grsu.guideapp.base;

//https://github.com/KucherenkoIhor/DelegationActivityTemplate

import android.view.View;

public interface BaseView {

    void showProgress();

    void hideProgress();

    View getContentView();

}
