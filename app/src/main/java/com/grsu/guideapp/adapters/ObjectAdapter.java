package com.grsu.guideapp.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.listeners.ItemClickListener;
import com.grsu.guideapp.holders.object.ObjectHolder;
import com.grsu.guideapp.models.DtoObject;
import java.util.ArrayList;
import java.util.List;

public class ObjectAdapter extends Adapter<ObjectHolder> implements Filterable {

    private List<DtoObject> mObjects;
    private List<DtoObject> mObjectsFiltered;
    private ItemClickListener mListener;
    private ObjectFilter filter;

    public ObjectAdapter(List<DtoObject> objects, ItemClickListener listener) {
        mObjects = objects;
        mObjectsFiltered = objects;
        mListener = listener;
    }

    @NonNull
    @Override
    public ObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_object, parent, false);
        return new ObjectHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ObjectHolder holder, int position) {
        holder.bind(mObjectsFiltered.get(position));
    }

    @Override
    public int getItemCount() {
        return mObjectsFiltered != null ? mObjectsFiltered.size() : 0;
    }

    public void setObjectList(List<DtoObject> typeList) {
        this.mObjects = typeList;
        this.mObjectsFiltered = typeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new ObjectFilter();
        return filter;
    }

    private class ObjectFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            FilterResults results = new FilterResults();

            if (chars != null && chars.length() > 0) {
                List<DtoObject> filteredList = new ArrayList<>();
                for (DtoObject object : mObjects) {
                    if (object.getName().toUpperCase().contains(chars.toString().toUpperCase())) {
                        filteredList.add(object);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            } else {
                results.count = mObjects.size();
                results.values = mObjects;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mObjectsFiltered = (List<DtoObject>) results.values;
            notifyDataSetChanged();
        }
    }
}
