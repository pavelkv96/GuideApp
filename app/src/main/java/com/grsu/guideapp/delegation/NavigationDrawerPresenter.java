package com.grsu.guideapp.delegation;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static com.grsu.guideapp.utils.CheckSelfPermission.checkVersionSdk;
import static com.grsu.guideapp.utils.CheckSelfPermission.groupExternalStorage;
import static com.grsu.guideapp.utils.CheckSelfPermission.writeExternalStorageIsGranted;

import android.os.Build.VERSION_CODES;
import android.support.v4.app.Fragment;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.delegation.NavigationDrawerContract.NavigationDrawerView;
import com.grsu.guideapp.utils.CheckSelfPermission;
import com.grsu.guideapp.utils.MessageViewer.Logs;

public class NavigationDrawerPresenter
        extends BasePresenterImpl<NavigationDrawerView>
        implements NavigationDrawerContract.NavigationDrawerPresenter {

    private static final int CODE_PERMISSION_GROUP_EXTERNAL_STORAGE = 1;
    private static final String TAG = NavigationDrawerPresenter.class.getSimpleName();

    @Override
    public void replaceFragment(Fragment fragment) {
        Logs.e(TAG, "replaceFragment: No");
        getView().getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).commit();
    }

    @Override
    public boolean checkPermissions() {

        if (checkVersionSdk(VERSION_CODES.M)) {

            if (writeExternalStorageIsGranted(getView().getActivity())) {

                requestPermissions(getView().getActivity(), groupExternalStorage,
                        CODE_PERMISSION_GROUP_EXTERNAL_STORAGE);

                Logs.e(TAG, "replaceFragment: Yes");

                return false;
            }
        }

        return true;
    }

    @Override
    public void checkPermissionsResult(int requestCode, int[] grantResults) {
        switch (requestCode) {
            case CODE_PERMISSION_GROUP_EXTERNAL_STORAGE: {
                if (CheckSelfPermission.isAllGranted(grantResults)) {
                    mView.openMapFragment();
                    Logs.e(TAG, "onRequestPermissionsResult: if");
                } else {
                    mView.showToastMessage("Don't have permission");
                    Logs.e(TAG, "onRequestPermissionsResult: else");
                }
            }
            break;
        }

    }
}
