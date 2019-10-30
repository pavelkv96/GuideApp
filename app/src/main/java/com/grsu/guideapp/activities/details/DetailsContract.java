package com.grsu.guideapp.activities.details;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.models.DtoDetail;

public interface DetailsContract {

    interface DetailView extends BaseView {

        void insertData(DtoDetail detail);
    }

    interface DetailPresenter extends BasePresenter<DetailView> {

        void getDetail(int id, String locale);
    }

    interface DetailInteractor {

        void getDetail(OnSuccessListener<DtoDetail> listener, int id, String locale);
    }
}
