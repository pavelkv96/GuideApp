package com.grsu.guideapp.delegation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.delegation.NavigationDrawerContract.NavigationDrawerView;
import com.grsu.guideapp.utils.MessageViewer.Logs;

public class NavigationDrawerPresenter extends BasePresenterImpl<NavigationDrawerView>
        implements NavigationDrawerContract.NavigationDrawerPresenter {

    private static final String TAG = NavigationDrawerPresenter.class.getSimpleName();

    @Override
    public void replaceFragment(Fragment fragment) {
        Logs.e(TAG, "replaceFragment: No");
        FragmentManager manager = getView().getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, fragment).commit();
    }
}
