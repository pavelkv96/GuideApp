package com.grsu.guideapp.fragments.test;

import com.grsu.guideapp.fragments.map_preview_v1.MapPreviewContract.MapPreviewInteractor;
import com.grsu.guideapp.fragments.map_preview_v1.MapPreviewContract.MapPreviewPresenter;
import com.grsu.guideapp.fragments.map_preview_v1.MapPreviewContract.MapPreviewViews;

public interface TestContract {

    interface TestViews extends MapPreviewViews {

    }

    interface TestPresenter extends MapPreviewPresenter {

    }

    interface TestInteractor extends MapPreviewInteractor {

    }
}
