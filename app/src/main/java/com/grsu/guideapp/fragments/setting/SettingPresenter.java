package com.grsu.guideapp.fragments.setting;

import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.fragments.setting.SettingContract.SettingView;

public class SettingPresenter extends BasePresenterImpl<SettingView>
        implements SettingContract.SettingPresenter {

    public void doSomething() {
        mView.onSomethingDone();
    }

}