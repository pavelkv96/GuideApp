package com.grsu.guideapp.holders.about;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseViewHolder;
import com.grsu.guideapp.base.listeners.ItemClickListener;
import com.grsu.guideapp.fragments.about.AboutItem;

public class AboutHolder extends BaseViewHolder<AboutItem> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.description)
    TextView action;

    public AboutHolder(View itemView, ItemClickListener listener) {
        super(itemView, listener);
    }

    @Override
    public void bind(AboutItem item) {
        Context context = itemView.getContext();
        title.setText(context.getString(item.getTitle()));
        action.setText(context.getString(item.getAction()));
    }
}
