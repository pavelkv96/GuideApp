package com.grsu.guideapp.fragments.about;

import androidx.annotation.StringRes;

public class AboutItem {

    private int title;
    private int action;

    AboutItem(@StringRes int title, @StringRes int action) {
        this.title = title;
        this.action = action;
    }

    public int getTitle() {
        return title;
    }

    public int getAction() {
        return action;
    }
}
