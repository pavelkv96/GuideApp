package com.grsu.guideapp.fragments.setting;

import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.fragments.setting.SettingContract.SettingView;
import java.io.File;

public class SettingPresenter extends BasePresenterImpl<SettingView>
        implements SettingContract.SettingPresenter, OnFinishedListener<String> {
    private SettingView settingView;
    private SettingInteractor settingInteractor;

    public SettingPresenter(SettingView view, SettingInteractor interactor) {
        settingView = view;
        settingInteractor = interactor;
    }

    @Override
    public void deleteMapFile(File path) {
        settingInteractor.deleteMapFile(this, path);
    }

    @Override
    public void onFinished(String message) {
        settingView.showToast(message);
    }
}