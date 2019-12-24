package com.grsu.guideapp.holders.types;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseViewHolder;
import com.grsu.guideapp.base.listeners.ItemClickListener;
import com.grsu.guideapp.models.DtoType;

public class TypeHolder extends BaseViewHolder<DtoType> {

//    @BindView(R.id.item_type_image)
    ImageView mItemTypeImage;
//    @BindView(R.id.item_type_name)
    TextView mItemTypeName;

    public TypeHolder(View itemView, ItemClickListener listener) {
        super(itemView, listener);
    }

    @Override
    public void bind(DtoType type) {
        mItemTypeName.setText(type.getName());
        mItemTypeImage.setImageResource(getImage(type.getId()));
    }

    private int getImage(int idCategory) {
        return R.mipmap.ic_launcher;
    }
}
