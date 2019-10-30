package com.grsu.guideapp.activities.details;

import com.grsu.guideapp.App;
import com.grsu.guideapp.activities.details.DetailsContract.DetailPresenter;
import com.grsu.guideapp.activities.details.DetailsContract.DetailView;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.models.DtoDetail;

public class DetailsPresenter extends BasePresenterImpl<DetailView> implements DetailPresenter,
        OnSuccessListener<DtoDetail> {

    private DetailView mView;
    private DetailsInteractor mInteractor;


    DetailsPresenter(DetailView pView, DetailsInteractor pInteractor) {
        mView = pView;
        mInteractor = pInteractor;
    }

    @Override
    public void getDetail(int id, String locale) {
        mInteractor.getDetail(this, id, locale);
    }

    @Override
    public void onSuccess(final DtoDetail detail) {
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                mView.insertData(detail);
            }
        });
    }

    @Override
    public void onFailure(final Throwable throwable) {
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                mView.showToast(throwable.getMessage());
            }
        });
    }
}
