package com.grsu.guideapp.fragments.setting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import butterknife.OnClick;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.utils.MessageViewer.Toasts;

public class SettingsFragment extends BaseFragment<SettingPresenter> {

    private static final String TAG = "SettingsFragment";

    @NonNull
    @Override
    protected SettingPresenter getPresenterInstance() {
        return new SettingPresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_settings;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @OnClick(R.id.btn_snackbar)
    public void buttonClick(View view) {
        Context context = getContext();

        if (context != null) {
            if (DatabaseHelper.deleteDatabase(context)) {
                Toasts.makeS(context, "Database deleted");
            } else {
                Toasts.makeS(context, "Database not found");
            }
        }
    }

}
