package com.grsu.guideapp.fragments.setting;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.preference.ListPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.grsu.guideapp.App;
import com.grsu.guideapp.R;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.fragments.setting.SettingContract.SettingView;
import com.grsu.guideapp.project_settings.SharedPref;

public class SettingFragment extends
        BaseSettingsFragment<SettingPresenter, NavigationDrawerActivity>
        implements SettingView {

    private ListPreference prefLang;

    @NonNull
    @Override
    protected SettingPresenter getPresenterInstance() {
        return new SettingPresenter(this, new SettingInteractor(getActivity));
    }

    @Override
    protected String getTitle() {
        return getString(R.string.settings_fragment);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.pref_general);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        getActivity.setTitleToolbar(getTitle());
        prefLang = (ListPreference) findPreference(SharedPref.KEY_LANGUAGE);
        return rootView;
    }

   /* @OnClick(R.id.btn_fragment_settings_clear_content)
    public void deleteContentCache(View view) {
        if (getActivity != null) {
            if (StorageUtils.deleteDatabase(getActivity)) {
                Toasts.makeS(getActivity, R.string.success_database_deleted);
            } else {
                Toasts.makeS(getActivity, R.string.error_database_not_found);
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
        preferences.edit().putBoolean(SharedPref.KEY_CONTENT, false).apply();
        Toasts.makeS(getActivity, R.string.success_deleted_content_folder);
    }

    @OnClick(R.id.btn_fragment_settings_delete_map_file)
    public void deleteMapFile(View view) {
        final File file = new File(StorageUtils.getDatabasePath(getActivity), Settings.MAP_FILE);
        mPresenter.deleteMapFile(file);
    }

    @OnClick(R.id.btn_fragment_settings_change_language)
    public void onChecked(View view) {
        String s = preference.getString(SharedPref.KEY_LANGUAGE, "en");
        if (s.equals("en")) {
            s = "ru";
            preference.edit().putString(SharedPref.KEY_LANGUAGE, s).apply();
        } else {
            s = "en";
            preference.edit().putString(SharedPref.KEY_LANGUAGE, s).apply();
        }

        App.setLocale(preference, getResources());
        getActivity.recreate();
    }*/

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPref, String key) {
        Editor editor = sharedPref.edit();
        switch (key) {
            case SharedPref.KEY_LANGUAGE: {
                prefLang.setSummary(prefLang.getEntry());

                editor.putString(SharedPref.KEY_LANGUAGE, prefLang.getValue());
                App.setLocale(sharedPref, getActivity.getResources());
                getActivity.recreate();
            }
            break;
        }
        editor.apply();
    }
}
