package com.grsu.guideapp.fragments.setting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import butterknife.OnClick;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.database.CacheDBHelper;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import com.grsu.guideapp.utils.StorageUtils;
import java.io.File;

public class SettingsFragment extends BaseFragment<SettingPresenter, NavigationDrawerActivity> {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    @NonNull
    @Override
    protected SettingPresenter getPresenterInstance() {
        return new SettingPresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_settings;
    }

    @OnClick(R.id.btn_fragment_settings_clear_content)
    public void buttonClick(View view) {
        Context context = getContext();

        if (context != null) {
            if (StorageUtils.deleteDatabase(context)) {
                Toasts.makeS(context, "Database deleted");
            } else {
                Toasts.makeS(context, "Database not found");
            }
        }
    }

    @OnClick(R.id.btn_fragment_settings_clear_map_cache)
    public void clear(View view) {
        File file = new File(Settings.CACHE, Settings.CACHE_DATABASE_NAME);
        if (file.exists()) {
            CacheDBHelper.clearCache();
            Toasts.makeS(getActivity, "Database deleted");
        } else {
            Toasts.makeS(getActivity, "Database not found");
        }
    }
}