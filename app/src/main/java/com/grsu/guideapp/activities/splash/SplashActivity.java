package com.grsu.guideapp.activities.splash;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.grsu.guideapp.App;
import com.grsu.guideapp.BuildConfig;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.splash.SplashContract.SplashView;
import com.grsu.guideapp.base.BaseActivity;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.network.model.Datum;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.CheckPermission;
import com.grsu.guideapp.utils.StorageUtils;
import com.grsu.ui.progress.CustomProgressBar;
import java.io.File;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView,
        View.OnClickListener {

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
        if (preferences.contains("splash")) {
            openActivity();
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
        if (CheckPermission.canWriteStorage(this)) {
            otherContent();
        } else {
            ActivityCompat.requestPermissions(this, CheckPermission.groupStorage, 1);
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
            if (CheckPermission.isAllGranted(grantResults)) {
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
        if (!preferences.contains("splash")) {
            preferences.edit().putBoolean("splash", true).apply();
        }
        /*App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                showToast(android.R.string.ok);
            }
        });*/
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
//        check();

        mPresenter.copyFromAssets(file, Settings.ZOOM_TABLE);
        mPresenter.copyFromAssets(map, Settings.MAP_FILE);
    }

    private void check() {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                if (App.isOnline()) {
                    Call<List<Datum>> routes = App.getThread().networkIO()
                            .getRoutes(BuildConfig.ApiKey);
                    try {
                        Response<List<Datum>> response = routes.execute();
                        if (response.isSuccessful()) {
                            new Test(SplashActivity.this).loadRoute(response.body());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        check();
    }
}
/*
package com.grsu.guideapp.activities.splash;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.view.View;
import com.grsu.guideapp.App;
import com.grsu.guideapp.BuildConfig;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.splash.SplashContract.SplashView;
import com.grsu.guideapp.base.BaseActivity;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.network.model.Datum;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.CheckPermission;
import com.grsu.guideapp.utils.MessageViewer.MySnackbar;
import com.grsu.guideapp.utils.StorageUtils;
import com.grsu.ui.progress.CustomProgressBar;
import java.io.File;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView,
        View.OnClickListener {

    private CustomProgressBar progress_view;
    AlertDialog dialog;

    @NonNull
    @Override
    protected SplashPresenter getPresenterInstance() {
        return new SplashPresenter(this, new SplashInteractor(getAssets()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Builder builder = new Builder(this);
        builder.setTitle("Диалоговое окно");
        builder.setMessage("Продолжить работу");
        builder.setNegativeButton(android.R.string.cancel, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setPositiveButton(R.string.error_snackbar_open_settings, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CheckPermission.settingsIntent(SplashActivity.this);
            }
        });

        dialog = builder.create();

        progress_view = findViewById(R.id.current_progress);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (CheckPermission.canWriteStorage(this)) {
            otherContent();
        } else {
            ActivityCompat.requestPermissions(this, CheckPermission.groupStorage, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (CheckPermission.isAllGranted(grantResults)) {
                otherContent();
                return;
            }
        }
        dialog.show();
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
        startActivity(NavigationDrawerActivity.newIntent(this));
        finish();
    }

    public void otherContent() {
        File photos = new File(Settings.CONTENT);
        if (!photos.exists()) {
            photos.mkdirs();
        }
        File file = new File(getFilesDir(), Settings.ZOOM_TABLE);
        File map = new File(StorageUtils.getDatabasePath(this), Settings.MAP_FILE);
        StorageUtils.copyAssetsFolder(photos, "photo", getAssets());
//        check();

        mPresenter.copyFromAssets(file, Settings.ZOOM_TABLE);
        mPresenter.copyFromAssets(map, Settings.MAP_FILE);
    }

    private void check() {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                if (App.isOnline()) {
                    Call<List<Datum>> routes = App.getThread().networkIO()
                            .getRoutes(BuildConfig.ApiKey);
                    try {
                        Response<List<Datum>> response = routes.execute();
                        if (response.isSuccessful()) {
                            new Test(SplashActivity.this).loadRoute(response.body());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    View view = findViewById(R.id.coordinator_layout_1);
                    MySnackbar.makeL(view, "Нет доступа к интернету", SplashActivity.this,
                            "Повторить");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        check();
    }
}*/
