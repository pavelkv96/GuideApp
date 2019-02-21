package com.grsu.guideapp.activities.splash;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.splash.SplashContract.SplashView;
import com.grsu.guideapp.base.BaseActivity;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.CheckSelfPermission;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import com.grsu.guideapp.utils.StorageUtils;
import com.grsu.ui.progress.CustomProgressBar;
import java.io.File;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private SharedPreferences preferences;
    private int progress = 0;
    private CustomProgressBar progress_view;

    @NonNull
    @Override
    protected SplashPresenter getPresenterInstance() {
        return new SplashPresenter(this, new SplashInteractor(getAssets()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (CheckSelfPermission.writeExternalStorageIsGranted(this)) {
            Logs.e(TAG, "true");
            String[] storage = CheckSelfPermission.groupExternalStorage;
            ActivityCompat.requestPermissions(this, storage, 1);
        } else {
            Logs.e(TAG, "false");
            start();
        }
    }

    @Override
    public void showMessage(final String message) {
        otherContent();
        updateViewProgress(2);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toasts.makeL(SplashActivity.this, message);
            }
        });
    }

    @Override
    public void writeInSharedPreference(boolean flag) {
        Editor editor = preferences.edit();
        editor.putBoolean("content", flag);
        editor.apply();
    }

    @Override
    public void updateViewProgress(final int newProgress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (TAG) {
                    progress += newProgress;
                    progress_view.setProgress(progress);
                }
            }
        });
    }

    public void otherContent() {
        File file = new File(getFilesDir(), Settings.ZOOM_TABLE);
        File file1 = new File(StorageUtils.getDatabasePath(this), Settings.MAP_FILE);
        AssetManager assetManager = getAssets();

        if (!file.exists()) {
            StorageUtils.copyAssets(Settings.ZOOM_TABLE, file.getAbsolutePath(), assetManager);
        }

        if (!file1.exists()) {
            String toFilePath = file1.getAbsolutePath();
            StorageUtils.copyAssets(Settings.MAP_FILE, toFilePath, assetManager);
        }

        Editor editor = preferences.edit();
        if (!preferences.contains(Constants.KEY_SINGLE_CHOICE_ITEM)) {
            editor.putInt(Constants.KEY_SINGLE_CHOICE_ITEM, 100);
        }
        editor.apply();

        startActivity(NavigationDrawerActivity.newIntent(this));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (CheckSelfPermission.isAllGranted(grantResults) && requestCode == 1) {
            start();
        } else {
            finish();
        }
    }

    private void start() {
        progress_view = findViewById(R.id.current_progress);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!preferences.contains("content")) {
            Editor editor = preferences.edit();
            editor.putBoolean("content", false);
            editor.apply();
        }

        if (!preferences.getBoolean("content", false)) {
            mPresenter.delNewProgress();
        } else {
            otherContent();
        }
    }
}
