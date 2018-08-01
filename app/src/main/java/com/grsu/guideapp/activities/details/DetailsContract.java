package com.grsu.guideapp.activities.details;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;

public interface DetailsContract {

    interface DetailsView extends BaseView {

        void onSomethingDone();
    }

    interface DetailsPresenter extends BasePresenter<DetailsView> {

        void doSomething();
    }
}
