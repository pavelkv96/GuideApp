package com.grsu.guideapp.activities.splash;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import butterknife.OnClick;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseActivity;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.activities.splash.SplashContract.SplashView;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.StorageUtils;
import java.io.File;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @NonNull
    @Override
    protected SplashPresenter getPresenterInstance() {
        return new SplashPresenter(this, new SplashInteractor(getAssets()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @OnClick(R.id.btn_activity_splash_load_and_start)
    public void load(View view) {
        File file = new File(getFilesDir(), Settings.ZOOM_TABLE);
        File file1 = getDatabasePath(Settings.MAP_FILE);
        AssetManager assetManager = getAssets();

        if (!file.exists()) {
            StorageUtils.copyAssets(Settings.ZOOM_TABLE, file.getAbsolutePath(), assetManager);
        }

        if (!file1.exists()) {
            String toFilePath = file1.getAbsolutePath();
            StorageUtils.copyAssets(Settings.MAP_FILE, toFilePath, assetManager);
        }

        startActivity(NavigationDrawerActivity.newIntent(this));
    }

    @OnClick(R.id.btn_activity_splash_loading)
    public void loading(View view) {
        mPresenter.getProgress();
    }

    @OnClick(R.id.btn_activity_splash_delete)
    public void delete(View view) {
        mPresenter.delProgress();
    }

    @OnClick(R.id.btn_activity_splash_un_zip)
    public void unZip(View view) {
        mPresenter.unZipProgress();
    }
}
