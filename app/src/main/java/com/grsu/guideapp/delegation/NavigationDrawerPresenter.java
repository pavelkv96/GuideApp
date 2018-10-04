package com.grsu.guideapp.delegation;

import android.support.v4.app.Fragment;
import android.util.Log;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.base.listeners.OnReplaceFragment;
import com.grsu.guideapp.delegation.NavigationDrawerContract.NavigationDrawerView;

public class NavigationDrawerPresenter extends BasePresenterImpl<NavigationDrawerView>
        implements NavigationDrawerContract.NavigationDrawerPresenter, OnReplaceFragment {

    private NavigationDrawerView drawerView;

    private static final String TAG = NavigationDrawerPresenter.class.getSimpleName();

    NavigationDrawerPresenter(NavigationDrawerView drawerView) {
        this.drawerView = drawerView;
    }

    @Override
    public void onReplace(Fragment fragment) {
        drawerView.replaceFragment(fragment);
    }
}