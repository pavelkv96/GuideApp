package com.grsu.guideapp.fragments.about;

public class AboutItem {

    private String title;
    private String action;

    AboutItem(String title, String action) {
        this.title = title;
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public String getAction() {
        return action;
    }
}
