package com.grsu.guideapp.activities.route;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import com.grsu.guideapp.App;
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
    protected void attachBaseContext(Context newBase) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        super.attachBaseContext(App.getInstance().setLocale(preferences, newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_route);
        Drawable drawable = ContextCompat.getDrawable(this ,R.drawable.ic_filter);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(drawable);
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
        bundle.putString(Constants.KEY_NAME_ROUTE, route.getNameRoute().getName());
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
