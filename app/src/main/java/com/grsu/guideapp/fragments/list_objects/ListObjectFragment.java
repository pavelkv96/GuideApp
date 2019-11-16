package com.grsu.guideapp.fragments.list_objects;

import android.app.SearchManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.details.DetailsActivity;
import com.grsu.guideapp.adapters.ObjectAdapter;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.base.listeners.ItemClickListener;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.fragments.list_objects.ListObjectContract.ObjectView;
import com.grsu.guideapp.models.DtoObject;
import com.grsu.guideapp.project_settings.Constants;
import java.util.List;

public class ListObjectFragment extends BaseFragment<ListObjectPresenter, NavigationDrawerActivity>
        implements ObjectView, ItemClickListener, OnQueryTextListener {

    private List<DtoObject> mObjects;
//    @BindView(R.id.fragment_list_object_rv)
    RecyclerView mRecyclerView;
//    @BindView(R.id.fragment_list_object_empty)
    TextView fragment_list_object_empty;
    private ObjectAdapter mAdapter;

    @NonNull
    @Override
    protected ListObjectPresenter getPresenterInstance() {
        return new ListObjectPresenter(this, new ListObjectInteractor(new Test(getContext())));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_list_objects;
    }

    @Override
    protected String getTitle() {
        return "";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setHasOptionsMenu(true);

        String title = getTitle();
        if (getArguments() != null) {
            int type = getArguments().getInt(Constants.KEY_ID_TYPE);
            title = getArguments().getString(Constants.KEY_NAME_TYPE);
            mPresenter.getObject(getString(R.string.locale), type);
        }

        getActivity.setTitleToolbar(title);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity));
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity, DividerItemDecoration.VERTICAL));
        mAdapter = new ObjectAdapter(null, this);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    public static ListObjectFragment newInstance(int type, String name) {
        Bundle args = new Bundle();
        ListObjectFragment fragment = new ListObjectFragment();
        args.putInt(Constants.KEY_ID_TYPE, type);
        args.putString(Constants.KEY_NAME_TYPE, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClick(View view, int position) {
        if (getActivity() != null && mObjects != null && !mObjects.isEmpty()) {
            DetailsActivity.newInstance(getActivity(), mObjects.get(position).getId());
        }
    }

    @Override
    public void updateAdapter(List<DtoObject> objects) {
        mObjects = objects;
        if (mAdapter != null) {
            mAdapter.setObjectList(objects);
        }
    }

    @Override
    public void emptyData() {
        mRecyclerView.setVisibility(View.GONE);
        fragment_list_object_empty.setVisibility(View.VISIBLE);
        fragment_list_object_empty.setText(R.string.empty_list);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);

        SearchView view = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager manager = ContextCompat.getSystemService(getActivity, SearchManager.class);
        if (manager == null) {
            return;
        }
        view.setSearchableInfo(manager.getSearchableInfo(getActivity.getComponentName()));
        view.setImeOptions(view.getImeOptions() | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        view.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_list_routes == item.getItemId()) {
            getActivity.openListRoutesFragment();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
        return false;
    }
}
