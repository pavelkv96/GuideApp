package com.grsu.guideapp.fragments.test;

import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.fragments.map_preview.MapPreviewInteractor;

public class TestInteractor extends MapPreviewInteractor implements TestContract.TestInteractor{

    public TestInteractor(DatabaseHelper pDbHelper) {
        super(pDbHelper);
    }
}