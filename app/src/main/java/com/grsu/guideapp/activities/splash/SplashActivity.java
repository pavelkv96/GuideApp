package com.grsu.guideapp.activities.splash;

import static com.grsu.guideapp.utils.StreamUtils.copyAssets;
import static com.grsu.guideapp.utils.constants.Constants.PROVIDER_MAPSFORGE;
import static com.grsu.guideapp.utils.constants.ConstantsPaths.CACHE;
import static com.grsu.guideapp.utils.constants.ConstantsPaths.CACHE_FILE;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.database.CacheDBHelper;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.mf.MapsForgeTileSource;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import com.grsu.guideapp.utils.StorageUtils;
import java.io.File;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

public class SplashActivity extends AppCompatActivity implements OnClickListener {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private static Button btn_start;
    private Button btn_load;
    private Button btn_remove;
    private static TextView tv_status;
    private static Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;

        btn_start = findViewById(R.id.btn_activity_splash_start);
        btn_load = findViewById(R.id.btn_activity_splash_load);
        btn_remove = findViewById(R.id.btn_activity_splash_remove);
        tv_status = findViewById(R.id.tv_activity_splash_status);

        btn_start.setOnClickListener(this);
        btn_load.setOnClickListener(this);
        btn_remove.setOnClickListener(this);

        File file = new File(getFilesDir() + "/ZoomTables.data");

        if (!file.exists()) {
            copyAssets("ZoomTables.data", getFilesDir() + "/ZoomTables.data", this);
        }

        check();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_activity_splash_start: {
                finish();
                startActivity(NavigationDrawerActivity.newIntent(this));
            }
            break;

            case R.id.btn_activity_splash_load: {
                if (check()) {
                    new CacheDBHelper(this);
                    btn_start.setEnabled(false);
                    loadMap();
                }
            }
            break;

            case R.id.btn_activity_splash_remove: {
                CacheDBHelper.clearCache();
                check();
            }
            break;
        }
    }

    private static boolean check() {
        File file = new File(CACHE, CACHE_FILE);
        if (file.exists()) {
            tv_status.setText("Map exists");
            return false;
        } else {
            tv_status.setText("None map");
            return true;
        }
    }

    private void loadMap() {
        AndroidGraphicFactory.createInstance(this.getApplication());
        Context cxt = getApplicationContext();
        File file = new File(StorageUtils.getStorage() + "/osmdroid/KA.map");

        Toasts.makeL(cxt, "Loaded map file " + file.exists());

        if (file.exists()) {
            MapsForgeTileSource.createFromFiles(new File[]{file}, null, PROVIDER_MAPSFORGE);
            MapUtils.getTileInRange(53.9229, 23.5187, 53.7850, 23.8790, 11, 18);
            //MapUtils.getTileInRange(53.7597,23.7099, 53.5986,23.9845, 11, 17);
        }

    }

    public static void show() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btn_start.setEnabled(true);
                check();
                AndroidGraphicFactory.clearResourceMemoryCache();
            }
        });
    }

    @Override
    protected void onDestroy() {
        CacheDBHelper.refreshDb();
        MapsForgeTileSource.dispose();
        AndroidGraphicFactory.clearResourceMemoryCache();
        super.onDestroy();
    }

}
