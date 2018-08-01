package com.grsu.guideapp.activities.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.details.DetailsContract.DetailsView;
import com.grsu.guideapp.base.BaseActivity;

public class DetailsActivity extends BaseActivity<DetailsPresenter> implements DetailsView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    }

    @NonNull
    @Override
    protected DetailsPresenter getPresenterInstance() {
        return new DetailsPresenter();
    }

    //@OnClick(R.id.btnGo)
    public void onBtnGoClick() {
        mPresenter.doSomething();
    }

    @Override
    public void onSomethingDone() {
        //startActivity(NavigationDrawerActivity.newIntent(this));
    }
}
