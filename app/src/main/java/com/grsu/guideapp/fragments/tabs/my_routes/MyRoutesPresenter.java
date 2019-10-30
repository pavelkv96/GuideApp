package com.grsu.guideapp.fragments.tabs.my_routes;

import com.grsu.guideapp.App;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.fragments.tabs.my_routes.MyRoutesContract.MyRoutesView;
import com.grsu.guideapp.models.Route;
import java.util.List;

class MyRoutesPresenter extends BasePresenterImpl<MyRoutesView>
        implements MyRoutesContract.MyRoutesPresenter, OnSuccessListener<List<Route>> {

    private MyRoutesInteractor mInteractor;

    MyRoutesPresenter(MyRoutesInteractor interactor) {
        mInteractor = interactor;
    }


    @Override
    public void getListRoute(String locale) {
        mInteractor.getListRoute(this, locale);
    }

    @Override
    public void onSuccess(final List<Route> routes) {
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                if (mView != null) {
                    mView.updateList(routes);
                }
            }
        });
    }

    @Override
    public void onFailure(final Throwable throwable) {
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                if (mView != null) {
                    mView.showToast(throwable.getMessage());
                }
            }
        });
    }
}
