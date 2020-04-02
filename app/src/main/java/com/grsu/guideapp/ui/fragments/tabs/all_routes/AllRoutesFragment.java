//package com.grsu.guideapp.ui.fragments.tabs.all_routes;
//
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import com.grsu.guideapp.App;
//import com.grsu.guideapp.R;
//import com.grsu.guideapp.ui.adapters.AllRoutesListAdapter;
//import com.grsu.guideapp.data.local.database.vo.RouteItemVO;
//import com.grsu.guideapp.utils.CheckPermission;
//import java.util.List;
//
//import kotlin.Unit;
//import kotlin.jvm.functions.Function2;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import timber.log.Timber;
//
//public class AllRoutesFragment extends Fragment<>
//        implements AllRoutesViews, Callback<Object>, OnRefreshListener,
//        Function2<View, Integer, Unit> {
//
//    private List<RouteItemVO> listRoutes;
//
//    AllRoutesListAdapter adapter;
////    @BindView(R.id.rv_fragment_list_routes)
//    RecyclerView rw_fragment_list_routes;
//
////    @BindView(R.id.srl_fragment_all_routes)
//    SwipeRefreshLayout srl_fragment_list_routes;
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        mPresenter.getListRoutes(getString(R.string.locale));
//        if (adapter != null) {
//            adapter.submitList(listRoutes);
//        }
//    }
//
//    @NonNull
//    @Override
//    protected Object getPresenterInstance() {
//        return new Object(this, new Object());
//    }
//
//    @Override
//    protected int getLayout() {
//        return R.layout.fragment_all_routes;
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//            @Nullable Bundle savedInstanceState) {
//        View view = super.onCreateView(inflater, container, savedInstanceState);
//        rw_fragment_list_routes.setHasFixedSize(true);
//        rw_fragment_list_routes.setLayoutManager(new LinearLayoutManager(getContext()));
//        srl_fragment_list_routes.setOnRefreshListener(this);
//        return view;
//    }
//
//    @Override
//    public void showMessage(Throwable throwable) {
//        Timber.e(throwable);
//    }
//
//    @Override
//    public void setData(final List<RouteItemVO> routes) {
//        listRoutes = routes;
//        adapter = new AllRoutesListAdapter(listRoutes, this);
//        rw_fragment_list_routes.setAdapter(adapter);
//    }
//
//    @Override
//    public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
//        Timber.e("onResponse: ");
//        if (response.isSuccessful()) {
//            if (response.body() != null && adapter != null) {
//                List<Object> datums = response.body().getDatums();
//                helper.loadRoute(this, datums);
//            }
//        } else {
//            showToast(response.message());
//            srl_fragment_list_routes.setRefreshing(false);
//        }
//    }
//
//    @Override
//    public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
//        if (!call.isCanceled()) {
//            Timber.e(t, "onFailure: ");
//        } else {
//            Timber.e("onFailure: IS CANCELED");
//        }
//        srl_fragment_list_routes.setRefreshing(false);
//    }
//
//    @Override
//    public void onFinished(final Integer integer) {
//        App.getThread().mainThread(new Runnable() {
//            @Override
//            public void run() {
//                if (srl_fragment_list_routes != null) {
//                    srl_fragment_list_routes.setRefreshing(false);
//                }
//                showToast(integer);
////                listRoutes = helper.getListRoutes(getString(R.string.locale));
//                listRoutes = null;
//                if (adapter!=null)
//                adapter.submitList(listRoutes);
//            }
//        });
//    }
//
//    @Override
//    public void onRefresh() {
//        srl_fragment_list_routes.setRefreshing(true);
//        if (App.isOnline()) {
//            Call<Object> root = App.getThread().networkIO().getRoutes();
//            root.enqueue(this);
//        } else {
//            srl_fragment_list_routes.setRefreshing(false);
//            showToast(getString(R.string.no_internet_connection));
//        }
//    }
//
//    public static AllRoutesFragment newInstance() {
//        return new AllRoutesFragment();
//    }
//
//    @Override
//    public Unit invoke(View view, Integer position) {
//        if (CheckPermission.INSTANCE.checkStoragePermission(requireActivity()) && listRoutes != null) {
//        } else {
//            int message = R.string.error_snackbar_do_not_have_permission_write_on_the_storage;
//            Snackbar.make(getView(), message, getActivity());
//        }
//        return null;
//    }
//}
