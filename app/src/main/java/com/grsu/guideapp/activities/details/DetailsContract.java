package com.grsu.guideapp.activities.details;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.models.InformationAboutPoi;

public interface DetailsContract {

    interface DetailsView extends BaseView {

        void onSomethingDone();
    }

    interface DetailsPresenter extends BasePresenter<DetailsView> {

        void getById(String idPoint);
    }

    interface DetailsInteractor {

        interface OnFinishedListener {

            void onFinished(InformationAboutPoi poi);
        }

        void getInfoById(OnFinishedListener listener, String id);
    }
}
