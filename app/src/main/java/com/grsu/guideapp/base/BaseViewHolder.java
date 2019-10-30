package com.grsu.guideapp.base;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import butterknife.ButterKnife;
import com.grsu.guideapp.base.listeners.ItemClickListener;

public abstract class BaseViewHolder<T> extends ViewHolder implements OnClickListener {

    protected ItemClickListener listener;

    public BaseViewHolder(View itemView, ItemClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    public abstract void bind(T type);

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onItemClick(view, getAdapterPosition());
        }
    }
}
