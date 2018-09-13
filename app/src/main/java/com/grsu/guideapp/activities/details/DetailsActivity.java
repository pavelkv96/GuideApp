package com.grsu.guideapp.activities.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;
import butterknife.BindView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.details.DetailsContract.DetailsView;
import com.grsu.guideapp.base.BaseActivity;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.project_settings.Constants;

public class DetailsActivity extends BaseActivity<DetailsPresenter> implements DetailsView {

    @BindView(R.id.btn_activity_details_id_point)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String s = getIntent().getStringExtra(Constants.KEY_ID_POINT);

        textView.setText(s);

        mPresenter.getById(s);
    }

    @NonNull
    @Override
    protected DetailsPresenter getPresenterInstance() {
        return new DetailsPresenter(this, new DetailsInteractor(new DatabaseHelper(this)));
    }

    @Override
    public void onSomethingDone() {
        //startActivity(NavigationDrawerActivity.newIntent(this));
    }

    public static Intent newIntent(Context context, String idPoint) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_ID_POINT, idPoint);
        return new Intent(context, DetailsActivity.class).putExtras(bundle);
    }
}
