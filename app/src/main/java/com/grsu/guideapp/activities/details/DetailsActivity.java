package com.grsu.guideapp.activities.details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.details.DetailsContract.DetailsView;
import com.grsu.guideapp.base.BaseActivity;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.models.InfoAboutPoi;
import com.grsu.guideapp.project_settings.Constants;

public class DetailsActivity extends BaseActivity<DetailsPresenter> implements DetailsView {

    @BindView(R.id.btn_activity_details_id_point)
    TextView textView;

    @BindView(R.id.btn_activity_details_type)
    TextView textView1;
    @BindView(R.id.btn_activity_details_name_locale)
    TextView textView2;
    @BindView(R.id.btn_activity_details_shortDescriptionPoint)
    TextView textView3;
    @BindView(R.id.btn_activity_details_audioReference)
    TextView textView4;
    @BindView(R.id.btn_activity_details_link)
    TextView textView5;


    @BindView(R.id.iv_activity_details_content)
    ImageView iv_activity_details_content;

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

    public static Intent newIntent(Context context, String idPoint) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_ID_POINT, idPoint);
        return new Intent(context, DetailsActivity.class).putExtras(bundle);
    }

    @Override
    public void showImage(Bitmap image) {
        iv_activity_details_content.setImageBitmap(image);
    }

    @Override
    public void showImage(int resource) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resource);
        iv_activity_details_content.setImageBitmap(bitmap);
    }

    @Override
    public void setContent(InfoAboutPoi content) {
        textView1.setText(content.getType());
        textView2.setText(content.getName_locale());
        textView3.setText(content.getShortDescriptionPoint());
        textView4.setText(content.getAudioReference());
        textView5.setText(content.getLink());
    }
}
