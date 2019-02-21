package com.grsu.guideapp.fragments.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.OnClick;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.database.CacheDBHelper;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.fragments.setting.SettingContract.SettingView;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import com.grsu.guideapp.utils.StorageUtils;
import java.io.File;

public class SettingsFragment extends BaseFragment<SettingPresenter, NavigationDrawerActivity> implements
        SettingView {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    @NonNull
    @Override
    protected SettingPresenter getPresenterInstance() {
        return new SettingPresenter(this, new SettingInteractor(getActivity));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_settings;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.settings_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        getActivity.setTitleToolbar(getTitle());
        return rootView;
    }

    @OnClick(R.id.btn_fragment_settings_clear_content)
    public void deleteContentCache(View view) {
        Context context = getContext();

        if (context != null) {
            if (StorageUtils.deleteDatabase(context)) {
                Toasts.makeS(context, R.string.success_database_deleted);
            } else {
                Toasts.makeS(context, R.string.error_database_not_found);
            }
        }
    }

    @OnClick(R.id.btn_fragment_settings_clear_map_cache)
    public void deleteMapCache(View view) {
        File file = new File(Settings.CACHE, Settings.CACHE_DATABASE_NAME);
        if (file.exists()) {
            CacheDBHelper.clearCache();
            Toasts.makeS(getActivity, R.string.success_database_deleted);
        } else {
            Toasts.makeS(getActivity, R.string.error_database_not_found);
        }
    }

    @OnClick(R.id.btn_fragment_settings_delete_content)
    public void deleteContent(View view) {
        final File file = new File(Settings.CONTENT);
        new Thread(new Runnable() {
            @Override
            public void run() {
                StorageUtils.deleteRecursive(file);
            }
        }).start();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity);
        preferences.edit().putBoolean("content", false).apply();
        Toasts.makeS(getActivity, R.string.success_deleted_content_folder);
    }

    @OnClick(R.id.btn_fragment_settings_delete_map_file)
    public void deleteMapFile(View view) {
        final File file = new File(StorageUtils.getDatabasePath(getActivity), Settings.MAP_FILE);
        mPresenter.deleteMapFile(file);
    }
}
