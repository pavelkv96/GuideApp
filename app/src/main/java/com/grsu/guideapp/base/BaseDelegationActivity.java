package com.grsu.guideapp.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public abstract class BaseDelegationActivity<
        V extends BaseView,
        P extends BasePresenterImpl<V>,
        D extends BaseActivityDelegate<V, P>>
        extends BaseActivity<P> {

    protected D mDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate = instantiateDelegateInstance();
        mDelegate.onCreate(mPresenter);
    }

    protected abstract D instantiateDelegateInstance();

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        mDelegate.setContentView();
    }

    @Override
    protected void onDestroy() {
        mDelegate.onDestroy();
        super.onDestroy();
    }

}
