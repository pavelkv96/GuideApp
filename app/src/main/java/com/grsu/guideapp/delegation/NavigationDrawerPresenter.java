package com.grsu.guideapp.delegation;

import android.support.v4.app.Fragment;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.delegation.NavigationDrawerContract.NavigationDrawerView;

public class NavigationDrawerPresenter
        extends BasePresenterImpl<NavigationDrawerView>
        implements NavigationDrawerContract.NavigationDrawerPresenter {

    @Override
    public void doSomething() {
        mView.onSomethingDone();
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        mView.replaceFragment(fragment);
    }
}
