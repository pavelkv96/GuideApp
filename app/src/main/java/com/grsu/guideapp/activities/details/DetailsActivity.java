package com.grsu.guideapp.activities.details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;

import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.details.DetailsContract.DetailView;
import com.grsu.guideapp.base.BaseActivity;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.models.DtoDetail;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.project_settings.Constants.Language;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends BaseActivity<DetailsPresenter> implements DetailView {

//    @BindView(R.id.toolbar)
    Toolbar mToolbar;

//    @BindView(R.id.tv_fragment_details_name)
    TextView nameTextView;
//    @BindView(R.id.tv_fragment_details_description)
    TextView descriptionTextView;
//    @BindView(R.id.tv_fragment_details_address)
    TextView addressTextView;
//    @BindView(R.id.tv_fragment_details_email)
    TextView emailTextView;
//    @BindView(R.id.tv_fragment_details_link)
    TextView linkTextView;
//    @BindView(R.id.tv_fragment_details_phone)
    TextView phoneTextView;

//    @BindView(R.id.iv_fragment_details_image)
    ImageView image;

    //DownloadManager mManager;

    @NonNull
    @Override
    protected DetailsPresenter getPresenterInstance() {
        return new DetailsPresenter(this, new DetailsInteractor(new Test(this)));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_details);

        setSupportActionBar(mToolbar);

        int id = getIntent().getIntExtra(Constants.KEY_ID_OBJECT, -1);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
       /* mManager = ContextCompat.getSystemService(this, DownloadManager.class);

        Uri uri = Uri.parse("https://upload.wikimedia.org/wikipedia/commons/6/6c/Grieg_Lyric_Pieces_Kobold.ogg");

        Request request = new Request(uri);
        request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(this, "content/audio/", "audio-2");
        mManager.enqueue(request);*/

        mPresenter.getDetail(id, getString(R.string.locale));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void insertData(final DtoDetail detail) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(detail.getNameLocale().getName());
        }

        nameTextView.setText(detail.getNameLocale().getName());

        Spanned spanned = HtmlCompat.fromHtml(detail.getNameLocale().getDescription(), 0);
        descriptionTextView.setText(spanned);

        if (detail.getAddress().length > 2) {
            if (getString(R.string.locale).equals(Language.ru.name())) {
                if (!detail.getAddress()[0].isEmpty()) {
                    addressTextView.setVisibility(View.VISIBLE);
                    String text = getString(R.string.address) + ":\n" + detail.getAddress()[0];
                    addressTextView.setText(text);
                }
            } else {
                if (!detail.getAddress()[1].isEmpty()) {
                    addressTextView.setVisibility(View.VISIBLE);
                    String text = getString(R.string.address) + ":\n" + detail.getAddress()[1];
                    addressTextView.setText(text);
                }
            }
        }

        if (!detail.getEmail().isEmpty()) {
            emailTextView.setVisibility(View.VISIBLE);
            String email = getString(R.string.email) + ":\n" + detail.getEmail();
            emailTextView.setText(email);
        }

        if (!detail.getPhone().isEmpty()) {
            phoneTextView.setVisibility(View.VISIBLE);
            String phone = getString(R.string.phone) + ":\n" + detail.getPhone();
            phoneTextView.setText(phone);
        }

        if (!detail.getLink().isEmpty()) {
            linkTextView.setVisibility(View.VISIBLE);
            String link = getString(R.string.link) + ":\n" + detail.getLink();
            linkTextView.setText(link);
        }
        Picasso.get().load(detail.getPhotoReference()).error(R.drawable.ic_launcher_background).into(image);

        String path = Uri.parse("android.resource://com.grsu.guideapp/" + R.raw.audio).getPath();
        Intent service = new Intent(this, DetailsPlayerService.class);
        service.setAction(Constants.KEY_RECORD);
        service.putExtra(Constants.KEY_NAME_PLACE_RECORD, detail.getNameLocale().getName());
        service.putExtra(Constants.KEY_NAME_RECORD, path);
        startService(service);


        /*App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                String s = "https://vk.com/mp3/audio_api_unavailable.mp3";
                SaveAdapter.saveAudio(s);
                Intent service = new Intent(DetailsActivity.this, DetailsPlayerService.class);
                service.setAction(Constants.KEY_RECORD);
                service.putExtra(Constants.KEY_NAME_RECORD, CryptoUtils.hash(s));
                service.putExtra(Constants.KEY_NAME_PLACE_RECORD, detail.getNameLocale().getName());
                startService(service);
            }
        });*/
    }

    @Override
    protected void onStop() {
        stopService(new Intent(this, DetailsPlayerService.class));
        super.onStop();
    }

    public static void newInstance(Context context, int data) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(Constants.KEY_ID_OBJECT, (Integer) data);
        context.startActivity(intent);
    }
}