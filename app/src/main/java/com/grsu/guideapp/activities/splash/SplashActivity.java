package com.grsu.guideapp.activities.splash;

import static com.grsu.guideapp.utils.StreamUtils.copyAssets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.grsu.guideapp.R;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import java.io.File;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_activity_splash_load_and_start)
    public void load(View view) {
        File file = new File(getFilesDir() + "/ZoomTables.data");
        File file1 = getDatabasePath("KA.map");

        if (!file.exists()) {
            copyAssets("ZoomTables.data", getFilesDir() + "/ZoomTables.data", this);
        }

        if (!file1.exists()) {
            copyAssets("KA.map", getDatabasePath("KA.map").toString(), this);
        }

        startActivity(NavigationDrawerActivity.newIntent(this));
    }
}
