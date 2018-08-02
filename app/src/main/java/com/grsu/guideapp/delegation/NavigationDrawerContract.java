package com.grsu.guideapp.delegation;

import android.support.v4.app.Fragment;
import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;

public interface NavigationDrawerContract {

    interface NavigationDrawerView extends BaseView {

        void openMapFragment();

        void openSettingsFragment();

        void openListRoutesFragment();

        void showToastMessage(String message);

        NavigationDrawerActivity getActivity();

    }

    interface NavigationDrawerPresenter extends BasePresenter<NavigationDrawerView> {

        void replaceFragment(Fragment fragment);

        boolean checkPermissions();

        void checkPermissionsResult(int requestCode, int[] grantResults);

    }
}
