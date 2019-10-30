package com.grsu.guideapp.fragments.about;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.grsu.guideapp.BuildConfig;
import com.grsu.guideapp.R;
import com.grsu.guideapp.adapters.AboutAdapter;
import com.grsu.guideapp.base.listeners.ItemClickListener;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import java.util.ArrayList;
import java.util.List;

public class AboutFragment extends Fragment implements ItemClickListener {

    @BindView(R.id.fragment_about_rv)
    RecyclerView fragment_about_rv;

    @BindView(R.id.fragment_about_version)
    TextView fragment_about_version;

    private NavigationDrawerActivity parentActivity;
    private Unbinder mUnBinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        mUnBinder = ButterKnife.bind(this, view);

        String text = getString(R.string.title_about_app, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
        fragment_about_version.setText(text);
        parentActivity = (NavigationDrawerActivity) getActivity();
        fragment_about_rv.setHasFixedSize(true);
        fragment_about_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        fragment_about_rv.setAdapter(new AboutAdapter(getData(), this));
        parentActivity.setTitleToolbar(getString(R.string.about_fragment));
        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            /*case 0:
                openURL(R.string.nav_header_subtitle);
                break;*/
            case 0:
                composeEmail();
                break;
            case 1:
                rateMe();
                break;
        }
    }

    private void composeEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_address_iac)});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject_feedback));
        if (intent.resolveActivity(parentActivity.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void rateMe() {
        try {
            Uri uri = Uri.parse("market://details?id=" + parentActivity.getPackageName());
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (ActivityNotFoundException e) {
            String startUri = "http://play.google.com/store/apps/details?id=";
            Uri uri = Uri.parse(startUri + parentActivity.getPackageName());
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

    public void openURL(int urlId) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(urlId))));
    }

    public List<AboutItem> getData() {
        List<AboutItem> items = new ArrayList<>();
        /*items.add(new AboutItem(R.string.nav_header_title, R.string.action_about_openURL));*/
        items.add(new AboutItem(R.string.title_about_contact_developer,
                R.string.action_about_contact_developer));
        items.add(new AboutItem(R.string.title_about_rate_app, R.string.action_about_rate_app));
        return items;
    }

    @Override
    public void onDestroyView() {
        mUnBinder.unbind();
        super.onDestroyView();
    }
}