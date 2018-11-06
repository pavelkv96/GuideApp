package com.grsu.guideapp.views.dialogs;

import static com.grsu.guideapp.project_settings.Constants.KEY_SELECTED_ITEM;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.ListView;
import com.grsu.guideapp.R;

public class CustomMultiChoiceItemsDialogFragment extends DialogFragment implements
        OnMultiChoiceClickListener, OnClickListener {

    boolean[] checkedItems = {false, false, false, false, false};
    private OnMultiChoiceListDialogFragment dialogListener;

    public interface OnMultiChoiceListDialogFragment {

        void onOk(long[] arrayList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            dialogListener = (OnMultiChoiceListDialogFragment) getActivity();
        } catch (ClassCastException e) {
            dialogListener = (OnMultiChoiceListDialogFragment) getParentFragment();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            long[] selectedItemsIndexList = getArguments().getLongArray(KEY_SELECTED_ITEM);

            if (selectedItemsIndexList != null) {
                for (long aSelectedItemsIndexList : selectedItemsIndexList) {
                    checkedItems[(int) (aSelectedItemsIndexList)] = true;
                }
            }
        }

        Builder builder = new Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.dialog_types_objects));
        builder.setMultiChoiceItems(R.array.multi_items, checkedItems, this);
        builder.setPositiveButton(getResources().getString(R.string.ok), this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        long[] checkedItemIds = ((AlertDialog) dialogInterface).getListView().getCheckItemIds();
        dialogListener.onOk(checkedItemIds);
    }

    @Override
    public void onClick(DialogInterface dialogs, int which, boolean isChecked) {
        ListView list = ((AlertDialog) dialogs).getListView();

        int checkedItemCount = list.isItemChecked(0) ? list.getCheckedItemCount() - 1
                : list.getCheckedItemCount();

        if (checkedItemCount < checkedItems.length - 1) {
            list.setItemChecked(0, false);
            checkedItems[0] = false;
        } else {
            list.setItemChecked(0, true);
            checkedItems[0] = true;
        }

        if (which == 0) {
            for (int i = 0; i < checkedItems.length; i++) {
                if (list.isItemChecked(i) && !isChecked) {
                    list.setItemChecked(i, false);
                    checkedItems[i] = false;
                } else {
                    list.setItemChecked(i, true);
                    checkedItems[i] = true;
                }
            }
        }
    }

    public static CustomMultiChoiceItemsDialogFragment newInstance(long[] IndexList) {

        Bundle args = new Bundle();
        args.putLongArray(KEY_SELECTED_ITEM, IndexList);

        CustomMultiChoiceItemsDialogFragment fragment = new CustomMultiChoiceItemsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static String getTags(){
        return CustomMultiChoiceItemsDialogFragment.class.getSimpleName();
    }
}
