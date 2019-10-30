package com.grsu.guideapp.fragments.list_objects;

import com.grsu.guideapp.App;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.fragments.list_objects.ListObjectContract.ObjectInteractor;
import com.grsu.guideapp.fragments.list_objects.ListObjectContract.ObjectPresenter;
import com.grsu.guideapp.fragments.list_objects.ListObjectContract.ObjectView;
import com.grsu.guideapp.models.DtoObject;
import java.util.List;

public class ListObjectPresenter extends BasePresenterImpl<ObjectView> implements ObjectPresenter,
        OnSuccessListener<List<DtoObject>> {

    private ObjectView mView;
    private ObjectInteractor mInteractor;

    public ListObjectPresenter(ObjectView pView, ObjectInteractor pInteractor) {
        mView = pView;
        mInteractor = pInteractor;
    }

    @Override
    public void getObject(String locale, int type) {
        mInteractor.getObjects(this, locale, type);
    }

    @Override
    public void onSuccess(final List<DtoObject> dtoObjects) {
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                if (dtoObjects != null && !dtoObjects.isEmpty()) {
                    mView.updateAdapter(dtoObjects);
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
                mView.emptyData();
            }
        });
    }
}
