package com.grsu.guideapp.activityies;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.grsu.guideapp.R;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.StorageUtils;
import java.io.File;

public class SplashActivity extends AppCompatActivity {

    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        unbinder = ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_activity_splash_load)
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
        startActivity(NavigationDrawerActivity.newIntent(SplashActivity.this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}