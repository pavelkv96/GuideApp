package com.grsu.guideapp.fragments.categories;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.adapters.TypesAdapter;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.base.listeners.ItemClickListener;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.fragments.categories.CategoriesContract.CategoriesViews;
import com.grsu.guideapp.fragments.list_objects.ListObjectFragment;
import com.grsu.guideapp.models.DtoType;
import java.util.List;

public class CatalogFragment extends BaseFragment<CategoriesPresenter, FragmentActivity>
        implements CategoriesViews, ItemClickListener {

    private List<DtoType> mTypes;
    TypesAdapter adapter;
//    @BindView(R.id.fragment_catalog_rv)
    RecyclerView fragment_catalog_rv;
//    @BindView(R.id.fragment_catalog_empty_tv)
    TextView fragment_catalog_empty_tv;

    @NonNull
    @Override
    protected CategoriesPresenter getPresenterInstance() {
        return new CategoriesPresenter(this, new CategoriesInteractor(new Test(getContext())));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_catalog;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.list_objects_fragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);

//        getActivity.setTitleToolbar(getTitle());
        fragment_catalog_rv.setHasFixedSize(true);
        fragment_catalog_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        fragment_catalog_rv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new TypesAdapter(null, this);
        fragment_catalog_rv.setAdapter(adapter);
        mPresenter.getAllTypes(getString(R.string.locale));
        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        DtoType type = mTypes.get(position);
        ListObjectFragment fragment = ListObjectFragment.newInstance(type.getId(), type.getName());
        getActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(fragment.getClass().getName() + "_" + position)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_routes, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_list_routes == item.getItemId()) {
//            getActivity.openListRoutesFragment();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateList(List<DtoType> types) {
        mTypes = types;
        if (adapter != null) {
            adapter.setTypeList(types);
        }
    }

    @Override
    public void emptyData() {
        fragment_catalog_rv.setVisibility(View.GONE);
        fragment_catalog_empty_tv.setVisibility(View.VISIBLE);
        fragment_catalog_empty_tv.setText(R.string.empty_list);
    }
}
