package com.grsu.guideapp.delegation;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;

public interface NavigationDrawerContract {

    interface NavigationDrawerView extends BaseView {

        NavigationDrawerActivity getActivity();
    }

    interface NavigationDrawerPresenter extends BasePresenter<NavigationDrawerView> {

    }
}