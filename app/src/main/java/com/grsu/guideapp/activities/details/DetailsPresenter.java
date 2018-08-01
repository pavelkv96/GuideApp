package com.grsu.guideapp.activities.details;

import com.grsu.guideapp.activities.details.DetailsContract.DetailsView;
import com.grsu.guideapp.base.BasePresenterImpl;

public class DetailsPresenter extends BasePresenterImpl<DetailsView>
        implements DetailsContract.DetailsPresenter {

    @Override
    public void doSomething() {
        mView.onSomethingDone();
    }

}