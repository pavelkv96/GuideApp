package com.grsu.guideapp.delegation;

import android.support.v4.app.Fragment;
import android.util.Log;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.base.listeners.OnReplaceFragment;
import com.grsu.guideapp.delegation.NavigationDrawerContract.NavigationDrawerView;

public class NavigationDrawerPresenter extends BasePresenterImpl<NavigationDrawerView>
        implements NavigationDrawerContract.NavigationDrawerPresenter, OnReplaceFragment {

    private static final String TAG = NavigationDrawerPresenter.class.getSimpleName();

    @Override
    public void onReplace(Fragment fragment) {
        Log.e(TAG, "onReplace: ");
        getView().getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).commit();
    }
}