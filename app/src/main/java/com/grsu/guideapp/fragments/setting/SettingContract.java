package com.grsu.guideapp.fragments.setting;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import java.io.File;

public interface SettingContract {

    interface SettingView extends BaseView {

    }

    interface SettingPresenter extends BasePresenter<SettingView> {

        void deleteMapFile(File path);
    }

    interface SettingInteractor {
        void deleteMapFile(OnFinishedListener<String> listener, File path);
    }
}
