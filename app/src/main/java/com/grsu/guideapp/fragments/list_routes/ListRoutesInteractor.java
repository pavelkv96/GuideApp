package com.grsu.guideapp.fragments.list_routes;

import android.os.Handler;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.models.Route;
import java.util.List;

public class ListRoutesInteractor implements ListRoutesContract.ListRoutesInteractor {

    DatabaseHelper helper;

    public ListRoutesInteractor(DatabaseHelper databaseHelper) {
        this.helper = databaseHelper;
    }

    @Override
    public void getListAllRoutes(final OnSuccessListener<List<Route>> listener) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onSuccess(helper.getListRoutes());
                } catch (Exception e) {
                    listener.onFailure(e);
                }
            }
        });
    }
}
