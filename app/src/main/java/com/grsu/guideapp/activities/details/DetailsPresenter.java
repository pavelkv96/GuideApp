package com.grsu.guideapp.activities.details;

import com.grsu.guideapp.activities.details.DetailsContract.DetailsInteractor.OnFinishedListener;
import com.grsu.guideapp.activities.details.DetailsContract.DetailsView;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.models.InformationAboutPoi;
import com.grsu.guideapp.utils.MessageViewer.Logs;

public class DetailsPresenter extends BasePresenterImpl<DetailsView> implements OnFinishedListener,
        DetailsContract.DetailsPresenter {

    private static final String TAG = DetailsPresenter.class.getSimpleName();

    private DetailsView detailsView;
    private DetailsInteractor detailsInteractor;

    public DetailsPresenter(DetailsView detailsView, DetailsInteractor detailsInteractor) {
        this.detailsView = detailsView;
        this.detailsInteractor = detailsInteractor;
    }

    @Override
    public void getById(String idPoint) {
        detailsInteractor.getInfoById(this, idPoint);
    }

    @Override
    public void onFinished(InformationAboutPoi poi) {
        Logs.e(TAG, poi + "");
    }
}