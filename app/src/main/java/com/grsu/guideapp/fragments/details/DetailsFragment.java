package com.grsu.guideapp.fragments.details;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.fragments.details.DetailsContract.DetailsView;
import com.grsu.guideapp.models.InfoAboutPoi;
import com.grsu.guideapp.project_settings.Constants;

public class DetailsFragment extends BaseFragment<DetailsPresenter> implements DetailsView {

    @BindView(R.id.tv_fragment_details_id_point)
    TextView textView;

    @BindView(R.id.tv_fragment_details_type)
    TextView textView1;
    @BindView(R.id.tv_fragment_details_name_locale)
    TextView textView2;
    @BindView(R.id.tv_fragment_details_shortDescriptionPoint)
    TextView textView3;
    @BindView(R.id.tv_fragment_details_audioReference)
    TextView textView4;
    @BindView(R.id.tv_fragment_details_link)
    TextView textView5;


    @BindView(R.id.iv_fragment_details_content)
    ImageView iv_fragment_details_content;

    @NonNull
    @Override
    protected DetailsPresenter getPresenterInstance() {
        return new DetailsPresenter(this, new DetailsInteractor(new DatabaseHelper(getContext())));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_details;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String s = getArguments().getString(Constants.KEY_ID_POINT);

        textView.setText(s);

        mPresenter.getById(s);
    }

    public static DetailsFragment newInstance(String idPoint) {

        Bundle args = new Bundle();
        args.putString(Constants.KEY_ID_POINT, idPoint);
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void showImage(Bitmap image) {
        iv_fragment_details_content.setImageBitmap(image);
    }

    @Override
    public void showImage(int resource) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resource);
        iv_fragment_details_content.setImageBitmap(bitmap);
    }

    @Override
    public void setContent(InfoAboutPoi content) {
        textView1.setText(content.getType());
        textView2.setText(content.getName_locale());
        textView3.setText(content.getShortDescriptionPoint());
        textView4.setText(content.getAudioReference());
        textView5.setText(content.getLink());
    }

    @OnClick(R.id.btn_fragment_details_start)
    public void startService(View view) {
        if (getActivity() != null) {
            getActivity().stopService(new Intent(getActivity(), DetailsPlayerService.class));
            getActivity().startService(new Intent(getActivity(), DetailsPlayerService.class)
                    .putExtra(Constants.KEY_RECORD, textView.getText().toString())
                    .setAction(Constants.KEY_RECORD)
            );
        }
    }
}
