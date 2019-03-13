package com.grsu.guideapp.activities.splash;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import com.grsu.guideapp.App;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.splash.SplashContract.SplashView;
import com.grsu.guideapp.base.BaseActivity;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import com.grsu.guideapp.utils.StorageUtils;
import com.grsu.ui.progress.CustomProgressBar;
import java.io.File;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView {

    private SharedPreferences preferences;
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

        progress_view = findViewById(R.id.current_progress);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        otherContent();
    }

    @Override
    public void updateViewProgress(final int progress) {
        App.getThread().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                progress_view.setProgress(progress);
            }
        });
    }

    @Override
    public void openActivity() {
        App.getThread().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                Toasts.makeL(SplashActivity.this, android.R.string.ok);
            }
        });
        startActivity(NavigationDrawerActivity.newIntent(this));
        finish();
    }

    public void otherContent() {
        File file = new File(getFilesDir(), Settings.ZOOM_TABLE);
        File map = new File(StorageUtils.getDatabasePath(this), Settings.MAP_FILE);

        mPresenter.copyInAssets(file, Settings.ZOOM_TABLE);
        /*if (!file.exists()) {
            StorageUtils.copyAssets(file.getAbsolutePath(), Settings.ZOOM_TABLE, manager);
        }*/

        mPresenter.copyInAssets(map, Settings.MAP_FILE);
        /*if (!map.exists()) {
            //StorageUtils.copyAssets(map.getAbsolutePath(), Settings.MAP_FILE, manager);
        }*/

        Editor editor = preferences.edit();
        if (!preferences.contains(Constants.KEY_SINGLE_CHOICE_ITEM)) {
            editor.putInt(Constants.KEY_SINGLE_CHOICE_ITEM, 1000);
        }
        editor.apply();
    }
}
