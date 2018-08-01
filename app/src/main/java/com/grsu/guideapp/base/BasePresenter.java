package com.grsu.guideapp.base;

public interface BasePresenter<V extends BaseView> {

    void attachView(V view);

    void detachView();


}
