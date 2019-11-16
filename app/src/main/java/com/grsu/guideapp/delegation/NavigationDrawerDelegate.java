package com.grsu.guideapp.delegation;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseActivityDelegate;
import com.grsu.guideapp.delegation.NavigationDrawerContract.NavigationDrawerView;
import com.grsu.guideapp.fragments.about.AboutFragment;
import com.grsu.guideapp.fragments.categories.CatalogFragment;
import com.grsu.guideapp.fragments.setting.SettingFragment;
import com.grsu.guideapp.fragments.tabs.list_routes.ListRoutesFragment;

public class NavigationDrawerDelegate
        extends BaseActivityDelegate<NavigationDrawerView, NavigationDrawerPresenter>
        implements OnNavigationItemSelectedListener {

//    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;
//    @BindView(R.id.toolbar)
    protected Toolbar mToolBar;
//    @BindView(R.id.nav_view)
    protected NavigationView mNavigationView;

    @Override
    public void onCreate(NavigationDrawerPresenter presenter) {
        super.onCreate(presenter);
    }

    @Override
    protected void setContentView() {
        super.setContentView();
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
        onNavigationItemSelected(mNavigationView.getMenu().findItem(R.id.nav_slideshow));
    }

    public Integer getSelectedItem() {
        MenuItem item = mNavigationView.getCheckedItem();
        return item == null ? null : item.getItemId();
    }

    public void setSelectedItem(@IdRes final int idItem) {
        onNavigationItemSelected(mNavigationView.getMenu().findItem(idItem));
        mNavigationView.post(new Runnable() {
            @Override
            public void run() {
                mNavigationView.setCheckedItem(idItem);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_camera:
                //mPresenter.getView().openMapFragment();
                break;
            case R.id.nav_gallery:
                mPresenter.replaceFragment(new CatalogFragment());
                break;
            case R.id.nav_slideshow:
                mPresenter.replaceFragment(new ListRoutesFragment());
                break;
            case R.id.nav_settings:
                mPresenter.replaceFragment(new SettingFragment());
                break;
            case R.id.nav_share:
                mPresenter.replaceFragment(new AboutFragment());
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
