package com.grsu.guideapp.fragments.setting;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import butterknife.OnClick;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseFragment;

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


    @OnClick(R.id.btn_see)
    public void buttonClick(View view) {
        Log.e(TAG, "buttonClick: ");
        Snackbar.make(view, "ButtonClick", LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
