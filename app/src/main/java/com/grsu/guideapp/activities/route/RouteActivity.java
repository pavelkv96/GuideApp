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
import com.grsu.guideapp.fragments.route_preview.RoutePreviewFragment;
import com.grsu.guideapp.models.Route;
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

        Bundle args = getIntent().getBundleExtra(Constants.KEY_BUNDLE_ROUTE);
        if (args == null) {
            finish();
        } else {
            if (savedInstanceState == null) {
                onReplace(RoutePreviewFragment.newInstance(args));
            }
        }

    }

    public static Intent newIntent(Context context, Route route) {
        Intent intent = new Intent(context, RouteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_ID_ROUTE, route.getIdRoute());
        bundle.putString(Constants.KEY_NAME_ROUTE, route.getNameRoute());
        bundle.putString(Constants.KEY_SOUTHWEST, route.getSouthwest());
        bundle.putString(Constants.KEY_NORTHEAST, route.getNortheast());
        intent.putExtra(Constants.KEY_BUNDLE_ROUTE, bundle);
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
