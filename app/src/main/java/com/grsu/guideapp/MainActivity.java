package com.grsu.guideapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_activity_main_start).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(NavigationDrawerActivity.newIntent(MainActivity.this));
            }
        });
    }
}