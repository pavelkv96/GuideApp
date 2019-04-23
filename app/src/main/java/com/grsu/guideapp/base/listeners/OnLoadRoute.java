package com.grsu.guideapp.base.listeners;

public interface OnLoadRoute<Task> {

    void onStartLoad(int i);

    void onSuccess(Task task);

    void onFailure(Throwable throwable);

    void onCancelLoad();
}
