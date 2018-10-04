package com.grsu.guideapp.delegation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import com.grsu.guideapp.fragments.MapFragment;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseDelegationActivity;
import com.grsu.guideapp.delegation.NavigationDrawerContract.NavigationDrawerView;

public class NavigationDrawerActivity extends BaseDelegationActivity<NavigationDrawerView,
        NavigationDrawerPresenter, NavigationDrawerDelegate>
        implements NavigationDrawerView {

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    private FragmentManager manager;

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
        return new NavigationDrawerPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        setSupportActionBar(mToolbar);
        super.onCreate(savedInstanceState);

        manager = getSupportFragmentManager();
    }

    @Override
    public void onBackPressed() {
        if (!mDelegate.closeDrawer()) {
            super.onBackPressed();
        }
    }

    @Override
    public void openMapFragment() {
        mPresenter.onReplace(MapFragment.newInstance());
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public NavigationDrawerActivity getActivity() {
        return this;
    }
}