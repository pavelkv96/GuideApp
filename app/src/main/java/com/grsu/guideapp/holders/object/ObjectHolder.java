package com.grsu.guideapp.holders.object;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseViewHolder;
import com.grsu.guideapp.base.listeners.ItemClickListener;
import com.grsu.guideapp.models.DtoObject;
import com.squareup.picasso.Picasso;

public class ObjectHolder extends BaseViewHolder<DtoObject> {

//    @BindView(R.id.item_type_image)
    ImageView mItemTypeImage;
//    @BindView(R.id.item_type_name)
    TextView mItemTypeName;

    public ObjectHolder(View itemView, ItemClickListener listener) {
        super(itemView, listener);
    }

    @Override
    public void bind(DtoObject object) {
        mItemTypeName.setText(object.getName());
        Picasso.get().load(object.getPhoto()).error(R.drawable.ic_launcher_background).into(mItemTypeImage);
    }
}
