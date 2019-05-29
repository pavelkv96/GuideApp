package com.grsu.guideapp.holders.routes;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.listeners.ItemClickListener;
import com.grsu.guideapp.models.Route;

public class NotDownloadViewHolder extends BaseRouteViewHolder {

    private static final String TAG = NotDownloadViewHolder.class.getSimpleName();
    @BindView(R.id.btn_item_routes_about)
    protected Button btn_item_routes_about;
    @BindView(R.id.btn_item_routes_download)
    protected Button btn_item_routes_download;

    public NotDownloadViewHolder(@NonNull View pView, ItemClickListener listener) {
        super(pView, listener);
    }

    public void bind(final Route route) {
        super.bind(route);
        itemView.setOnClickListener(null);
        btn_item_routes_about.setOnClickListener(this);
        btn_item_routes_about.setTag(Const.ABOUT);
        btn_item_routes_download.setTag(Const.DOWNLOAD);
        btn_item_routes_download.setOnClickListener(this);
    }
}
