package com.grsu.guideapp.base.listeners;

public interface OnProgressListener<Task> extends  OnSuccessListener<Task>{

    void onProgress(int progress);
}
