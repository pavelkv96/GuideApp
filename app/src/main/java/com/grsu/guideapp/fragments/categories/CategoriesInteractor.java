package com.grsu.guideapp.fragments.categories;

import android.os.Handler;
import com.grsu.guideapp.App;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.models.DtoType;
import java.util.List;

public class CategoriesInteractor implements CategoriesContract.CategoriesInteractor {

    private Test helper;

    CategoriesInteractor(Test databaseHelper) {
        this.helper = databaseHelper;
    }

    @Override
    public void getAllTypes(final OnSuccessListener<List<DtoType>> listener, final String loc) {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onSuccess(helper.getAllType(loc));
                } catch (Exception e) {
                    listener.onFailure(e);
                }
            }
        });
    }
}
