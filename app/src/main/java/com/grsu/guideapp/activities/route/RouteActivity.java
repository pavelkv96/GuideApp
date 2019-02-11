package com.grsu.guideapp.activities.route;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.listeners.OnFragmentReplace;
import com.grsu.guideapp.fragments.map.MapFragment;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.utils.MessageViewer.Logs;

public class RouteActivity extends AppCompatActivity implements OnFragmentReplace {

    private static final String TAG = RouteActivity.class.getSimpleName();
    FragmentManager fm;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_route);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        super.onCreate(savedInstanceState);
        Logs.e(TAG, "onCreate: ");

        fm = getSupportFragmentManager();

        int id = getIntent().getIntExtra(Constants.KEY_ID_ROUTE, -1);
        String name = getIntent().getStringExtra(Constants.KEY_NAME_ROUTE);

        if (id == -1 && name.isEmpty()) {
            finish();
        } else {
            if (savedInstanceState == null) {
                onReplace(MapFragment.newInstance(id, name));
            }
        }

    }

    public static Intent newIntent(Context context, int id_route, String name_route) {
        Intent intent = new Intent(context, RouteActivity.class);
        intent.putExtra(Constants.KEY_ID_ROUTE, id_route);
        intent.putExtra(Constants.KEY_NAME_ROUTE, name_route);
        return intent;
    }

    @Override
    public void onReplace(Fragment fragment) {
        String backStackName = fragment.getClass().getName();
        fm.beginTransaction()
                .replace(R.id.activity_route_container, fragment)
                .addToBackStack(backStackName)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Logs.e(TAG, "onBackPressed: ");
        if (fm.getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }

    public void setTitleToolbar(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
