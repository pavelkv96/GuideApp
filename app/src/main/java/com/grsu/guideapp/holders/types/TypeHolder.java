package com.grsu.guideapp.holders.types;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseViewHolder;
import com.grsu.guideapp.base.listeners.ItemClickListener;
import com.grsu.guideapp.models.DtoType;

public class TypeHolder extends BaseViewHolder<DtoType> {

    @BindView(R.id.item_type_image)
    ImageView mItemTypeImage;
    @BindView(R.id.item_type_name)
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
        switch (idCategory) {
            case 6:
                return R.drawable.id_6;
            case 107:
                return R.drawable.id_107;
            case 108:
                return R.drawable.id_108;
            case 109:
                return R.drawable.id_109;
            case 110:
                return R.drawable.id_110;
            case 111:
                return R.drawable.id_111;
            case 112:
                return R.drawable.id_112;
            case 113:
                return R.drawable.id_113;
            case 114:
                return R.drawable.id_114;
            case 115:
                return R.drawable.id_115;
            default:
                return R.mipmap.ic_launcher;
        }
    }
}
