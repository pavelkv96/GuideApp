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
import com.grsu.guideapp.fragments.map.MapFragment;

public class NavigationDrawerActivity
        extends BaseDelegationActivity<
        NavigationDrawerView,
        NavigationDrawerPresenter,
        NavigationDrawerDelegate>
        implements NavigationDrawerView {

    private static final int REQUEST_CODE_PERMISSION_READ_CONTACTS = 1;
    private static final String TAG = NavigationDrawerActivity.class.getSimpleName();

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
    public void onSomethingDone() {
//        Toast.makeText(this, "Replace with your own action", Toast.LENGTH_LONG).show();
        Snackbar.make(getContentView(), "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public void openCamera() {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            int permissionStatus = ContextCompat
                    .checkSelfPermission(this, permission.WRITE_EXTERNAL_STORAGE);
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                                permission.WRITE_EXTERNAL_STORAGE,
                                permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSION_READ_CONTACTS);
            } else {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new MapFragment()).commit();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_READ_CONTACTS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new MapFragment()).commit();
                } else {
                    Log.e(TAG, "onRequestPermissionsResult: ");
                    Toast.makeText(this, "Don't have permission", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
