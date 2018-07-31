package com.grsu.guideapp;

import com.grsu.guideapp.models.InformationAboutPoint;
import com.grsu.guideapp.models.Lines;
import com.grsu.guideapp.models.ListLines;
import com.grsu.guideapp.models.Photo;
import com.grsu.guideapp.models.Place;
import com.grsu.guideapp.models.Points;
import com.grsu.guideapp.models.RatingRoute;
import com.grsu.guideapp.models.Routes;
import com.grsu.guideapp.models.Users;

public class DbModels {

    public static final Class<?>[] DB_MODELS = new Class[]{
            Users.class,
            Routes.class,
            RatingRoute.class,
            Place.class,
            Lines.class,
            ListLines.class,
            Points.class,
            Photo.class,
            InformationAboutPoint.class
    };
}
