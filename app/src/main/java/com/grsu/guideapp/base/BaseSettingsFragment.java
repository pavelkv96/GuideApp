package com.grsu.guideapp.base;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.utils.MessageViewer.Toasts;

public abstract class BaseSettingsFragment<P extends BasePresenter, A extends FragmentActivity>
        extends PreferenceFragmentCompat implements BaseView, OnSharedPreferenceChangeListener {

    protected SharedPreferences preferences;
    protected A getActivity;

    protected abstract @NonNull
    P getPresenterInstance();

    protected abstract String getTitle();

    private ProgressDialog mProgressDialog = null;

    private View rootView;

    protected P mPresenter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);//inflater.inflate(getLayout(), container, false);
        getActivity = (A) getActivity();
        mPresenter = getPresenterInstance();
        mPresenter.attachView(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity);
        preferences.registerOnSharedPreferenceChangeListener(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        mPresenter.detachView();
        preferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroyView();
    }

    @Override
    public void showProgress(String title, String message) {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = new ProgressDialog(getActivity());
            if (title != null) {
                mProgressDialog.setTitle(title);
            }
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void changeProgress(int progress) {

    }

    @Override
    public void showToast(String message) {
        Toasts.makeL(getActivity, message);
    }

    @Override
    public void showToast(int message) {
        Toasts.makeL(getActivity, message);
    }

    @Override
    public View getContentView() {
        return rootView;
    }
}
