package com.grsu.guideapp.base.listeners;

public interface OnSuccessListener<Task> {

    void onSuccess(Task task);

    void onFailure(Throwable throwable);

}