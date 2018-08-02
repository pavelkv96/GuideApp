package com.grsu.guideapp.delegation;

import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;
import butterknife.BindView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseDelegationActivity;
import com.grsu.guideapp.delegation.NavigationDrawerContract.NavigationDrawerView;
import com.grsu.guideapp.fragments.ListRoutesFragment;
import com.grsu.guideapp.fragments.map.MapFragment;
import com.grsu.guideapp.fragments.setting.SettingsFragment;
import com.grsu.guideapp.utils.MessageViewer;

public class NavigationDrawerActivity
        extends BaseDelegationActivity<
        NavigationDrawerView,
        NavigationDrawerPresenter,
        NavigationDrawerDelegate>
        implements NavigationDrawerView {

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    /*public static Intent newIntent(Context context) {
        return new Intent(context, NavigationDrawerActivity.class);
    }*/

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

    @Override
    public void openMapFragment() {
        if (mPresenter.checkPermissions()) {
            mPresenter.replaceFragment(new MapFragment());
        }
    }

    @Override
    public void openSettingsFragment() {
        mPresenter.replaceFragment(new SettingsFragment());
    }

    @Override
    public void openListRoutesFragment() {
        mPresenter.replaceFragment(new ListRoutesFragment());
    }

    @Override
    public void showToastMessage(String message) {
        MessageViewer.Toasts.makeS(this, message);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        mPresenter.checkPermissionsResult(requestCode, grantResults);
    }
}
