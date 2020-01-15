package com.grsu.guideapp.fragments.route_preview;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import androidx.annotation.StringRes;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.grsu.guideapp.App;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.base.listeners.OnLoadRoute;
import com.grsu.guideapp.base.listeners.OnProgressListener;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.fragments.route_preview.RoutePreviewContract.RouteInteractor;
import com.grsu.guideapp.fragments.route_preview.RoutePreviewContract.RoutePresenter;
import com.grsu.guideapp.fragments.route_preview.RoutePreviewContract.RouteViews;
import com.grsu.guideapp.models.DtoRoute;

import timber.log.Timber;

class RoutePreviewPresenter extends BasePresenterImpl<RouteViews>
        implements RoutePresenter, OnLoadRoute<Integer>, OnShowListener {

    private ProgressDialog mProgressDialog;
    private RouteInteractor mInteractor;

    RoutePreviewPresenter(RouteInteractor pInteractor) {
        mInteractor = pInteractor;
    }

    private static final String TAG = RoutePreviewPresenter.class.getSimpleName();

    @Override
    public void getRouteById(int id, String locale) {
        mInteractor.getRouteById(new OnSuccessListener<DtoRoute>() {
            @Override
            public void onSuccess(final DtoRoute route) {
                App.getThread().mainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mView != null) {
                            mView.setData(route);
                        }
                    }
                });
            }

            @Override
            public void onFailure(final Throwable throwable) {
                App.getThread().mainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mView != null) {
                            mView.closeFragment();
                            mView.showToast(throwable.getMessage());
                        }
                    }
                });
            }
        }, id, locale);
    }

    @Override
    public void downloadRoute(int id) {
        onStartLoad(id);
    }

    @Override
    public void updateRoute(final int id) {
        Context context = getView().getContentView().getContext();
        mView.showProgress("", context.getString(R.string.updating_route));
        mInteractor.updateRoute(new OnProgressListener<String>() {
            @Override
            public void onProgress(final int progress) {
                App.getThread().mainThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.changeProgress(progress);
                    }
                });
            }

            @Override
            public void onSuccess(String s) {
                Timber.e("onSuccess: ");
                App.getThread().mainThread(new Runnable() {
                    @Override
                    public void run() {
                        Context context = mView.getContentView().getContext();
                        String locale = context.getString(R.string.locale);
                        getRouteById(id, locale);
                        mView.hideProgress();
                    }
                });
            }

            @Override
            public void onFailure(final Throwable throwable) {
                App.getThread().mainThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.hideProgress();
                        mView.showToast(R.string.error_message_update);
                        Timber.e(throwable, "onFailure: %s", throwable.getMessage());
                    }
                });
            }}, id);
    }

    @Override
    public void openPreviewRoute(int id) {
        mView.openFragment(id);
    }

    @Override
    public void openRoute(Bundle bundle) {
        mView.openFragment(bundle);
    }

    private void showProgress(int title, String message) {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = new ProgressDialog(mView.getContentView().getContext());
            mProgressDialog.setTitle(title);
            String cancel = mView.getContentView().getContext().getString(R.string.cancel);
            int negative = ProgressDialog.BUTTON_NEGATIVE;
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setButton(negative, cancel, (DialogInterface.OnClickListener) null);

            mProgressDialog.setOnShowListener(this);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

    }

    private void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onStartLoad(int id_route) {
        if (mView != null) {
            mInteractor.saveRoute(this, id_route);
            int title = R.string.load_route;
            Context context = mView.getContentView().getContext();
            String message = context.getString(R.string.wait_please);
            showProgress(title, message);
        }
    }

    @Override
    public void onSuccess(@StringRes final Integer res) {
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                hideProgress();
                if (mView != null) {
                    mView.showToast(res);
                    mView.visibleDownloadRouteButton(false);
                    mView.visibleStartRouteButton(true);
                    mView.visibleUpdateRouteButton(false);
                    mView.visiblePreviewRouteButton(false);
                }
            }
        });
    }

    @Override
    public void onFailure(final Throwable throwable) {
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                hideProgress();
                if (mView != null) {
                    String s = mView.getContentView().getContext().getString(R.string.retry_again_later);
                    mView.showToast(throwable.getMessage() + "\n" + s);
                }
            }
        });
    }

    @Override
    public void onCancelLoad() {
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                hideProgress();
                if (mView != null) {
                    mView.showToast(android.R.string.cancel);
                }
            }
        });
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        final ProgressDialog dialog = (ProgressDialog) dialogInterface;
        final Button button = dialog.getButton(ProgressDialog.BUTTON_NEGATIVE);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage("Сanceled download...");
                button.setVisibility(View.GONE);
                mInteractor.setCancel(true);
            }
        });
    }

    @Override
    public void onProgress(final int progress) {
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.setProgress(progress);
                }
            }
        });
    }
}
