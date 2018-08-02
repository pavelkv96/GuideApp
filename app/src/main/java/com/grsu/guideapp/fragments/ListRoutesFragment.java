package com.grsu.guideapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import com.grsu.guideapp.R;
import com.grsu.guideapp.adapters.RoutesListAdapter;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.models.Routes;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import okhttp3.Route;

public class ListRoutesFragment extends Fragment {

    private RecyclerView rw_content_activity_found_routes;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_routes, container, false);

        DatabaseHelper mDBHelper = new DatabaseHelper(getContext());

        File database = getContext().getDatabasePath(DatabaseHelper.DBNAME);
        if (!database.exists()) {
            mDBHelper.getReadableDatabase();
            //Copy db
            if (copyDatabase(getContext())) {
                Toast.makeText(getContext(), "Copy database success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Copy data error", Toast.LENGTH_SHORT).show();
                return view;
            }
        }

        List<Routes> mProductList = mDBHelper.getListProduct();

        rw_content_activity_found_routes = view.findViewById(R.id.recycler_view);
        rw_content_activity_found_routes.setHasFixedSize(true);
        rw_content_activity_found_routes.setLayoutManager(new LinearLayoutManager(getContext()));

        loadData(mProductList);

        return view;
    }

    private void loadData(List<Routes> mProductList) {
        rw_content_activity_found_routes
                .setAdapter(new RoutesListAdapter(mProductList));
    }

    private boolean copyDatabase(Context context) {
        try {

            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.e("MainActivity", "DB copied");
            return true;
        } catch (Exception e) {
            e.getMessage();
            Log.e("MainActivity", "DB don't copied");

            return false;
        }
    }
}
