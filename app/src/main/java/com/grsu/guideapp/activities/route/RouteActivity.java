package com.grsu.guideapp.activities.route;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.listeners.OnFragmentReplace;
import com.grsu.guideapp.fragments.map.MapFragment;
import com.grsu.guideapp.utils.MessageViewer.Logs;

public class RouteActivity extends AppCompatActivity implements OnFragmentReplace {

    private static final String TAG = RouteActivity.class.getSimpleName();
    FragmentManager fm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logs.e(TAG, "onCreate: ");
        setContentView(R.layout.activity_route);

        fm = getSupportFragmentManager();

        Integer id = getIntent().getIntExtra("KEY", -1);

        if (id == -1) {
            finish();
        } else {
            if (savedInstanceState == null) {
                onReplace(MapFragment.newInstance(id));
            }
        }

    }

    public static Intent newIntent(Context context, Integer id_route) {
        Intent intent = new Intent(context, RouteActivity.class);
        intent.putExtra("KEY", id_route);
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
    public void onBackPressed() {
        Logs.e(TAG, "onBackPressed: ");
        if (fm.getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }

}
