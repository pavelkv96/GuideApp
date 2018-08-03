package com.grsu.guideapp.fragments.map;

import com.grsu.guideapp.activities.details.DetailsContract;
import com.grsu.guideapp.activities.details.DetailsContract.DetailsView;
import com.grsu.guideapp.base.BasePresenterImpl;

public class MapPresenter extends BasePresenterImpl<DetailsView>
        implements DetailsContract.DetailsPresenter {

    public void doSomething() {
        mView.onSomethingDone();
    }

}