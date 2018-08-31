package com.grsu.guideapp.views.dialogs;

import static com.grsu.guideapp.utils.Constants.KEY_CHECKED_ITEM;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class CustomSingleChoiceItemsDialogFragment extends DialogFragment implements
        OnClickListener {

    private CharSequence[] items = {"1000", "500", "250", "100"};
    private String item = items[items.length - 1].toString();
    private OnChoiceItemListener onChoiceItemListener;
    private int checkedItem;

    public interface OnChoiceItemListener {

        void choiceItem(String itemValue);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onChoiceItemListener = (OnChoiceItemListener) getActivity();
        } catch (ClassCastException ignored) {
            onChoiceItemListener = (OnChoiceItemListener) getParentFragment();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            getCheckedItem(getArguments().getCharSequence(KEY_CHECKED_ITEM, "100").toString());
        }
        AlertDialog.Builder builder = new Builder(getActivity());

        builder.setTitle("Radius (in meters)").setSingleChoiceItems(items, checkedItem, this)
                .setPositiveButton("OK", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onChoiceItemListener.choiceItem(item);
                    }
                });
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case 0: {
                item = items[i].toString();
            }
            case 1: {
                item = items[i].toString();
            }
            case 2: {
                item = items[i].toString();
            }
            break;
            case 3:
                item = items[i].toString();
        }
    }

    private void getCheckedItem(String value) {
        for (int i = 0; i < items.length; i++) {
            if (value.equals(items[i].toString())) {
                checkedItem = i;
                return;
            }
        }
    }

    public static CustomSingleChoiceItemsDialogFragment newInstance(CharSequence checkedItem) {
        CustomSingleChoiceItemsDialogFragment fragment = new CustomSingleChoiceItemsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putCharSequence(KEY_CHECKED_ITEM, checkedItem);
        fragment.setArguments(bundle);
        return fragment;
    }

}
