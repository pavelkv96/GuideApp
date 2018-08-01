package com.grsu.guideapp.fragments.setting;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;

public interface SettingContract {

    interface SettingView extends BaseView {

        void onSomethingDone();
    }

    interface DetailsPresenter extends BasePresenter<SettingView> {

        void doSomething();
    }
}
