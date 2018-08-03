package com.grsu.guideapp;

import com.grsu.guideapp.models.InformationAboutPoint;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.ListLines;
import com.grsu.guideapp.models.Place;
import com.grsu.guideapp.models.Route;

public class DbModels {

    public static final Class<?>[] DB_MODELS = new Class[]{
            Route.class,
            Place.class,
            Line.class,
            ListLines.class,
            InformationAboutPoint.class
    };
}
