package com.grsu.guideapp.fragments.tabs.list_routes;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.models.Route;
import java.util.List;

public interface ListRoutesContract {

    interface ListRoutesViews extends BaseView {

    }

    interface ListRoutesPresenter extends BasePresenter<ListRoutesViews> {

    }
}
