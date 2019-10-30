package com.grsu.guideapp.base.listeners;

public interface OnLoadRoute<Task> extends OnProgressListener<Task> {

    void onStartLoad(int i);

    void onCancelLoad();
}
