package com.grsu.guideapp.activities.details;

import com.grsu.guideapp.App;
import com.grsu.guideapp.activities.details.DetailsContract.DetailInteractor;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.models.DtoDetail;

public class DetailsInteractor implements DetailInteractor {

    private Test mHelper;

    DetailsInteractor(Test pHelper) {
        mHelper = pHelper;
    }

    @Override
    public void getDetail(final OnSuccessListener<DtoDetail> listener, final int id,
            final String locale) {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onSuccess(mHelper.getInfoById(id, locale));
                } catch (Exception e) {
                    listener.onFailure(e);
                }
            }
        });
    }
}
