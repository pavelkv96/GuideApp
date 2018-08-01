package com.grsu.guideapp.fragments.map;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;

public interface MapContract {

    interface MapView extends BaseView {

        void onSomethingDone();
    }

    interface DetailsPresenter extends BasePresenter<MapView> {

        void doSomething();
    }
}
