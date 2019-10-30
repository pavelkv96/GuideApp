package com.grsu.guideapp.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.grsu.guideapp.App;
import com.grsu.guideapp.utils.MessageViewer.Toasts;

public abstract class BaseActivity<P extends BasePresenter>
        extends AppCompatActivity
        implements BaseView {

    private Unbinder mUnBinder;

    private ProgressDialog mProgressDialog = null;
    protected SharedPreferences preferences;

    protected @NonNull
    abstract P getPresenterInstance();

    protected P mPresenter;

    @Override
    protected void attachBaseContext(Context newBase) {
        preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        super.attachBaseContext(App.getInstance().setLocale(preferences, newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenterInstance();
        mPresenter.attachView(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mUnBinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDestroy();
    }

    //BaseView
    @Override
    public void showProgress(String title, String message) {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = new ProgressDialog(this);
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
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            if (!mProgressDialog.isIndeterminate()) {
                mProgressDialog.setIndeterminate(false);
            }
            mProgressDialog.setProgress(progress);
        }
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showToast(String message) {
        Toasts.makeL(this, message);
    }

    @Override
    public void showToast(@StringRes int message) {
        Toasts.makeL(this, message);
    }

    @Override
    public View getContentView() {
        return getWindow().getDecorView();
    }
}
