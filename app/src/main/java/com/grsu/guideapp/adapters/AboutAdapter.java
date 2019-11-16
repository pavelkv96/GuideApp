package com.grsu.guideapp.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.listeners.ItemClickListener;
import com.grsu.guideapp.fragments.about.AboutItem;
import com.grsu.guideapp.holders.about.AboutHolder;
import java.util.List;

public class AboutAdapter extends RecyclerView.Adapter<AboutHolder> {

    private List<AboutItem> aboutItems;
    private ItemClickListener listener;

    public AboutAdapter(List<AboutItem> aboutItems, ItemClickListener listener) {
        this.aboutItems = aboutItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AboutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater from = LayoutInflater.from(parent.getContext());
        View view = from.inflate(R.layout.item_about, parent, false);
        return new AboutHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AboutHolder holder, int position) {
        AboutItem item = aboutItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return aboutItems != null ? aboutItems.size() : 0;
    }
}
