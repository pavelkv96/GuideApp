package com.grsu.guideapp.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseChildFragment<P extends BasePresenter, F extends BaseFragment>
        extends Fragment implements BaseView {

    private Unbinder mUnBinder;

    protected F getParent;

    protected abstract @NonNull
    P getPresenterInstance();

    protected abstract @LayoutRes
    int getLayout();

    private ProgressDialog mProgressDialog = null;

    private View rootView;

    protected P mPresenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayout(), container, false);
        getParent = (F) getParentFragment();
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

    //BaseView
    @Override
    public void showProgress(String title, String message) {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = new ProgressDialog(getParent.getActivity);
            if (title != null) {
                mProgressDialog.setTitle(title);
            }
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

    }

    @Override
    public void changeProgress(int progress) {

    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getParent.getActivity, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToast(@StringRes int message) {
        Toast.makeText(getParent.getActivity, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public View getContentView() {
        return rootView;
    }
}
