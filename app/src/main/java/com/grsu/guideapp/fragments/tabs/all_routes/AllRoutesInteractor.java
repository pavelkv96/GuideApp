package com.grsu.guideapp.fragments.tabs.all_routes;

import android.os.Handler;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.models.Route;
import java.util.List;

public class AllRoutesInteractor implements AllRoutesContract.AllRoutesInteractor {

    private Test helper;

    AllRoutesInteractor(Test databaseHelper) {
        this.helper = databaseHelper;
    }

    @Override
    public void getListAllRoutes(final OnSuccessListener<List<Route>> listener, final String loc) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onSuccess(helper.getListRoutes(loc, true));
                } catch (Exception e) {
                    listener.onFailure(e);
                }
            }
        });
    }
}
