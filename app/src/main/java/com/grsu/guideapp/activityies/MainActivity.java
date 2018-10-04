package com.grsu.guideapp.activityies;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import com.grsu.guideapp.R;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.utils.StorageUtils;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_activity_main_start).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(getFilesDir(), "ZoomTables.data");
                AssetManager assetManager = getAssets();

                if (!file.exists()) {
                    StorageUtils.copyAssets("ZoomTables.data", file.getAbsolutePath(), assetManager);
                }
                startActivity(NavigationDrawerActivity.newIntent(MainActivity.this));
            }
        });
    }
}