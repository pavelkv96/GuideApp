package com.grsu.guideapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.grsu.guideapp.R;
import com.grsu.guideapp.adapters.RoutesListAdapter;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.io.File;
import java.util.List;

public class ListRoutesFragment extends Fragment {

    private RecyclerView rw_fragment_list_routes;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_routes, container, false);

        DatabaseHelper mDBHelper = new DatabaseHelper(getContext());

        File database = getContext().getDatabasePath(Settings.DATABASE_INFORMATION_NAME);
        if (!database.exists()) {
            mDBHelper.getReadableDatabase();
            //Copy db
            if (DatabaseHelper.copyDatabase(getContext())) {
                Logs.e(ListRoutesFragment.class.getSimpleName(), "Copy database success");
            } else {
                Logs.e(ListRoutesFragment.class.getSimpleName(), "Copy data error");
                return rootView;
            }
        }

        List<Route> mRoutesList = mDBHelper.getListRoutes();

        rw_fragment_list_routes = rootView.findViewById(R.id.rw_fragment_list_routes);
        rw_fragment_list_routes.setHasFixedSize(true);
        rw_fragment_list_routes.setLayoutManager(new LinearLayoutManager(getContext()));

        loadData(mRoutesList);

        return rootView;
    }

    private void loadData(List<Route> mRoutesList) {
        rw_fragment_list_routes.setAdapter(new RoutesListAdapter(getActivity(), mRoutesList));
    }

}
