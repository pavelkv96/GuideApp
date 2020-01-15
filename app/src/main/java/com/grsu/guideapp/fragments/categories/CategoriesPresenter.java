package com.grsu.guideapp.fragments.categories;

import com.grsu.guideapp.App;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.fragments.categories.CategoriesContract.CategoriesViews;
import com.grsu.guideapp.models.DtoType;
import java.util.List;

public class CategoriesPresenter extends BasePresenterImpl<CategoriesViews> implements
        CategoriesContract.CategoriesPresenter, OnSuccessListener<List<DtoType>> {

    private CategoriesViews mViews;
    private CategoriesInteractor mInteractor;

    CategoriesPresenter(CategoriesViews pViews, CategoriesInteractor pInteractor) {
        this.mViews = pViews;
        this.mInteractor = pInteractor;
    }

    @Override
    public void getAllTypes(String loc) {
        mInteractor.getAllTypes(this, loc);
    }

    @Override
    public void onSuccess(final List<DtoType> types) {
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                if (types != null && !types.isEmpty()) {
                    mViews.updateList(types);
                } else {
                    mView.emptyData();
                }
            }
        });
    }

    @Override
    public void onFailure(final Throwable throwable) {
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                mViews.emptyData();
            }
        });
    }
}
