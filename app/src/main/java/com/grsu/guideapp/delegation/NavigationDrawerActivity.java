package com.grsu.guideapp.delegation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseDelegationActivity;
import com.grsu.guideapp.delegation.NavigationDrawerContract.NavigationDrawerView;

public class NavigationDrawerActivity extends BaseDelegationActivity<NavigationDrawerView,
        NavigationDrawerPresenter, NavigationDrawerDelegate>
        implements NavigationDrawerView {

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    public static Intent newIntent(Context context) {
        return new Intent(context, NavigationDrawerActivity.class);
    }

    @Override
    protected NavigationDrawerDelegate instantiateDelegateInstance() {
        return new NavigationDrawerDelegate();
    }

    @NonNull
    @Override
    protected NavigationDrawerPresenter getPresenterInstance() {
        return new NavigationDrawerPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        setSupportActionBar(mToolbar);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (!mDelegate.closeDrawer()) {
            super.onBackPressed();
        }
    }

    @Override
    public NavigationDrawerActivity getActivity() {
        return this;
    }
}