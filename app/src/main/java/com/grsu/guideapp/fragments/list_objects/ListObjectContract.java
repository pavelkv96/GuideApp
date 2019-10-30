package com.grsu.guideapp.fragments.list_objects;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.models.DtoObject;
import java.util.List;

public interface ListObjectContract {

    interface ObjectView extends BaseView {

        void updateAdapter(List<DtoObject> objects);

        void emptyData();
    }

    interface ObjectPresenter extends BasePresenter<ObjectView> {

        void getObject(String locale, int type);
    }

    interface ObjectInteractor {

        void getObjects(OnSuccessListener<List<DtoObject>> listener, String locale, int type);
    }
}
