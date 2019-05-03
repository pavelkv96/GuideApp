package com.grsu.guideapp.fragments.details;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.fragments.details.DetailsContract.DetailsView;
import com.grsu.guideapp.models.InfoAboutPoi;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.CryptoUtils;
import com.squareup.picasso.Picasso;
import java.io.File;

public class DetailsFragment extends BaseFragment<DetailsPresenter, RouteActivity>
        implements DetailsView {

    private Intent intent;

    @BindView(R.id.tv_fragment_details_last_update)
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

    @BindView(R.id.btn_fragment_details_start)
    Button button;

    @NonNull
    @Override
    protected DetailsPresenter getPresenterInstance() {
        return new DetailsPresenter(this, new DetailsInteractor(new Test(getContext())));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_details;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.details_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity.setTitleToolbar(getTitle());
        Bundle bundle = getArguments();

        if (bundle != null) {
            String idPoint = bundle.getString(Constants.KEY_ID_POINT, "");
            if (!idPoint.equals("")) {
                intent = new Intent(getActivity, DetailsPlayerService.class);

                textView.setText(idPoint);

                mPresenter.getById(idPoint, getString(R.string.locale));
            } else {
                showToast("Error get data");
                getActivity.getSupportFragmentManager().popBackStack();
            }
        } else {
            showToast("Error get data");
        }
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
        getActivity.setTitleToolbar(content.getNameLocale().getShortName());

        textView.setText(content.getLast_update());
        textView1.setText(content.getType());
        textView2.setText(content.getNameLocale().getFullName());
        textView3.setText(content.getNameLocale().getFullDescription());
        textView4.setText(content.getAudioReference());
        textView5.setText(content.getLink());
        String photo = CryptoUtils.hash(content.getPhotoReference());
        File file = new File(Settings.CONTENT, photo);
        Picasso.get().load(file).into(iv_fragment_details_content);
    }

    @Override
    public void returnedIntent(File file) {
        intent.putExtra(Constants.KEY_NAME_RECORD, file);
    }

    @Override
    public void hideButton() {
        button.setVisibility(View.GONE);
    }

    @Override
    public void showButton() {
        button.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_fragment_details_start)
    public void startService(View view) {
        if (getActivity != null) {
            getActivity.stopService(new Intent(getActivity, DetailsPlayerService.class));
            intent.putExtra(Constants.KEY_NAME_PLACE_RECORD, textView.getText().toString());
            getActivity.startService(intent.setAction(Constants.KEY_RECORD));
        }
    }

    public static DetailsFragment newInstance(String idPoint) {
        Bundle args = new Bundle();
        args.putString(Constants.KEY_ID_POINT, idPoint);
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        getActivity.stopService(new Intent(getActivity, DetailsPlayerService.class));
        super.onDestroyView();
    }
}
