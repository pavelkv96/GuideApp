package com.grsu.guideapp.fragments.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.recyclerview.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.grsu.guideapp.App;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseSettingsFragment;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.fragments.setting.SettingContract.SettingView;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.project_settings.SharedPref;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import com.grsu.guideapp.utils.StorageUtils;
import java.io.File;

public class SettingFragment extends
        BaseSettingsFragment<SettingPresenter, NavigationDrawerActivity>
        implements SettingView, OnPreferenceClickListener {

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().addItemDecoration(new DividerItemDecoration(getActivity, 1));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        getActivity.setTitleToolbar(getTitle());
        prefLang = (ListPreference) findPreference(SharedPref.KEY_LANGUAGE);
        prefLang.setSummary(prefLang.getEntry());

        findPreference(SharedPref.KEY_CONTENT).setOnPreferenceClickListener(this);
        findPreference(SharedPref.KEY_MAP_CONTENT).setOnPreferenceClickListener(this);
        findPreference(SharedPref.KEY_MAP_DELETE_FILE).setOnPreferenceClickListener(this);
        return rootView;
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String key) {
        addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPref, String key) {
        Editor editor = sharedPref.edit();
        switch (key) {
            case SharedPref.KEY_LANGUAGE: {
                prefLang.setSummary(prefLang.getEntry());

                editor.putString(SharedPref.KEY_LANGUAGE, prefLang.getValue()).apply();
                App.getInstance().setLocale(sharedPref, App.getInstance());
                getActivity.recreate();
            }
            break;
            default: break;
        }
        editor.apply();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case SharedPref.KEY_CONTENT: {
                Context context = getContext();
                if (context != null) {
                    if (StorageUtils.deleteDatabase(context)) {
                        Toasts.makeS(context, R.string.success_database_deleted);
                    } else {
                        Toasts.makeS(context, R.string.error_database_not_found);
                    }

                    File database = context.getDatabasePath(Settings.DATABASE_INFORMATION_NAME);
                    if (!database.exists()) {
                        StorageUtils.copyDatabase(context);
                    }
                }
            }
            break;
            case SharedPref.KEY_MAP_CONTENT: {
                File file = new File(Settings.CACHE, Settings.CACHE_DATABASE_NAME);
                if (file.exists()) {
                    //TODO clear cache database
                    Toasts.makeS(getActivity, R.string.success_database_deleted);
                } else {
                    Toasts.makeS(getActivity, R.string.error_database_not_found);
                }
            }
            break;
            case SharedPref.KEY_MAP_DELETE_FILE: {
                File file = new File(StorageUtils.getDatabasePath(getActivity), Settings.MAP_FILE);
                mPresenter.deleteMapFile(file);
            }
            break;
        }
        return false;
    }
}
