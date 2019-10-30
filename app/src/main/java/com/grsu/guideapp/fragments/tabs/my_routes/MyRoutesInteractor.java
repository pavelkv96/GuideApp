package com.grsu.guideapp.fragments.tabs.my_routes;

import com.grsu.guideapp.App;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.models.Route;
import java.util.List;

class MyRoutesInteractor implements MyRoutesContract.MyRoutesInteractor {

    private Test mHelper;

    MyRoutesInteractor(Test helper) {
        mHelper = helper;
    }

    @Override
    public void getListRoute(final OnSuccessListener<List<Route>> listener, final String locale) {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onSuccess(mHelper.getListRoutes(locale, false));
                }catch (Exception e){
                    listener.onFailure(e);
                }
            }
        });
    }
}
