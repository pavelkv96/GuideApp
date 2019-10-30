package com.grsu.guideapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.listeners.ItemClickListener;
import com.grsu.guideapp.holders.types.TypeHolder;
import com.grsu.guideapp.models.DtoType;
import java.util.List;

public class TypesAdapter extends Adapter<TypeHolder> {

    private List<DtoType> mTypes;
    private ItemClickListener mListener;

    public TypesAdapter(List<DtoType> types, ItemClickListener listener) {
        mTypes = types;
        mListener = listener;
    }

    @NonNull
    @Override
    public TypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type, parent, false);
        return new TypeHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeHolder holder, int position) {
        holder.bind(mTypes.get(position));
    }

    @Override
    public int getItemCount() {
        return mTypes != null ? mTypes.size() : 0;
    }

    public void setTypeList(List<DtoType> typeList) {
        this.mTypes = typeList;
        notifyDataSetChanged();
    }

}
