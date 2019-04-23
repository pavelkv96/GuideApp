package com.grsu.guideapp.fragments.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.adapters.AboutAdapter;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import java.util.ArrayList;
import java.util.List;

public class AboutFragment extends ListFragment {

    private NavigationDrawerActivity parentActivity;
    private final int openURLRow = 0;
    private final int contactDevRow = 1;
    private final int rateAppRow = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        parentActivity = (NavigationDrawerActivity) getActivity();
        parentActivity.setTitleToolbar(getString(R.string.about_fragment));

        List<AboutItem> items = getData();
        setListAdapter(new AboutAdapter(parentActivity, R.layout.title_descr_list_item, items));

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        switch (position) {
            case openURLRow:
                openURL(R.string.nav_header_subtitle);
                break;
            case contactDevRow:
                composeEmail(new String[]{getString(R.string.email_address_iac)},
                        getString(R.string.email_subject_feedback));
                break;
            case rateAppRow:
                rateMe();
                break;
        }
    }

    private void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(parentActivity.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void rateMe() {
        try {
            Uri uri = Uri.parse("market://details?id=" + parentActivity.getPackageName());
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (android.content.ActivityNotFoundException e) {
            String startUri = "http://play.google.com/store/apps/details?id=";
            Uri uri = Uri.parse(startUri + parentActivity.getPackageName());
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

    public void openURL(int urlId) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(urlId))));
    }

    public List<AboutItem> getData(){
        List<AboutItem> items = new ArrayList<>();
        items.add(new AboutItem(getString(R.string.nav_header_title), getString(R.string.action_about_openURL)));
        items.add(new AboutItem(getString(R.string.title_about_contact_developer),
                getString(R.string.action_about_contact_developer)));
        items.add(new AboutItem(getString(R.string.title_about_rate_app),
                getString(R.string.action_about_rate_app)));
        return items;
    }
}