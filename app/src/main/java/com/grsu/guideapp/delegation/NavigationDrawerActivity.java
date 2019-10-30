package com.grsu.guideapp.delegation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseDelegationActivity;
import com.grsu.guideapp.delegation.NavigationDrawerContract.NavigationDrawerView;
import com.grsu.guideapp.utils.MessageViewer.Toasts;

public class NavigationDrawerActivity
        extends BaseDelegationActivity<
        NavigationDrawerView,
        NavigationDrawerPresenter,
        NavigationDrawerDelegate>
        implements NavigationDrawerView, Runnable {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private boolean doubleBackToExitPressedOnce = false;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setSupportActionBar(mToolbar);
        mDelegate.setSelectedItem(R.id.nav_slideshow);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            if (!mDelegate.closeDrawer() && doubleBackToExitPressedOnce) {
                super.onBackPressed();
            }

            doubleBackToExitPressedOnce = true;
            Toasts.makeS(this, R.string.message_once_more_to_exit);
            new Handler().postDelayed(this, 2000);
        }
    }

    @Override
    public NavigationDrawerActivity getActivity() {
        return this;
    }

    @Override
    public void openAboutFragment() {
        mDelegate.setSelectedItem(R.id.nav_share);
    }

    @Override
    public void openSettingsFragment() {
        mDelegate.setSelectedItem(R.id.nav_settings);
    }

    @Override
    public void openListRoutesFragment() {
        mDelegate.setSelectedItem(R.id.nav_slideshow);
    }

    @Override
    public void openCatalogFragment() {
        mDelegate.setSelectedItem(R.id.nav_gallery);
    }

    @Override
    public void showToastMessage(String message) {
        Toasts.makeS(this, message);
    }

    public void setTitleToolbar(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void run() {
        doubleBackToExitPressedOnce = false;
    }
}
