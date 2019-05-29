package com.grsu.guideapp.activities.splash;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.grsu.guideapp.App;
import com.grsu.guideapp.BuildConfig;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.splash.SplashContract.SplashView;
import com.grsu.guideapp.base.BaseActivity;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.network.model.Datum;
import com.grsu.guideapp.network.model.Root;
import com.grsu.guideapp.network.model.Timestamp;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.project_settings.SharedPref;
import com.grsu.guideapp.utils.CheckPermission;
import com.grsu.guideapp.utils.StorageUtils;
import com.grsu.ui.progress.CustomProgressBar;
import java.io.File;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView {

    @BindView(R.id.current_progress)
    CustomProgressBar progress_view;

    @BindView(R.id.btn_activity_splash_next)
    Button btn_activity_splash_next;

    @BindView(R.id.btn_activity_splash_settings)
    Button btn_activity_splash_settings;

    @BindView(R.id.tv_activity_splash_title)
    TextView tv_activity_splash_title;

    @BindView(R.id.tv_activity_splash_description)
    TextView tv_activity_splash_description;

    @NonNull
    @Override
    protected SplashPresenter getPresenterInstance() {
        return new SplashPresenter(this, new SplashInteractor(getAssets()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        boolean isContains = preferences.contains(SharedPref.KEY_SPLASH);
        if (isContains && CheckPermission.checkStoragePermission(this)) {
            openActivity();
            if (App.isOnline()) {
                checkUpdate();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        btn_activity_splash_next.setVisibility(View.VISIBLE);
        btn_activity_splash_settings.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_activity_splash_next)
    public void buttonNext() {
        if (CheckPermission.checkStoragePermission(this)) {
            otherContent();
        } else {
            ActivityCompat.requestPermissions(this, CheckPermission.groupStorageAndLocation, 1);
        }
    }

    @OnClick(R.id.btn_activity_splash_close)
    public void buttonClose() {
        finish();
    }

    @OnClick(R.id.btn_activity_splash_settings)
    public void buttonSettings() {
        CheckPermission.settingsIntent(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (CheckPermission.checkStoragePermission(this)) {
                btn_activity_splash_next.setEnabled(false);
                otherContent();
                return;
            }
        }

        btn_activity_splash_settings.setVisibility(View.VISIBLE);
        btn_activity_splash_next.setVisibility(View.GONE);

        String s = "Предоставте доступ";
        String text = "Предоставте приложению доступ к памяти в настройках вашего устройства, иначе использовать приложение будет невозможно.";

        tv_activity_splash_title.setText(s);
        tv_activity_splash_description.setText(text);
    }

    @Override
    public void updateViewProgress(final int progress) {
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                progress_view.setProgress(progress);
            }
        });
    }

    @Override
    public void openActivity() {
        if (!preferences.contains(SharedPref.KEY_SPLASH)) {
            preferences.edit().putBoolean(SharedPref.KEY_SPLASH, true).apply();
        }
        startActivity(NavigationDrawerActivity.newIntent(this));
        finish();
    }

    public void otherContent() {
        progress_view.setVisibility(View.VISIBLE);
        File photos = new File(Settings.CONTENT);
        if (!photos.exists()) {
            photos.mkdirs();
        }
        File file = new File(getFilesDir(), Settings.ZOOM_TABLE);
        File map = new File(StorageUtils.getDatabasePath(this), Settings.MAP_FILE);
        StorageUtils.copyAssetsFolder(photos, "photo", getAssets());

        mPresenter.copyFromAssets(file, Settings.ZOOM_TABLE);
        mPresenter.copyFromAssets(map, Settings.MAP_FILE);
    }

    private void checkUpdate() {
        String apiKey = BuildConfig.ApiKey;
        String datetime = new Test(this).getLastCheck();
        if (datetime == null) {
            return;
        }

        Call<Root> root = App.getThread().networkIO().checkUpdateRoute(apiKey, datetime);
        root.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(@NonNull Call<Root> call, @NonNull Response<Root> response) {
                if (response.body() != null) {
                    SparseArray<Timestamp> updateIds = new SparseArray<>();
                    for (Datum datum : response.body().getDatums()){
                        updateIds.put(datum.getId(), datum.getData().getRoute().getTimestamp());
                    }
                    new Test(SplashActivity.this).setHaveUpdate(updateIds);
                }
                Log.e("TAG", "onResponse: " + call.request().url());
                Log.e("TAG", "onResponse: " + new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<Root> call, @NonNull Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage(), t);
            }
        });
    }
}