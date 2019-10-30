package com.grsu.guideapp.fragments.categories;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.models.DtoType;
import com.grsu.guideapp.models.Route;
import java.util.List;

public interface CategoriesContract extends OnSuccessListener<List<Route>> {

    interface CategoriesViews extends BaseView {

        void updateList(List<DtoType> types);

        void emptyData();
    }

    interface CategoriesPresenter extends BasePresenter<CategoriesViews> {

        void getAllTypes(String loc);
    }

    interface CategoriesInteractor {

        void getAllTypes(final OnSuccessListener<List<DtoType>> listener, final String loc);
    }
}
