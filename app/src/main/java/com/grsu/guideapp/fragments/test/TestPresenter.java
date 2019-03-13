package com.grsu.guideapp.fragments.test;

import com.grsu.guideapp.fragments.map_preview.MapPreviewPresenter;
import com.grsu.guideapp.fragments.test.TestContract.TestViews;

public class TestPresenter extends MapPreviewPresenter implements TestContract.TestPresenter {

    public TestPresenter(TestViews mapViews, TestInteractor mapInteractor) {
        super(mapViews, mapInteractor);
    }
}
