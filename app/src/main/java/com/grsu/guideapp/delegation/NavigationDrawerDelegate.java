package com.grsu.guideapp.delegation;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.BindView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseActivityDelegate;
import com.grsu.guideapp.delegation.NavigationDrawerContract.NavigationDrawerView;

public class NavigationDrawerDelegate
        extends BaseActivityDelegate<NavigationDrawerView, NavigationDrawerPresenter>
        implements OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    protected Toolbar mToolBar;
    @BindView(R.id.nav_view)
    protected NavigationView mNavigationView;

    @Override
    public void onCreate(NavigationDrawerPresenter presenter) {
        super.onCreate(presenter);
        configureDrawer();
    }

    private void configureDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                mPresenter.getView().getActivity(),
                mDrawerLayout,
                mToolBar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_camera:
                mPresenter.getView().openMapFragment();
                break;
            case R.id.nav_gallery:
                mPresenter.getView().openSettingsFragment();
                break;
            case R.id.nav_slideshow:
                mPresenter.getView().openListRoutesFragment();
                break;
            case R.id.nav_manage:

                break;
            case R.id.nav_share:

                break;
            case R.id.nav_send:

                break;
        }
        closeDrawer();
        return true;
    }

    public boolean closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else {
            return false;
        }
    }

}
