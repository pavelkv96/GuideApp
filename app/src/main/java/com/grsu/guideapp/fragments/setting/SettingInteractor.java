package com.grsu.guideapp.fragments.setting;

import android.content.Context;
import android.os.Handler;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.StorageUtils;
import java.io.File;

public class SettingInteractor implements SettingContract.SettingInteractor {

    private Context context;

    public SettingInteractor(Context context) {
        this.context = context;
    }

    @Override
    public void deleteMapFile(final OnFinishedListener<String> listener, final File path) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                StorageUtils.deleteRecursive(path);
                if (!path.exists()) {
                    String toFilePath = path.getAbsolutePath();
                    StorageUtils.copyAssets(toFilePath, Settings.MAP_FILE, context.getAssets());
                    listener.onFinished(context.getString(R.string.success_map_file_updated));
                } else {
                    listener.onFinished(context.getString(R.string.error_not_find_map_file));
                }
            }
        });
    }
}
