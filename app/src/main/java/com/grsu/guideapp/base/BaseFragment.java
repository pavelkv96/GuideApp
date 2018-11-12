package com.grsu.guideapp.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import java.util.Arrays;

public abstract class BaseFragment<P extends BasePresenter, A extends FragmentActivity>
        extends Fragment implements BaseView {

    private Unbinder mUnBinder;
    private SharedPreferences preferences;

    protected A getActivity;

    protected abstract @NonNull
    P getPresenterInstance();

    protected abstract @LayoutRes
    int getLayout();

    private ProgressDialog mProgressDialog = null;

    protected View rootView;

    protected P mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayout(), container, false);
        getActivity = (A) getActivity();
        mPresenter = getPresenterInstance();
        mPresenter.attachView(this);

        mUnBinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        mPresenter.detachView();
        mUnBinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        preferences = null;
        super.onDetach();
    }

    protected <T> void save(String key, T data) {
        if (data instanceof long[]) {
            preferences.edit().putString(key, Arrays.toString((long[]) data)).apply();
        }
    }

    protected <T> T read(String key, Class<T> clazz) {

        if (clazz.isAssignableFrom(Long[].class)) {
            String s = preferences.getString(key, null);
            if (s != null) {
                String[] split = s.substring(1, s.length() - 1).split(", ");
                long[] array = new long[split.length];
                for (int i = 0; i < split.length; i++) {
                    array[i] = Long.parseLong(split[i]);
                }
                return (T) array;
            }
        }

        return null;
    }

    protected void remove(String key) {
        preferences.edit().remove(key).apply();
    }

    //BaseView
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
    public View getContentView() {
        return rootView;
    }
}
