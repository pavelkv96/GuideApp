package com.grsu.guideapp.views.dialogs;

import static com.grsu.guideapp.utils.Constants.KEY_SELECTED_ITEM;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import java.util.ArrayList;

public class CustomMultiChoiceItemsDialogFragment extends DialogFragment implements
        OnMultiChoiceClickListener, OnClickListener {

    private CharSequence[] items = {"Type A", "Type B", "Type C", "Type D"};
    private ArrayList<Integer> selectedItemsIndexList;
    boolean[] checkedItems = {false, false, false, false};
    private OnMultiChoiceListDialogFragment dialogListener;

    public interface OnMultiChoiceListDialogFragment {

        void onOk(ArrayList<Integer> arrayList);
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
            selectedItemsIndexList = getArguments().getIntegerArrayList(KEY_SELECTED_ITEM);

            if (selectedItemsIndexList != null) {
                for (int i = 0; i < selectedItemsIndexList.size(); i++) {
                    checkedItems[selectedItemsIndexList.get(i) - 1] = true;
                }
            }
        }

        Builder builder = new Builder(getActivity());

        builder.setTitle("Radius (in meters)")
                .setMultiChoiceItems(items, checkedItems, this)
                .setPositiveButton("OK", this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        dialogListener.onOk(selectedItemsIndexList);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
        if (isChecked) {
            selectedItemsIndexList.add(which + 1);
        } else {
            if (selectedItemsIndexList.contains(which + 1)) {
                selectedItemsIndexList.remove(Integer.valueOf(which + 1));
            }
        }
    }

    public static CustomMultiChoiceItemsDialogFragment newInstance(
            ArrayList<Integer> selectedItemsIndexList) {

        Bundle args = new Bundle();
        args.putIntegerArrayList(KEY_SELECTED_ITEM, selectedItemsIndexList);

        CustomMultiChoiceItemsDialogFragment fragment = new CustomMultiChoiceItemsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
