package com.grsu.guideapp.delegation;

import android.support.v4.app.Fragment;
import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;

public interface NavigationDrawerContract {

    interface NavigationDrawerView extends BaseView {

        void openCamera();

        void onSomethingDone();
        void replaceFragment(Fragment fragment);

        NavigationDrawerActivity getActivity();

    }

    interface NavigationDrawerPresenter extends BasePresenter<NavigationDrawerView> {

        void doSomething();
        void replaceFragment(Fragment fragment);

    }
}
