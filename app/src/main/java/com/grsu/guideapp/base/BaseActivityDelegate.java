package com.grsu.guideapp.base;

public abstract class BaseActivityDelegate<
        V extends BaseView,
        P extends BasePresenterImpl<V>> {

    //private Unbinder mUnBinder = null;

    protected P mPresenter;

    public void onCreate(P presenter) {
        mPresenter = presenter;
    }

    protected void setContentView() {
        //mUnBinder = ButterKnife.bind(this, mPresenter.getView().getContentView());
    }

    public void onDestroy() {
        //mUnBinder.unbind();
    }
}
