package com.grsu.guideapp.fragments.tabs.list_routes;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.grsu.guideapp.R;
import com.grsu.guideapp.adapters.ViewPagerAdapter;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.fragments.tabs.all_routes.AllRoutesFragment;
import com.grsu.guideapp.fragments.tabs.list_routes.ListRoutesContract.ListRoutesViews;
import com.grsu.guideapp.fragments.tabs.my_routes.MyRoutesFragment;

public class ListRoutesFragment extends BaseFragment<ListRoutesPresenter, NavigationDrawerActivity>
        implements ListRoutesViews {

    private static final String TAG = ListRoutesFragment.class.getSimpleName();

//    @BindView(R.id.tl_fragment_list_routes)
    TabLayout mTabLayout;

//    @BindView(R.id.vp_fragment_list_routes)
    ViewPager mViewPager;

    @NonNull
    @Override
    protected ListRoutesPresenter getPresenterInstance() {
        return new ListRoutesPresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_list_routes;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.list_routes_fragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setHasOptionsMenu(true);

        getActivity.setTitleToolbar(getTitle());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(MyRoutesFragment.newInstance(), getString(R.string.my_tab));
        adapter.addFragment(AllRoutesFragment.newInstance(), getString(R.string.all_tab));

        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_objects, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_list_objects == item.getItemId()) {
            getActivity.openCatalogFragment();
        }

        return super.onOptionsItemSelected(item);
    }
}
