package com.grsu.guideapp.activities.splash;

import static com.grsu.guideapp.utils.StreamUtils.copyAssets;
import static com.grsu.guideapp.utils.constants.ConstantsPaths.CACHE;
import static com.grsu.guideapp.utils.constants.ConstantsPaths.CACHE_FILE;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.grsu.guideapp.R;
import com.grsu.guideapp.database.CacheDBHelper;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import java.io.File;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @BindView(R.id.tv_activity_splash_status)
    TextView tv_status;
    File file1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        check();
    }

    @OnClick(R.id.btn_activity_splash_start)
    public void start(View view) {
        finish();
        startActivity(NavigationDrawerActivity.newIntent(this));
    }

    @OnClick(R.id.btn_activity_splash_load)
    public void load(View view){
        File file = new File(getFilesDir() + "/ZoomTables.data");

        if (!file.exists()) {
            copyAssets("ZoomTables.data", getFilesDir() + "/ZoomTables.data", this);
        }

        file1 = getDatabasePath("KA.map");

        if (!file1.exists()) {
            copyAssets("KA.map", getDatabasePath("KA.map").toString(), this);
        }

        check();
    }

    @OnClick(R.id.btn_activity_splash_remove)
    public void clear(View view) {
        CacheDBHelper.clearCache();
        file1.delete();
        check();
    }

    private void check() {
        File file = new File(CACHE, CACHE_FILE);
        if (file.exists()) {
            tv_status.setText("Map exists");
        } else {
            tv_status.setText("None map");
        }
    }

    @Override
    protected void onDestroy() {
        CacheDBHelper.refreshDb();
        super.onDestroy();
    }

}
