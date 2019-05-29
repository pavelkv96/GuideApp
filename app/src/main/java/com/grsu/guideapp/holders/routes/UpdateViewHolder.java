package com.grsu.guideapp.holders.routes;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.listeners.ItemClickListener;
import com.grsu.guideapp.models.Route;

public class UpdateViewHolder extends BaseRouteViewHolder {

    private static final String TAG = UpdateViewHolder.class.getSimpleName();

    @BindView(R.id.btn_item_routes_update)
    protected Button btn_item_routes_update;

    public UpdateViewHolder(@NonNull View pView, ItemClickListener listener) {
        super(pView, listener);
    }

    @Override
    public void bind(final Route route) {
        super.bind(route);
        btn_item_routes_update.setTag(Const.UPDATE);
        btn_item_routes_update.setOnClickListener(this);
    }
}
