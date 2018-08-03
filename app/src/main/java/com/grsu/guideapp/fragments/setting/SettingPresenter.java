package com.grsu.guideapp.fragments.setting;

import com.grsu.guideapp.activities.details.DetailsContract;
import com.grsu.guideapp.activities.details.DetailsContract.DetailsView;
import com.grsu.guideapp.base.BasePresenterImpl;

public class SettingPresenter extends BasePresenterImpl<DetailsView>
        implements DetailsContract.DetailsPresenter {

    public void doSomething() {
        mView.onSomethingDone();
    }

}