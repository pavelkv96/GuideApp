package com.grsu.guideapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.grsu.guideapp.fragments.about.AboutItem;
import com.grsu.guideapp.R;
import java.util.ArrayList;
import java.util.List;

public class AboutAdapter extends ArrayAdapter<AboutItem> {

    private List<AboutItem> aboutItems;

    public AboutAdapter(Context context, int resource, List<AboutItem> objects) {
        super(context, resource, objects);
        aboutItems = new ArrayList<>(objects);
    }

    private static class ViewHolder {

        private TextView title;
        private TextView action;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.title_descr_list_item, parent, false);

            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.title);
            holder.action = convertView.findViewById(R.id.description);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AboutItem aboutItem = aboutItems.get(position);

        holder.title.setText(aboutItem.getTitle());
        holder.action.setText(aboutItem.getAction());

        return convertView;
    }
}
