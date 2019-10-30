package com.grsu.guideapp.fragments.list_objects;

import com.grsu.guideapp.App;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.fragments.list_objects.ListObjectContract.ObjectInteractor;
import com.grsu.guideapp.models.DtoObject;
import java.util.List;

public class ListObjectInteractor implements ObjectInteractor {

    private Test mHelper;

    public ListObjectInteractor(Test pHelper) {
        mHelper = pHelper;
    }

    @Override
    public void getObjects(final OnSuccessListener<List<DtoObject>> listener, final String locale,
            final int type) {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onSuccess(mHelper.getAllObjectFromType(locale, type));
                } catch (Exception e) {
                    listener.onFailure(e);
                }
            }
        });
    }
}
