package com.grsu.guideapp.views.dialogs;

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
        } catch (ClassCastException ignored) {

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        selectedItemsIndexList = new ArrayList<>();

        Builder builder = new Builder(getActivity());

        builder.setTitle("Radius (in meters)").setMultiChoiceItems(items, checkedItems, this)
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

}
